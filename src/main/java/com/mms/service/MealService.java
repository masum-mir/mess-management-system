package com.mms.service;

import com.mms.model.Meal;
import com.mms.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MealService {

    @Autowired
    private MealRepository mealRepository;

    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    public List<Meal> getMealsByMonthAndYear(int month, int year) {
        return mealRepository.findByMonthAndYear(month, year);
    }

    public List<Meal> getMealsByDate(LocalDate date) {
        return mealRepository.findByDate(date);
    }

    public Meal getMealById(Integer id) {
        return mealRepository.findById(id);
    }

    @Transactional
    public Meal saveMeal(Meal meal) {
        Integer mealId = mealRepository.save(meal);
        meal.setMealId(mealId);
        return meal;
    }

    @Transactional
    public void updateMeal(Meal meal) {
        // Update meal record
        mealRepository.update(meal);
    }

    /**
     * Update only attendance count
     */
    @Transactional
    public void updateTotalAttendances(Integer mealId, Integer totalAttendances) {
        mealRepository.updateTotalAttendances(mealId, totalAttendances);
    }

    @Transactional
    public void deleteMeal(Integer id) {
        mealRepository.delete(id);
    }

    /**
     * Get total meals for a member in a specific month
     */
    public int getMemberTotalMeals(Integer memberId, int month, int year) {
        List<Meal> meals = mealRepository.findByMonthAndYear(month, year);
        return (int) meals.stream()
                .filter(meal -> meal.getMemberId().equals(memberId))
                .count();
    }
}