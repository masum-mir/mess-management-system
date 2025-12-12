package com.mms.controller;

import com.mms.model.Meal;
import com.mms.service.MealService;
import com.mms.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class MealController {

    private final MealService mealService;
    private final MemberService memberService;

    public MealController(MealService mealService, MemberService memberService) {
        this.mealService = mealService;
        this.memberService = memberService;
    }

    @GetMapping
    public String listMeals(@RequestParam(required = false) Integer month,
                            @RequestParam(required = false) Integer year,
                            Model model) {
        LocalDate now = LocalDate.now();
        int selectedMonth = (month != null) ? month : now.getMonthValue();
        int selectedYear = (year != null) ? year : now.getYear();

        List<Meal> meals = mealService.getMealsByMonth(selectedMonth, selectedYear);

        model.addAttribute("meals", meals);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("pageTitle", "Meals");

        return "meal/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Meal meal = new Meal();
        meal.setMealDate(LocalDate.now());

        model.addAttribute("meal", meal);
        model.addAttribute("members", memberService.getActiveMembers());
        model.addAttribute("pageTitle", "Add Meal");

        return "meal/add";
    }

    @PostMapping("/add")
    public String addMeal(@ModelAttribute Meal meal,
                          @RequestParam(value = "attendingMembers", required = false) List<Integer> attendingMembers,
                          RedirectAttributes redirectAttributes) {
        try {
            if (attendingMembers == null) {
                attendingMembers = new ArrayList<>();
            }

            meal.setMonth(meal.getMealDate().getMonthValue());
            meal.setYear(meal.getMealDate().getYear());

            mealService.saveMealWithAttendance(meal, attendingMembers);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Meal added successfully!");
            return "redirect:/meals";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error adding meal: " + e.getMessage());
            return "redirect:/meals/add";
        }
    }

    @GetMapping("/attendance/{id}")
    public String showAttendanceForm(@PathVariable Integer id, Model model) {
        Meal meal = mealService.getMealById(id);
        if (meal == null) {
            return "redirect:/meals";
        }

        model.addAttribute("meal", meal);
        model.addAttribute("members", memberService.getActiveMembers());
        model.addAttribute("attendance", mealService.getMealAttendance(id));
        model.addAttribute("pageTitle", "Meal Attendance");

        return "meal/attendance";
    }

    @PostMapping("/attendance/{id}")
    public String updateAttendance(@PathVariable Integer id,
                                   @RequestParam(value = "attendingMembers", required = false) List<Integer> attendingMembers,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (attendingMembers == null) {
                attendingMembers = new ArrayList<>();
            }

            mealService.updateMealAttendance(id, attendingMembers);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Attendance updated successfully!");
            return "redirect:/meals";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error updating attendance: " + e.getMessage());
            return "redirect:/meals/attendance/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteMeal(@PathVariable Integer id,
                             RedirectAttributes redirectAttributes) {
        try {
            mealService.deleteMeal(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Meal deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting meal: " + e.getMessage());
        }
        return "redirect:/meals";
    }
}