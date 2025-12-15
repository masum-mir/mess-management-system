package com.mms.controller;

import com.mms.model.Expense;
import com.mms.service.CategoryService;
import com.mms.service.ExpenseService;
import com.mms.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final MemberService memberService;
    private final CategoryService categoryService;

    public ExpenseController(ExpenseService expenseService,
                             MemberService memberService,
                             CategoryService categoryService) {
        this.expenseService = expenseService;
        this.memberService = memberService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listExpenses(@RequestParam(required = false) Integer month,
                               @RequestParam(required = false) Integer year,
                               Model model) {
        LocalDate now = LocalDate.now();
        int selectedMonth = (month != null) ? month : now.getMonthValue();
        int selectedYear = (year != null) ? year : now.getYear();

        List<Expense> expenses = expenseService.getExpensesByMonth(selectedMonth, selectedYear);
// Add a dummy expense object for the modal form (to avoid null errors)
        Expense newExpense = new Expense();
        newExpense.setExpenseDate(LocalDate.now());

        model.addAttribute("expense", newExpense);  // 👈 This is the fix!
        model.addAttribute("expenses", expenses);
        model.addAttribute("members", memberService.getActiveMembers());
        model.addAttribute("categories", categoryService.getNonMealCategories());
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("pageTitle", "Expenses");

        return "expense/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Expense expense = new Expense();
        expense.setExpenseDate(LocalDate.now());

        model.addAttribute("expense", expense);
        model.addAttribute("members", memberService.getActiveMembers());
        model.addAttribute("categories", categoryService.getNonMealCategories());
        model.addAttribute("pageTitle", "Add Expense");

        return "expense/add";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Map<String, Object> getExpenseData(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Expense expense = expenseService.getExpenseById(id);

            if (expense == null) {
                response.put("error", "Expense not found");
                return response;
            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            response.put("expenseId", expense.getExpenseId());
            response.put("expenseDate", expense.getExpenseDate().format(dateFormatter));
            response.put("categoryId", expense.getCategoryId());
            response.put("amount", expense.getAmount());
            response.put("description", expense.getDescription());
            response.put("recordedBy", expense.getRecordedBy());

        } catch (Exception e) {
            response.put("error", e.getMessage());
        }

        return response;
    }

    @PostMapping("/add")
    public String addExpense(@ModelAttribute Expense expense,
                             RedirectAttributes redirectAttributes) {
        try {

            expense.setMonth(expense.getExpenseDate().getMonthValue());
            expense.setYear(expense.getExpenseDate().getYear());

            expenseService.saveExpense(expense);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Expense added successfully!");
            return "redirect:/expenses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error adding expense: " + e.getMessage());
            return "redirect:/expenses/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Expense expense = expenseService.getExpenseById(id);
        if (expense == null) {
            return "redirect:/expenses";
        }

        model.addAttribute("expense", expense);
        model.addAttribute("members", memberService.getActiveMembers());
        model.addAttribute("categories", categoryService.getNonMealCategories());
        model.addAttribute("pageTitle", "Edit Expense");

        return "expense/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateExpense(@PathVariable Integer id,
                                @ModelAttribute Expense expense,
                                RedirectAttributes redirectAttributes) {
        try {
            expense.setExpenseId(id);
            expense.setMonth(expense.getExpenseDate().getMonthValue());
            expense.setYear(expense.getExpenseDate().getYear());

            expenseService.updateExpense(expense);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Expense updated successfully!");
            return "redirect:/expenses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error updating expense: " + e.getMessage());
            return "redirect:/expenses/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Integer id,
                                RedirectAttributes redirectAttributes) {
        try {
            expenseService.deleteExpense(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Expense deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting expense: " + e.getMessage());
        }
        return "redirect:/expenses";
    }

    @GetMapping("/per-person-mill-cost")
    public String shareExpansebyMealindovidual(){
        return expenseService.shareExpansebyMealindovidual();
    }

    @GetMapping("/per-member-other-cost")
    public String shareExpansebyOtherCost(){
        return expenseService.shareExpansebyOtherCost();
    }

}