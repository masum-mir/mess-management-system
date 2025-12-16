package com.mms.service;

import com.mms.model.Expense;
import com.mms.model.ExpenseShare;
import com.mms.model.Member;
import com.mms.repository.ExpenseRepository;
import com.mms.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    MemberRepository memberRepository;

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public List<Expense> getExpensesByMonth(int month, int year) {
        return expenseRepository.findByMonth(month, year);
    }

    public Expense getExpenseById(Integer id) {
        return expenseRepository.findById(id);
    }

    @Transactional
    public Expense saveExpense(Expense expense) {
        // Save expense
        Integer expenseId = expenseRepository.save(expense);
        expense.setExpenseId(expenseId);

        return expense;
    }

    @Transactional
    public void updateExpense(Expense expense) {
        // Delete old shares
//        expenseRepository.deleteExpenseShares(expense.getExpenseId());

        // Update expense
        expenseRepository.update(expense);

    }

    @Transactional
    public void deleteExpense(Integer id) {
        expenseRepository.delete(id);
    }

    public List<ExpenseShare> getExpenseShares(Integer expenseId) {
        return expenseRepository.findSharesByExpenseId(expenseId);
    }

    public String shareExpansebyMealindovidual(){
//         return expenseRepository.shareExpansebyMealindovidual();
        return null;
    }

    public String shareExpansebyOtherCost(){
//        return expenseRepository.shareExpansebyMealindovidual();
        return null;
    }

}