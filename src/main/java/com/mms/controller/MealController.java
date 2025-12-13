//package com.mms.controller;
//
//import com.mms.model.Meal;
//import com.mms.service.MealService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/meals")
//public class MealController {
//
////    private final MealService mealService;
////    private final MemberService memberService;
////
////    public MealController(MealService mealService, MemberService memberService) {
////        this.mealService = mealService;
////        this.memberService = memberService;
////    }
////
////    @GetMapping
////    public String listMeals(@RequestParam(required = false) Integer month,
////                            @RequestParam(required = false) Integer year,
////                            Model model) {
////        LocalDate now = LocalDate.now();
////        int selectedMonth = (month != null) ? month : now.getMonthValue();
////        int selectedYear = (year != null) ? year : now.getYear();
////
////        List<Meal> meals = mealService.getMealsByMonth(selectedMonth, selectedYear);
////
////        model.addAttribute("meals", meals);
////        model.addAttribute("selectedMonth", selectedMonth);
////        model.addAttribute("selectedYear", selectedYear);
////        model.addAttribute("pageTitle", "Meals");
////
////        return "meal/list";
////    }
////
////    @GetMapping("/add")
////    public String showAddForm(Model model) {
////        Meal meal = new Meal();
////        meal.setMealDate(LocalDate.now());
////
////        model.addAttribute("meal", meal);
////        model.addAttribute("members", memberService.getActiveMembers());
////        model.addAttribute("pageTitle", "Add Meal");
////
////        return "meal/add";
////    }
////
////    @PostMapping("/add")
////    public String addMeal(@ModelAttribute Meal meal,
////                          @RequestParam(value = "attendingMembers", required = false) List<Integer> attendingMembers,
////                          RedirectAttributes redirectAttributes) {
////        try {
////            if (attendingMembers == null) {
////                attendingMembers = new ArrayList<>();
////            }
////
////            meal.setMonth(meal.getMealDate().getMonthValue());
////            meal.setYear(meal.getMealDate().getYear());
////
////            mealService.saveMealWithAttendance(meal, attendingMembers);
////            redirectAttributes.addFlashAttribute("successMessage",
////                    "Meal added successfully!");
////            return "redirect:/meals";
////        } catch (Exception e) {
////            redirectAttributes.addFlashAttribute("errorMessage",
////                    "Error adding meal: " + e.getMessage());
////            return "redirect:/meals/add";
////        }
////    }
////
////    @GetMapping("/attendance/{id}")
////    public String showAttendanceForm(@PathVariable Integer id, Model model) {
////        Meal meal = mealService.getMealById(id);
////        if (meal == null) {
////            return "redirect:/meals";
////        }
////
////        model.addAttribute("meal", meal);
////        model.addAttribute("members", memberService.getActiveMembers());
////        model.addAttribute("attendance", mealService.getMealAttendance(id));
////        model.addAttribute("pageTitle", "Meal Attendance");
////
////        return "meal/attendance";
////    }
////
////    @PostMapping("/attendance/{id}")
////    public String updateAttendance(@PathVariable Integer id,
////                                   @RequestParam(value = "attendingMembers", required = false) List<Integer> attendingMembers,
////                                   RedirectAttributes redirectAttributes) {
////        try {
////            if (attendingMembers == null) {
////                attendingMembers = new ArrayList<>();
////            }
////
//////            mealService.updateMealAttendance(id, attendingMembers);
////            redirectAttributes.addFlashAttribute("successMessage",
////                    "Attendance updated successfully!");
////            return "redirect:/meals";
////        } catch (Exception e) {
////            redirectAttributes.addFlashAttribute("errorMessage",
////                    "Error updating attendance: " + e.getMessage());
////            return "redirect:/meals/attendance/" + id;
////        }
////    }
////
////    @GetMapping("/delete/{id}")
////    public String deleteMeal(@PathVariable Integer id,
////                             RedirectAttributes redirectAttributes) {
////        try {
////            mealService.deleteMeal(id);
////            redirectAttributes.addFlashAttribute("successMessage",
////                    "Meal deleted successfully!");
////        } catch (Exception e) {
////            redirectAttributes.addFlashAttribute("errorMessage",
////                    "Error deleting meal: " + e.getMessage());
////        }
////        return "redirect:/meals";
////    }
////}
//
//        @Autowired
//        MealService mealService;
//
//    public void DailyAttendanceController(MealService mealService) {
//        this.mealService = mealService;
//    }
//
//    /**
//     * প্রধান পেজ - সরল একটি পেজ যেখানে সব কাজ করা যাবে
//     */
////    @GetMapping
////    public String showDailyAttendancePage(Model model) {
////        // আজকের তারিখ
////        LocalDate today = LocalDate.now();
////
////        // সব active members
////        model.addAttribute("members", mealService.getActiveMembers());
////
////        // আজকের আগের attendance থাকলে দেখাবে
////        model.addAttribute("todayAttendance",
////                mealService.getMembersAttendanceByDate(today));
////
////        return "meal/list";
////    }
//
//        @GetMapping
//    public String listMeals(@RequestParam(required = false) Integer month,
//                            @RequestParam(required = false) Integer year,
//                            Model model) {
//        LocalDate now = LocalDate.now();
//        int selectedMonth = (month != null) ? month : now.getMonthValue();
//        int selectedYear = (year != null) ? year : now.getYear();
//
//        List<Meal> meals = mealService.getMealsByMonth(selectedMonth, selectedYear);
//
//        model.addAttribute("meals", meals);
//        model.addAttribute("selectedMonth", selectedMonth);
//        model.addAttribute("selectedYear", selectedYear);
//        model.addAttribute("pageTitle", "Meals");
//
//        return "meal/list";
//    }
//
//    /**
//     * REST API - দৈনিক attendance submit করার জন্য
//     * Frontend থেকে এই endpoint এ POST করবে
//     */
//    @PostMapping("/api/daily-attendance")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> submitDailyAttendance(
//            @RequestBody DailyAttendanceRequest request) {
//
//        try {
//            // তিনটা খাবারের জন্য একসাথে save করবে
//            // Backend এ আলাদা Meal এবং MealAttendance table এ সংরক্ষণ হবে
//            mealService.submitDailyAttendance(
//                    request.getDate(),
//                    request.getBreakfast(),
//                    request.getLunch(),
//                    request.getDinner(),
//                    1 // markedBy - session থেকে নিতে হবে
//            );
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("message", "উপস্থিতি সফলভাবে সংরক্ষণ হয়েছে");
//            response.put("date", request.getDate());
//            response.put("breakfastCount", request.getBreakfast().size());
//            response.put("lunchCount", request.getLunch().size());
//            response.put("dinnerCount", request.getDinner().size());
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Error: " + e.getMessage());
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//    }
//
//    /**
//     * REST API - নির্দিষ্ট তারিখের attendance দেখার জন্য
//     */
//    @GetMapping("/api/attendance/{date}")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> getAttendanceByDate(
//            @PathVariable String date) {
//
//        try {
//            LocalDate localDate = LocalDate.parse(date);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("date", date);
//            response.put("attendance",
//                    mealService.getMembersAttendanceByDate(localDate));
//            response.put("summary",
//                    mealService.getDailyAttendanceSummary(localDate));
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "Error: " + e.getMessage());
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//    }
//
//    /**
//     * DTO for daily attendance request
//     */
//    public static class DailyAttendanceRequest {
//        private LocalDate date;
//        private List<Integer> breakfast;
//        private List<Integer> lunch;
//        private List<Integer> dinner;
//
//        public LocalDate getDate() {
//            return date;
//        }
//
//        public void setDate(LocalDate date) {
//            this.date = date;
//        }
//
//        public List<Integer> getBreakfast() {
//            return breakfast;
//        }
//
//        public void setBreakfast(List<Integer> breakfast) {
//            this.breakfast = breakfast;
//        }
//
//        public List<Integer> getLunch() {
//            return lunch;
//        }
//
//        public void setLunch(List<Integer> lunch) {
//            this.lunch = lunch;
//        }
//
//        public List<Integer> getDinner() {
//            return dinner;
//        }
//
//        public void setDinner(List<Integer> dinner) {
//            this.dinner = dinner;
//        }
//    }
//}

package com.mms.controller;

import com.mms.model.Meal;
import com.mms.model.MealAttendance;
import com.mms.model.Member;
import com.mms.service.MealService;
import com.mms.service.MemberService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//
//@Controller
//@RequestMapping("/meals")
//public class MealController {
//
//    private final MealService mealService;
//    private final MemberService memberService;
//
//    public MealController(MealService mealService, MemberService memberService) {
//        this.mealService = mealService;
//        this.memberService = memberService;
//    }
//
//    /**
//     * Meal list page - সব meal দেখানো
//     * URL: /meals or /meals?month=12&year=2024
//     */
//    @GetMapping
//    public String listMeals(@RequestParam(required = false) Integer month,
//                            @RequestParam(required = false) Integer year,
//                            Model model) {
//        LocalDate now = LocalDate.now();
//        int selectedMonth = (month != null) ? month : now.getMonthValue();
//        int selectedYear = (year != null) ? year : now.getYear();
//
//        List<Meal> meals = mealService.getMealsByMonthAndYear(selectedMonth, selectedYear);
//
//        model.addAttribute("meals", meals);
//        model.addAttribute("selectedMonth", selectedMonth);
//        model.addAttribute("selectedYear", selectedYear);
//        model.addAttribute("pageTitle", "খাবার তালিকা");
//
//        return "meal/list";
//    }
//
//    /**
//     * Add meal page - নতুন meal যোগ করার form
//     * URL: GET /meals/add
//     */
//    @GetMapping("/add")
//    public String showAddForm(Model model) {
//        Meal meal = new Meal();
//        meal.setMealDate(LocalDate.now());
//
//        model.addAttribute("meal", meal);
//        model.addAttribute("members", memberService.getActiveMembers());
//        model.addAttribute("pageTitle", "নতুন খাবার যোগ করুন");
//
//        return "meal/add";
//    }
//
//    /**
//     * Save new meal - নতুন meal save করা
//     * URL: POST /meals/add
//     */
//    @PostMapping("/add")
//    public String addMeal(@ModelAttribute Meal meal,
//                          @RequestParam(value = "attendingMembers", required = false) List<Integer> attendingMembers,
//                          RedirectAttributes redirectAttributes) {
//        try {
//            if (attendingMembers == null) {
//                attendingMembers = new ArrayList<>();
//            }
//
//            meal.setMonth(meal.getMealDate().getMonthValue());
//            meal.setYear(meal.getMealDate().getYear());
//
//            mealService.createMealWithAttendance(meal, attendingMembers);
//
//            redirectAttributes.addFlashAttribute("successMessage",
//                    "✅ খাবার সফলভাবে যোগ করা হয়েছে!");
//            return "redirect:/meals";
//
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage",
//                    "❌ Error: " + e.getMessage());
//            return "redirect:/meals/add";
//        }
//    }
//
//    /**
//     * Show attendance form - attendance দেখা/edit করা
//     * URL: GET /meals/attendance/1
//     */
////    @GetMapping("/attendance/{id}")
////    public String showAttendanceForm(@PathVariable Integer id, Model model) {
////        Meal meal = mealService.getMealById(id);
////        if (meal == null) {
////            return "redirect:/meals";
////        }
////
////        model.addAttribute("meal", meal);
////        model.addAttribute("members", memberService.getActiveMembers());
////        model.addAttribute("attendance", mealService.getMealAttendance(id));
////        model.addAttribute("pageTitle", "উপস্থিতি পরিচালনা");
////
////        return "meal/attendance";
////    }
//
//    @GetMapping("/attendance/{id}")
//    public String showAttendanceForm(@PathVariable Integer id, Model model) {
//        Meal meal = mealService.getMealById(id);
//        if (meal == null) return "redirect:/meals";
//
//        List<Member> members = memberService.getActiveMembers();
//        List<MealAttendance> attendanceList = mealService.getMealAttendance(id);
//
//        Map<Integer, Boolean> attendanceMap = new HashMap<>();
//        for (MealAttendance att : attendanceList) {
//            attendanceMap.put(att.getMemberId(), Boolean.TRUE.equals(att.getIsPresent()));
//        }
//
//        model.addAttribute("meal", meal);
//        model.addAttribute("members", members);
//        model.addAttribute("attendanceMap", attendanceMap);
//        model.addAttribute("pageTitle", "উপস্থিতি পরিচালনা");
//
//        return "meal/attendance";
//    }
//
//
//    /**
//     * Update attendance - attendance update করা
//     * URL: POST /meals/attendance/1
//     */
//    @PostMapping("/attendance/{id}")
//    public String updateAttendance(@PathVariable Integer id,
//                                   @RequestParam(value = "attendingMembers", required = false) List<Integer> attendingMembers,
//                                   RedirectAttributes redirectAttributes) {
//        try {
//            if (attendingMembers == null) {
//                attendingMembers = new ArrayList<>();
//            }
//
//            mealService.updateMealAttendance(id, attendingMembers, 1); // 1 = default admin
//
//            redirectAttributes.addFlashAttribute("successMessage",
//                    "✅ উপস্থিতি সফলভাবে আপডেট করা হয়েছে!");
//            return "redirect:/meals";
//
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage",
//                    "❌ Error: " + e.getMessage());
//            return "redirect:/meals/attendance/" + id;
//        }
//    }
//
//    /**
//     * Delete meal - meal মুছে ফেলা
//     * URL: GET /meals/delete/1
//     */
//    @GetMapping("/delete/{id}")
//    public String deleteMeal(@PathVariable Integer id,
//                             RedirectAttributes redirectAttributes) {
//        try {
//            mealService.deleteMeal(id);
//            redirectAttributes.addFlashAttribute("successMessage",
//                    "✅ খাবার সফলভাবে মুছে ফেলা হয়েছে!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage",
//                    "❌ Error: " + e.getMessage());
//        }
//        return "redirect:/meals";
//    }
//
//    /**
//     * Daily attendance page - একদিনের সব meal এর attendance
//     * URL: GET /meals/daily
//     */
//    @GetMapping("/daily")
//    public String showDailyAttendancePage(Model model) {
//        LocalDate today = LocalDate.now();
//
//        model.addAttribute("members", memberService.getActiveMembers());
//        model.addAttribute("today", today);
//        model.addAttribute("todaysMeals", mealService.getMealsByDate(today));
//        model.addAttribute("pageTitle", "দৈনিক উপস্থিতি");
//
//        return "meal/daily";
//    }
//}
@Controller
@RequestMapping("/meals")
public class MealController {

    private final MealService mealService;
    private final MemberService memberService;

    public MealController(MealService mealService, MemberService memberService) {
        this.mealService = mealService;
        this.memberService = memberService;
    }

    /**
     * Meal list page - সব meal দেখানো (with modals)
     * URL: /meals or /meals?month=12&year=2024
     */
    @GetMapping
    public String listMeals(@RequestParam(required = false) Integer month,
                            @RequestParam(required = false) Integer year,
                            Model model) {
        LocalDate now = LocalDate.now();
        int selectedMonth = (month != null) ? month : now.getMonthValue();
        int selectedYear = (year != null) ? year : now.getYear();

        List<Meal> meals = mealService.getMealsByMonthAndYear(selectedMonth, selectedYear);
        List<Member> members = memberService.getActiveMembers();

        model.addAttribute("meals", meals);
        model.addAttribute("members", members);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("pageTitle", "খাবার তালিকা");

        return "meal/list";
    }

    /**
     * Save new meal - নতুন meal save করা (AJAX/Form submission)
     * URL: POST /meals/add
     */
    @PostMapping("/add")
    public String addMeal(@RequestParam("mealDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate mealDate,
                          @RequestParam("mealType") String mealType,
                          @RequestParam(value = "attendingMembers", required = false) List<Integer> attendingMembers,
                          RedirectAttributes redirectAttributes) {
        try {
            if (attendingMembers == null) {
                attendingMembers = new ArrayList<>();
            }

            // Check if meal already exists
            Meal existingMeal = mealService.getMealByDateAndType(mealDate, mealType);
            if (existingMeal != null) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "❌ এই তারিখ এবং সময়ের জন্য খাবার ইতিমধ্যে বিদ্যমান!");
                return "redirect:/meals";
            }

            Meal meal = new Meal();
            meal.setMealDate(mealDate);
            meal.setMealType(mealType);
            meal.setMonth(mealDate.getMonthValue());
            meal.setYear(mealDate.getYear());
            meal.setCreatedBy(1); // Default admin user

            mealService.createMealWithAttendance(meal, attendingMembers);

            redirectAttributes.addFlashAttribute("successMessage",
                    "✅ খাবার সফলভাবে যোগ করা হয়েছে!");
            return "redirect:/meals";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "❌ Error: " + e.getMessage());
            return "redirect:/meals";
        }
    }

    /**
     * Get attendance data for a meal (AJAX endpoint)
     * URL: GET /meals/attendance/{id}/data
     */
    @GetMapping("/attendance/{id}/data")
    @ResponseBody
    public Map<String, Object> getAttendanceData(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Meal meal = mealService.getMealById(id);
            if (meal == null) {
                response.put("error", "Meal not found");
                return response;
            }

            List<Member> members = memberService.getActiveMembers();
            List<MealAttendance> attendanceList = mealService.getMealAttendance(id);

            // Create attendance map
            Map<Integer, Boolean> attendanceMap = new HashMap<>();
            for (MealAttendance att : attendanceList) {
                attendanceMap.put(att.getMemberId(), Boolean.TRUE.equals(att.getIsPresent()));
            }

            // Format meal date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            String formattedDate = meal.getMealDate().format(formatter);

            // Create meal type badge HTML
            String mealTypeBadge = createMealTypeBadge(meal.getMealType());

            // Prepare member data
            List<Map<String, Object>> memberData = new ArrayList<>();
            for (Member member : members) {
                Map<String, Object> memberInfo = new HashMap<>();
                memberInfo.put("memberId", member.getMemberId());
                memberInfo.put("name", member.getName());
                memberData.add(memberInfo);
            }

            response.put("mealId", meal.getMealId());
            response.put("mealDate", formattedDate);
            response.put("mealType", meal.getMealType());
            response.put("mealTypeBadge", mealTypeBadge);
            response.put("members", memberData);
            response.put("attendance", attendanceMap);

        } catch (Exception e) {
            response.put("error", e.getMessage());
        }

        return response;
    }

    /**
     * Update attendance - attendance update করা
     * URL: POST /meals/attendance/{id}
     */
    @PostMapping("/attendance/{id}")
    public String updateAttendance(@PathVariable Integer id,
                                   @RequestParam(value = "attendingMembers", required = false) List<Integer> attendingMembers,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (attendingMembers == null) {
                attendingMembers = new ArrayList<>();
            }

            mealService.updateMealAttendance(id, attendingMembers, 1); // 1 = default admin

            redirectAttributes.addFlashAttribute("successMessage",
                    "✅ উপস্থিতি সফলভাবে আপডেট করা হয়েছে!");
            return "redirect:/meals";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "❌ Error: " + e.getMessage());
            return "redirect:/meals";
        }
    }

    /**
     * Delete meal - meal মুছে ফেলা
     * URL: GET /meals/delete/{id}
     */
    @GetMapping("/delete/{id}")
    public String deleteMeal(@PathVariable Integer id,
                             RedirectAttributes redirectAttributes) {
        try {
            mealService.deleteMeal(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "✅ খাবার সফলভাবে মুছে ফেলা হয়েছে!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "❌ Error: " + e.getMessage());
        }
        return "redirect:/meals";
    }

    /**
     * Helper method to create meal type badge HTML
     */
    private String createMealTypeBadge(String mealType) {
        String badgeClass = "";
        String icon = "";
        String text = "";

        switch (mealType) {
            case "Breakfast":
                badgeClass = "bg-warning text-dark";
                icon = "bi-sunrise";
                text = "নাস্তা";
                break;
            case "Lunch":
                badgeClass = "bg-success";
                icon = "bi-sun";
                text = "দুপুরের খাবার";
                break;
            case "Dinner":
                badgeClass = "bg-primary";
                icon = "bi-moon-stars";
                text = "রাতের খাবার";
                break;
            default:
                badgeClass = "bg-secondary";
                icon = "bi-question";
                text = mealType;
        }

        return String.format(
                "<span class='badge fs-5 %s'><i class='bi %s'></i> %s</span>",
                badgeClass, icon, text
        );
    }
}