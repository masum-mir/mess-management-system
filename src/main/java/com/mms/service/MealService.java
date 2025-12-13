package com.mms.service;

import com.mms.model.Meal;
import com.mms.model.MealAttendance;
import com.mms.model.MealShare;
import com.mms.model.Member;
import com.mms.repository.MealAttendanceRepository;
import com.mms.repository.MealRepository;
import com.mms.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Service
//public class MealService {
//
//    private final MealRepository mealRepository;
//    private final MemberRepository memberRepository;
//
//    public MealService(MealRepository mealRepository, MemberRepository memberRepository) {
//        this.mealRepository = mealRepository;
//        this.memberRepository = memberRepository;
//    }
//
//    public List<Meal> getAllMeals() {
//        return mealRepository.findAll();
//    }
//
//    public List<Meal> getMealsByMonth(int month, int year) {
//        return mealRepository.findByMonth(month, year);
//    }
//
//    public List<Meal> getMealsByDate(LocalDate date) {
//        return mealRepository.findByDate(date);
//    }
//
//    public Meal getMealById(Integer id) {
//        return mealRepository.findById(id);
//    }
//
//    /**
//     * Save meal with attendance for a specific meal type (Breakfast/Lunch/Dinner)
//     */
//    @Transactional
//    public Meal saveMealWithAttendance(LocalDate mealDate, String mealType, String description,
//                                       List<Integer> attendingMemberIds, Integer createdBy) {
//
//        // Check if meal already exists for this date and type
//        Meal existingMeal = mealRepository.findByDateAndType(mealDate, mealType);
//        if (existingMeal != null) {
//            throw new RuntimeException("Meal already exists for " + mealType + " on " + mealDate);
//        }
//
//        // Create new meal
//        Meal meal = new Meal();
//        meal.setMealDate(mealDate);
//        meal.setMealType(mealType);
//        meal.setDescription(description);
//        meal.setMonth(mealDate.getMonthValue());
//        meal.setYear(mealDate.getYear());
//        meal.setCreatedBy(createdBy);
//
//        // Save meal
//        Integer mealId = mealRepository.save(meal);
//        meal.setMealId(mealId);
//
//        // Mark attendance for all active members
//        List<Member> allMembers = memberRepository.findActiveMembers();
//
//        for (Member member : allMembers) {
//            MealAttendance attendance = new MealAttendance();
//            attendance.setMealId(mealId);
//            attendance.setMemberId(member.getMemberId());
//            attendance.setIsPresent(attendingMemberIds.contains(member.getMemberId()));
//            attendance.setMarkedBy(createdBy);
//
//            mealRepository.saveAttendance(attendance);
//        }
//
//        return meal;
//    }
//
//    /**
//     * Submit attendance for a specific date - creates/updates meals for each type
//     */
//    @Transactional
//    public void submitDailyAttendance(LocalDate date,
//                                      List<Integer> breakfastAttendees,
//                                      List<Integer> lunchAttendees,
//                                      List<Integer> dinnerAttendees,
//                                      Integer markedBy) {
//
//        // Process Breakfast
//        if (breakfastAttendees != null && !breakfastAttendees.isEmpty()) {
//            Meal existingBreakfast = mealRepository.findByDateAndType(date, "Breakfast");
//            if (existingBreakfast != null) {
//                updateMealAttendance(existingBreakfast.getMealId(), breakfastAttendees, markedBy);
//            } else {
//                saveMealWithAttendance(date, "Breakfast", "Daily breakfast", breakfastAttendees, markedBy);
//            }
//        }
//
//        // Process Lunch
//        if (lunchAttendees != null && !lunchAttendees.isEmpty()) {
//            Meal existingLunch = mealRepository.findByDateAndType(date, "Lunch");
//            if (existingLunch != null) {
//                updateMealAttendance(existingLunch.getMealId(), lunchAttendees, markedBy);
//            } else {
//                saveMealWithAttendance(date, "Lunch", "Daily lunch", lunchAttendees, markedBy);
//            }
//        }
//
//        // Process Dinner
//        if (dinnerAttendees != null && !dinnerAttendees.isEmpty()) {
//            Meal existingDinner = mealRepository.findByDateAndType(date, "Dinner");
//            if (existingDinner != null) {
//                updateMealAttendance(existingDinner.getMealId(), dinnerAttendees, markedBy);
//            } else {
//                saveMealWithAttendance(date, "Dinner", "Daily dinner", dinnerAttendees, markedBy);
//            }
//        }
//    }
//
//    @Transactional
//    public void updateMeal(Meal meal) {
//        mealRepository.update(meal);
//    }
//
//    @Transactional
//    public void deleteMeal(Integer id) {
//        // Delete related attendance and shares first
//        mealRepository.deleteAttendance(id);
//        mealRepository.deleteMealShares(id);
//        // Delete meal
//        mealRepository.delete(id);
//    }
//
//    public List<MealAttendance> getMealAttendance(Integer mealId) {
//        return mealRepository.findAttendanceByMealId(mealId);
//    }
//
//    public List<MealShare> getMealShares(Integer mealId) {
//        return mealRepository.findSharesByMealId(mealId);
//    }
//
//    /**
//     * Update attendance for a specific meal
//     */
//    @Transactional
//    public void updateMealAttendance(Integer mealId, List<Integer> attendingMemberIds, Integer markedBy) {
//        Meal meal = mealRepository.findById(mealId);
//
//        if (meal == null) {
//            throw new RuntimeException("Meal not found");
//        }
//
//        // Delete old attendance
//        mealRepository.deleteAttendance(mealId);
//
//        // Save new attendance for all active members
//        List<Member> allMembers = memberRepository.findActiveMembers();
//
//        for (Member member : allMembers) {
//            MealAttendance attendance = new MealAttendance();
//            attendance.setMealId(mealId);
//            attendance.setMemberId(member.getMemberId());
//            attendance.setIsPresent(attendingMemberIds.contains(member.getMemberId()));
//            attendance.setMarkedBy(markedBy);
//
//            mealRepository.saveAttendance(attendance);
//        }
//    }
//
//    /**
//     * Get attendance summary for a specific date
//     */
//    public Map<String, Object> getDailyAttendanceSummary(LocalDate date) {
//        Map<String, Object> summary = new HashMap<>();
//
//        List<Meal> mealsOnDate = mealRepository.findByDate(date);
//
//        for (Meal meal : mealsOnDate) {
//            List<MealAttendance> attendances = mealRepository.findAttendanceByMealId(meal.getMealId());
//            long presentCount = attendances.stream()
//                    .filter(MealAttendance::getIsPresent)
//                    .count();
//
//            String mealTypeKey = meal.getMealType().toLowerCase();
//            summary.put(mealTypeKey + "Count", presentCount);
//            summary.put(mealTypeKey + "MealId", meal.getMealId());
//        }
//
//        return summary;
//    }
//
//    /**
//     * Get monthly meal report for a member
//     */
//    public Map<String, Object> getMemberMonthlyReport(Integer memberId, int month, int year) {
//        List<Meal> monthlyMeals = mealRepository.findByMonth(month, year);
//
//        int totalMeals = 0;
//        int attendedMeals = 0;
//
//        for (Meal meal : monthlyMeals) {
//            List<MealAttendance> attendances = mealRepository.findAttendanceByMealId(meal.getMealId());
//
//            for (MealAttendance attendance : attendances) {
//                if (attendance.getMemberId().equals(memberId)) {
//                    totalMeals++;
//                    if (attendance.getIsPresent()) {
//                        attendedMeals++;
//                    }
//                    break;
//                }
//            }
//        }
//
//        Map<String, Object> report = new HashMap<>();
//        report.put("memberId", memberId);
//        report.put("month", month);
//        report.put("year", year);
//        report.put("totalMeals", totalMeals);
//        report.put("attendedMeals", attendedMeals);
//        report.put("missedMeals", totalMeals - attendedMeals);
//
//        return report;
//    }
//
//    /**
//     * Get all members' attendance for a specific date
//     */
//    public List<Map<String, Object>> getMembersAttendanceByDate(LocalDate date) {
//        List<Member> activeMembers = memberRepository.findActiveMembers();
//        List<Meal> mealsOnDate = mealRepository.findByDate(date);
//
//        List<Map<String, Object>> result = new ArrayList<>();
//
//        for (Member member : activeMembers) {
//            Map<String, Object> memberData = new HashMap<>();
//            memberData.put("memberId", member.getMemberId());
//            memberData.put("memberName", member.getName());
//
//            Map<String, Boolean> mealAttendance = new HashMap<>();
//
//            for (Meal meal : mealsOnDate) {
//                List<MealAttendance> attendances = mealRepository.findAttendanceByMealId(meal.getMealId());
//
//                for (MealAttendance attendance : attendances) {
//                    if (attendance.getMemberId().equals(member.getMemberId())) {
//                        mealAttendance.put(meal.getMealType().toLowerCase(), attendance.getIsPresent());
//                        break;
//                    }
//                }
//            }
//
//            memberData.put("attendance", mealAttendance);
//            result.add(memberData);
//        }
//
//        return result;
//    }
//}
//
//@Service
//public class MealService {
//
//    private final MealRepository mealRepository;
//    private final MemberRepository memberRepository;
//
//    public MealService(MealRepository mealRepository, MemberRepository memberRepository) {
//        this.mealRepository = mealRepository;
//        this.memberRepository = memberRepository;
//    }
//
//    public List<Meal> getAllMeals() {
//        return mealRepository.findAll();
//    }
//
//    public List<Meal> getMealsByMonth(int month, int year) {
//        return mealRepository.findByMonth(month, year);
//    }
//
//    public List<Meal> getMealsByDate(LocalDate date) {
//        return mealRepository.findByDate(date);
//    }
//
//    public Meal getMealById(Integer id) {
//        return mealRepository.findById(id);
//    }
//
//    /**
//     * Save meal with attendance for a specific meal type (Breakfast/Lunch/Dinner)
//     */
//    @Transactional
//    public Meal saveMealWithAttendance(LocalDate mealDate, String mealType, String description,
//                                       List<Integer> attendingMemberIds, Integer createdBy) {
//
//        // Check if meal already exists for this date and type
//        Meal existingMeal = mealRepository.findByDateAndType(mealDate, mealType);
//        if (existingMeal != null) {
//            throw new RuntimeException("Meal already exists for " + mealType + " on " + mealDate);
//        }
//
//        // Create new meal
//        Meal meal = new Meal();
//        meal.setMealDate(mealDate);
//        meal.setMealType(mealType);
//        meal.setMonth(mealDate.getMonthValue());
//        meal.setYear(mealDate.getYear());
//        meal.setCreatedBy(createdBy);
//
//        // Save meal
//        Integer mealId = mealRepository.save(meal);
//        meal.setMealId(mealId);
//
//        // Mark attendance for all active members
//        List<Member> allMembers = memberRepository.findActiveMembers();
//
//        for (Member member : allMembers) {
//            MealAttendance attendance = new MealAttendance();
//            attendance.setMealId(mealId);
//            attendance.setMemberId(member.getMemberId());
//            attendance.setIsPresent(attendingMemberIds.contains(member.getMemberId()));
//            attendance.setMarkedBy(createdBy);
//
//            mealRepository.saveAttendance(attendance);
//        }
//
//        return meal;
//    }
//    @Transactional
//    public Meal saveMealWithAttendance(Meal meal, List<Integer> attendingMemberIds) {
//
//        // Validate duplicate
//        Meal existing = mealRepository.findByDateAndType(meal.getMealDate(), meal.getMealType());
//        if (existing != null) {
//            throw new RuntimeException("Meal already exists for " +
//                    meal.getMealType() + " on " + meal.getMealDate());
//        }
//
//        // Set createdBy (default 1 or admin)
//        if (meal.getCreatedBy() == null) {
//            meal.setCreatedBy(1);
//        }
//
//        // Save meal
//        Integer mealId = mealRepository.save(meal);
//        meal.setMealId(mealId);
//
//        // Mark attendance for all members
//        List<Member> allMembers = memberRepository.findActiveMembers();
//
//        for (Member member : allMembers) {
//            MealAttendance attendance = new MealAttendance();
//            attendance.setMealId(mealId);
//            attendance.setMemberId(member.getMemberId());
//            attendance.setIsPresent(attendingMemberIds.contains(member.getMemberId()));
//            attendance.setMarkedBy(meal.getCreatedBy());
//
//            mealRepository.saveAttendance(attendance);
//        }
//
//        return meal;
//    }
//
//
//    /**
//     * Submit attendance for a specific date - creates/updates meals for each type
//     */
//    @Transactional
//    public void submitDailyAttendance(LocalDate date,
//                                      List<Integer> breakfastAttendees,
//                                      List<Integer> lunchAttendees,
//                                      List<Integer> dinnerAttendees,
//                                      Integer markedBy) {
//
//        // Process Breakfast
//        if (breakfastAttendees != null && !breakfastAttendees.isEmpty()) {
//            Meal existingBreakfast = mealRepository.findByDateAndType(date, "Breakfast");
//            if (existingBreakfast != null) {
//                updateMealAttendance(existingBreakfast.getMealId(), breakfastAttendees, markedBy);
//            } else {
//                saveMealWithAttendance(date, "Breakfast", "Daily breakfast", breakfastAttendees, markedBy);
//            }
//        }
//
//        // Process Lunch
//        if (lunchAttendees != null && !lunchAttendees.isEmpty()) {
//            Meal existingLunch = mealRepository.findByDateAndType(date, "Lunch");
//            if (existingLunch != null) {
//                updateMealAttendance(existingLunch.getMealId(), lunchAttendees, markedBy);
//            } else {
//                saveMealWithAttendance(date, "Lunch", "Daily lunch", lunchAttendees, markedBy);
//            }
//        }
//
//        // Process Dinner
//        if (dinnerAttendees != null && !dinnerAttendees.isEmpty()) {
//            Meal existingDinner = mealRepository.findByDateAndType(date, "Dinner");
//            if (existingDinner != null) {
//                updateMealAttendance(existingDinner.getMealId(), dinnerAttendees, markedBy);
//            } else {
//                saveMealWithAttendance(date, "Dinner", "Daily dinner", dinnerAttendees, markedBy);
//            }
//        }
//    }
//
//    @Transactional
//    public void updateMeal(Meal meal) {
//        mealRepository.update(meal);
//    }
//
//    @Transactional
//    public void deleteMeal(Integer id) {
//        // Delete related attendance and shares first
//        mealRepository.deleteAttendance(id);
//        mealRepository.deleteMealShares(id);
//        // Delete meal
//        mealRepository.delete(id);
//    }
//
//    public List<MealAttendance> getMealAttendance(Integer mealId) {
//        return mealRepository.findAttendanceByMealId(mealId);
//    }
//
//    public List<MealShare> getMealShares(Integer mealId) {
//        return mealRepository.findSharesByMealId(mealId);
//    }
//
//    /**
//     * Update attendance for a specific meal
//     */
//    @Transactional
//    public void updateMealAttendance(Integer mealId, List<Integer> attendingMemberIds, Integer markedBy) {
//        Meal meal = mealRepository.findById(mealId);
//
//        if (meal == null) {
//            throw new RuntimeException("Meal not found");
//        }
//
//        // Delete old attendance
//        mealRepository.deleteAttendance(mealId);
//
//        // Save new attendance for all active members
//        List<Member> allMembers = memberRepository.findActiveMembers();
//
//        for (Member member : allMembers) {
//            MealAttendance attendance = new MealAttendance();
//            attendance.setMealId(mealId);
//            attendance.setMemberId(member.getMemberId());
//            attendance.setIsPresent(attendingMemberIds.contains(member.getMemberId()));
//            attendance.setMarkedBy(markedBy);
//
//            mealRepository.saveAttendance(attendance);
//        }
//    }
//
//    /**
//     * Get attendance summary for a specific date
//     */
//    public Map<String, Object> getDailyAttendanceSummary(LocalDate date) {
//        Map<String, Object> summary = new HashMap<>();
//
//        List<Meal> mealsOnDate = mealRepository.findByDate(date);
//
//        for (Meal meal : mealsOnDate) {
//            List<MealAttendance> attendances = mealRepository.findAttendanceByMealId(meal.getMealId());
//            long presentCount = attendances.stream()
//                    .filter(MealAttendance::getIsPresent)
//                    .count();
//
//            String mealTypeKey = meal.getMealType().toLowerCase();
//            summary.put(mealTypeKey + "Count", presentCount);
//            summary.put(mealTypeKey + "MealId", meal.getMealId());
//        }
//
//        return summary;
//    }
//
//    /**
//     * Get monthly meal report for a member
//     */
//    public Map<String, Object> getMemberMonthlyReport(Integer memberId, int month, int year) {
//        List<Meal> monthlyMeals = mealRepository.findByMonth(month, year);
//
//        int totalMeals = 0;
//        int attendedMeals = 0;
//
//        for (Meal meal : monthlyMeals) {
//            List<MealAttendance> attendances = mealRepository.findAttendanceByMealId(meal.getMealId());
//
//            for (MealAttendance attendance : attendances) {
//                if (attendance.getMemberId().equals(memberId)) {
//                    totalMeals++;
//                    if (attendance.getIsPresent()) {
//                        attendedMeals++;
//                    }
//                    break;
//                }
//            }
//        }
//
//        Map<String, Object> report = new HashMap<>();
//        report.put("memberId", memberId);
//        report.put("month", month);
//        report.put("year", year);
//        report.put("totalMeals", totalMeals);
//        report.put("attendedMeals", attendedMeals);
//        report.put("missedMeals", totalMeals - attendedMeals);
//
//        return report;
//    }
//
//    /**
//     * Get all members' attendance for a specific date
//     */
//    public List<Map<String, Object>> getMembersAttendanceByDate(LocalDate date) {
//        List<Member> activeMembers = memberRepository.findActiveMembers();
//        List<Meal> mealsOnDate = mealRepository.findByDate(date);
//
//        List<Map<String, Object>> result = new ArrayList<>();
//
//        for (Member member : activeMembers) {
//            Map<String, Object> memberData = new HashMap<>();
//            memberData.put("memberId", member.getMemberId());
//            memberData.put("memberName", member.getName());
//
//            Map<String, Boolean> mealAttendance = new HashMap<>();
//
//            for (Meal meal : mealsOnDate) {
//                List<MealAttendance> attendances = mealRepository.findAttendanceByMealId(meal.getMealId());
//
//                for (MealAttendance attendance : attendances) {
//                    if (attendance.getMemberId().equals(member.getMemberId())) {
//                        mealAttendance.put(meal.getMealType().toLowerCase(), attendance.getIsPresent());
//                        break;
//                    }
//                }
//            }
//
//            memberData.put("attendance", mealAttendance);
//            result.add(memberData);
//        }
//
//        return result;
//    }
//}
@Service
public class MealService {

    private final MealRepository mealRepository;
    private final MemberRepository memberRepository;
    private final MealAttendanceRepository attendanceRepository;

    public MealService(MealRepository mealRepository,
                       MemberRepository memberRepository,
                       MealAttendanceRepository attendanceRepository) {
        this.mealRepository = mealRepository;
        this.memberRepository = memberRepository;
        this.attendanceRepository = attendanceRepository;
    }

    // Get all meals
    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    // Get meals by month and year
    public List<Meal> getMealsByMonthAndYear(int month, int year) {
        return mealRepository.findByMonthAndYear(month, year);
    }

    // Get meals by date
    public List<Meal> getMealsByDate(LocalDate date) {
        return mealRepository.findByDate(date);
    }

    // Get meal by ID
    public Meal getMealById(Integer id) {
        return mealRepository.findById(id);
    }

    public Meal getMealByDateAndType(LocalDate date, String mealType) {
        return mealRepository.findByDateAndType(date, mealType);
    }


    /**
     * Create meal with attendance
     * একটা meal create করে সাথে সাথে সব members এর attendance mark করে
     */
    @Transactional
    public Meal createMealWithAttendance(Meal meal, List<Integer> presentMemberIds) {
        // Check if meal already exists
        Meal existing = mealRepository.findByDateAndType(meal.getMealDate(), meal.getMealType());
        if (existing != null) {
            throw new RuntimeException("এই তারিখে " + meal.getMealType() + " ইতিমধ্যে আছে!");
        }

        // Set default created by if not set
        if (meal.getCreatedBy() == null) {
            meal.setCreatedBy(1); // Default admin ID
        }

        // Save meal
        Integer mealId = mealRepository.save(meal);
        meal.setMealId(mealId);

        // Create attendance for all active members
        List<Member> allMembers = memberRepository.findActiveMembers();

        for (Member member : allMembers) {
            MealAttendance attendance = new MealAttendance();
            attendance.setMealId(mealId);
            attendance.setMemberId(member.getMemberId());
            attendance.setIsPresent(presentMemberIds.contains(member.getMemberId()));
            attendance.setMarkedBy(meal.getCreatedBy());

            attendanceRepository.save(attendance);
        }

        // Update total attendances
        mealRepository.updateTotalAttendances(mealId);

        return meal;
    }

    /**
     * Update meal attendance
     * একটা meal এর attendance update করে
     */
    @Transactional
    public void updateMealAttendance(Integer mealId, List<Integer> presentMemberIds, Integer markedBy) {
        Meal meal = mealRepository.findById(mealId);
        if (meal == null) {
            throw new RuntimeException("Meal খুঁজে পাওয়া যায়নি!");
        }

        // Delete old attendance
        attendanceRepository.deleteByMealId(mealId);

        // Create new attendance for all active members
        List<Member> allMembers = memberRepository.findActiveMembers();

        for (Member member : allMembers) {
            MealAttendance attendance = new MealAttendance();
            attendance.setMealId(mealId);
            attendance.setMemberId(member.getMemberId());
            attendance.setIsPresent(presentMemberIds.contains(member.getMemberId()));
            attendance.setMarkedBy(markedBy);

            attendanceRepository.save(attendance);
        }

        // Update total attendances
        mealRepository.updateTotalAttendances(mealId);
    }

    /**
     * Submit daily attendance for all three meals
     * একদিনের তিনটা খাবারের attendance একসাথে submit করে
     */
    @Transactional
    public void submitDailyAttendance(LocalDate date,
                                      List<Integer> breakfastMembers,
                                      List<Integer> lunchMembers,
                                      List<Integer> dinnerMembers,
                                      Integer markedBy) {

        // Breakfast
        if (breakfastMembers != null && !breakfastMembers.isEmpty()) {
            Meal breakfast = mealRepository.findByDateAndType(date, "Breakfast");
            if (breakfast != null) {
                updateMealAttendance(breakfast.getMealId(), breakfastMembers, markedBy);
            } else {
                Meal newBreakfast = new Meal();
                newBreakfast.setMealDate(date);
                newBreakfast.setMealType("Breakfast");
                newBreakfast.setMonth(date.getMonthValue());
                newBreakfast.setYear(date.getYear());
                newBreakfast.setCreatedBy(markedBy);
                createMealWithAttendance(newBreakfast, breakfastMembers);
            }
        }

        // Lunch
        if (lunchMembers != null && !lunchMembers.isEmpty()) {
            Meal lunch = mealRepository.findByDateAndType(date, "Lunch");
            if (lunch != null) {
                updateMealAttendance(lunch.getMealId(), lunchMembers, markedBy);
            } else {
                Meal newLunch = new Meal();
                newLunch.setMealDate(date);
                newLunch.setMealType("Lunch");
                newLunch.setMonth(date.getMonthValue());
                newLunch.setYear(date.getYear());
                newLunch.setCreatedBy(markedBy);
                createMealWithAttendance(newLunch, lunchMembers);
            }
        }

        // Dinner
        if (dinnerMembers != null && !dinnerMembers.isEmpty()) {
            Meal dinner = mealRepository.findByDateAndType(date, "Dinner");
            if (dinner != null) {
                updateMealAttendance(dinner.getMealId(), dinnerMembers, markedBy);
            } else {
                Meal newDinner = new Meal();
                newDinner.setMealDate(date);
                newDinner.setMealType("Dinner");
                newDinner.setMonth(date.getMonthValue());
                newDinner.setYear(date.getYear());
                newDinner.setCreatedBy(markedBy);
                createMealWithAttendance(newDinner, dinnerMembers);
            }
        }
    }

    /**
     * Get meal attendance
     */
    public List<MealAttendance> getMealAttendance(Integer mealId) {
        return attendanceRepository.findByMealId(mealId);
    }

    /**
     * Delete meal
     */
    @Transactional
    public void deleteMeal(Integer mealId) {
        mealRepository.delete(mealId);
    }

    /**
     * Get attendance summary for a date
     */
    public Map<String, Object> getDailyAttendanceSummary(LocalDate date) {
        Map<String, Object> summary = new HashMap<>();
        List<Meal> meals = mealRepository.findByDate(date);

        for (Meal meal : meals) {
            String key = meal.getMealType().toLowerCase();
            summary.put(key + "Count", meal.getTotalAttendances());
            summary.put(key + "MealId", meal.getMealId());
        }

        return summary;
    }

    /**
     * Get member's monthly report
     */
    public Map<String, Object> getMemberMonthlyReport(Integer memberId, int month, int year) {
        // Implementation here
        return new HashMap<>();
    }
}
