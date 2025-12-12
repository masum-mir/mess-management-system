package com.mms.service;

import com.mms.model.Meal;
import com.mms.model.MealAttendance;
import com.mms.model.MealShare;
import com.mms.model.Member;
import com.mms.repository.MealRepository;
import com.mms.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final MemberRepository memberRepository;

    public MealService(MealRepository mealRepository, MemberRepository memberRepository) {
        this.mealRepository = mealRepository;
        this.memberRepository = memberRepository;
    }

    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    public List<Meal> getMealsByMonth(int month, int year) {
        return mealRepository.findByMonth(month, year);
    }

    public Meal getMealById(Integer id) {
        return mealRepository.findById(id);
    }

    @Transactional
    public Meal saveMealWithAttendance(Meal meal, List<Integer> attendingMemberIds) {
        // Calculate per person cost
        int attendeeCount = attendingMemberIds.size();
        BigDecimal perPersonCost = BigDecimal.ZERO;

        if (attendeeCount > 0) {
            perPersonCost = meal.getCost().divide(
                    BigDecimal.valueOf(attendeeCount), 2, RoundingMode.HALF_UP
            );
        }

        meal.setTotalAttendees(attendeeCount);
        meal.setPerPersonCost(perPersonCost);

        // Save meal
        Integer mealId = mealRepository.save(meal);
        System.out.println("Meal ID:: "+ mealId);
        meal.setMealId(mealId);

        // Mark attendance for all active members
        List<Member> allMembers = memberRepository.findActiveMembers();

        for (Member member : allMembers) {
            MealAttendance attendance = new MealAttendance();
            attendance.setMealId(mealId);
            attendance.setMemberId(member.getMemberId());
            attendance.setIsPresent(attendingMemberIds.contains(member.getMemberId()));
            attendance.setMarkedBy(meal.getCreatedBy());

            mealRepository.saveAttendance(attendance);

            // Save meal share only for attending members
            if (attendingMemberIds.contains(member.getMemberId())) {
                MealShare share = new MealShare();
                share.setMealId(mealId);
                share.setMemberId(member.getMemberId());
                share.setShareAmount(perPersonCost);

                mealRepository.saveMealShare(share);
            }
        }

        return meal;
    }

    @Transactional
    public void updateMeal(Meal meal) {
        mealRepository.update(meal);
    }

    @Transactional
    public void deleteMeal(Integer id) {
        mealRepository.delete(id);
    }

    public List<MealAttendance> getMealAttendance(Integer mealId) {
        return mealRepository.findAttendanceByMealId(mealId);
    }

    public List<MealShare> getMealShares(Integer mealId) {
        return mealRepository.findSharesByMealId(mealId);
    }

    /**
     * Update attendance for a meal
     */
    @Transactional
    public void updateMealAttendance(Integer mealId, List<Integer> attendingMemberIds) {
        Meal meal = mealRepository.findById(mealId);

        if (meal == null) {
            throw new RuntimeException("Meal not found");
        }

        // Delete old attendance and shares
        mealRepository.deleteAttendance(mealId);
        mealRepository.deleteMealShares(mealId);

        // Recalculate per person cost
        int attendeeCount = attendingMemberIds.size();
        BigDecimal perPersonCost = BigDecimal.ZERO;

        if (attendeeCount > 0) {
            perPersonCost = meal.getCost().divide(
                    BigDecimal.valueOf(attendeeCount), 2, RoundingMode.HALF_UP
            );
        }

        meal.setTotalAttendees(attendeeCount);
        meal.setPerPersonCost(perPersonCost);
        mealRepository.update(meal);

        // Save new attendance and shares
        List<Member> allMembers = memberRepository.findActiveMembers();

        for (Member member : allMembers) {
            MealAttendance attendance = new MealAttendance();
            attendance.setMealId(mealId);
            attendance.setMemberId(member.getMemberId());
            attendance.setIsPresent(attendingMemberIds.contains(member.getMemberId()));
            attendance.setMarkedBy(meal.getCreatedBy());

            mealRepository.saveAttendance(attendance);

            if (attendingMemberIds.contains(member.getMemberId())) {
                MealShare share = new MealShare();
                share.setMealId(mealId);
                share.setMemberId(member.getMemberId());
                share.setShareAmount(perPersonCost);

                mealRepository.saveMealShare(share);
            }
        }
    }
}