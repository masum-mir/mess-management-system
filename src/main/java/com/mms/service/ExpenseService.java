package com.mms.service;

import com.mms.model.Expense;
import com.mms.model.ExpenseShare;
import com.mms.model.Member;
import com.mms.repository.ExpenseRepository;
import com.mms.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final MemberRepository memberRepository;

    public ExpenseService(ExpenseRepository expenseRepository, MemberRepository memberRepository) {
        this.expenseRepository = expenseRepository;
        this.memberRepository = memberRepository;
    }

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

        // If shared expense, distribute among active members
        if (expense.getIsShared() && !expense.getExpenseType().equals("meal")) {
            distributeExpenseShare(expenseId, expense.getAmount());
        }

        return expense;
    }

    @Transactional
    public void updateExpense(Expense expense) {
        // Delete old shares
        expenseRepository.deleteExpenseShares(expense.getExpenseId());

        // Update expense
        expenseRepository.update(expense);

        // Redistribute shares if shared
        if (expense.getIsShared() && !expense.getExpenseType().equals("meal")) {
            distributeExpenseShare(expense.getExpenseId(), expense.getAmount());
        }
    }

    @Transactional
    public void deleteExpense(Integer id) {
        expenseRepository.delete(id);
    }

    /**
     * Distribute expense equally among all active members
     */
    private void distributeExpenseShare(Integer expenseId, BigDecimal amount) {
        List<Member> activeMembers = memberRepository.findActiveMembers();

        if (activeMembers.isEmpty()) {
            return;
        }

        int memberCount = activeMembers.size();
        BigDecimal sharePerMember = amount.divide(
                BigDecimal.valueOf(memberCount), 2, RoundingMode.HALF_UP
        );

        for (Member member : activeMembers) {
            ExpenseShare share = new ExpenseShare();
            share.setExpenseId(expenseId);
            share.setMemberId(member.getMemberId());
            share.setShareAmount(sharePerMember);
            expenseRepository.saveExpenseShare(share);
        }
    }

    public List<ExpenseShare> getExpenseShares(Integer expenseId) {
        return expenseRepository.findSharesByExpenseId(expenseId);
    }
}