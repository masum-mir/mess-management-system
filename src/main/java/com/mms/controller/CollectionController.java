package com.mms.controller;

import com.mms.model.Collection;
import com.mms.model.dto.MemberCollectionDto;
import com.mms.service.CollectionService;
import com.mms.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/collections")
public class CollectionController {

    private final CollectionService collectionService;
    private final MemberService memberService;

    public CollectionController(CollectionService collectionService,
                                MemberService memberService) {
        this.collectionService = collectionService;
        this.memberService = memberService;
    }

//    @GetMapping
//    public String listCollections(@RequestParam(required = false) Integer month,
//                                  @RequestParam(required = false) Integer year,
//                                  Model model) {
//        LocalDate now = LocalDate.now();
//        int selectedMonth = (month != null) ? month : now.getMonthValue();
//        int selectedYear = (year != null) ? year : now.getYear();
//
//        List<Collection> collections = collectionService.getCollectionsByMonth(selectedMonth, selectedYear);
//
//        model.addAttribute("collections", collections);
//        model.addAttribute("members", memberService.getActiveMembers());
//        model.addAttribute("leaders", memberService.getLeaders());
//        model.addAttribute("selectedMonth", selectedMonth);
//        model.addAttribute("selectedYear", selectedYear);
//        model.addAttribute("pageTitle", "Collections");
//
//        return "collection/list";
//    }

@GetMapping
public String listCollections(@RequestParam(required = false) Integer month,
                              @RequestParam(required = false) Integer year,
                              Model model) {
    LocalDate now = LocalDate.now();
    int selectedMonth = (month != null) ? month : now.getMonthValue();
    int selectedYear = (year != null) ? year : now.getYear();

    // Get aggregated member summaries from SQL
    List<MemberCollectionDto> memberSummaries =
            collectionService.getMemberSummaryByMonth(selectedMonth, selectedYear);

    // Create list to hold member collections with transactions
    List<MemberCollectionAggregate> memberCollections = new ArrayList<>();
    BigDecimal totalAmount = BigDecimal.ZERO;
    int totalTransactions = 0;

    // For each member, get their individual transactions
    for (MemberCollectionDto summary : memberSummaries) {

// Add these imports at the top of your CollectionController:
// import com.mms.model.MemberCollectionSummary;
// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.List;
        List<Collection> transactions = collectionService.getMemberTransactions(
                summary.getMemberId(), selectedMonth, selectedYear);

        MemberCollectionAggregate aggregate = new MemberCollectionAggregate();
        aggregate.setMemberId(summary.getMemberId());
        aggregate.setMemberName(summary.getMemberName());
        aggregate.setTransactionCount(summary.getTransactionCount());
        aggregate.setTotalAmount(summary.getTotalAmount());
        aggregate.setTransactions(transactions);

        memberCollections.add(aggregate);
        totalAmount = totalAmount.add(summary.getTotalAmount());
        totalTransactions += summary.getTransactionCount();
    }

    model.addAttribute("memberCollections", memberCollections);
    model.addAttribute("totalAmount", totalAmount);
    model.addAttribute("totalTransactions", totalTransactions);
    model.addAttribute("members", memberService.getActiveMembers());
    model.addAttribute("leaders", memberService.getManagers());
    model.addAttribute("selectedMonth", selectedMonth);
    model.addAttribute("selectedYear", selectedYear);
    model.addAttribute("pageTitle", "Collections");

    return "collection/list";
}

    // Helper class for view
    public static class MemberCollectionAggregate {
        private Integer memberId;
        private String memberName;
        private Integer transactionCount;
        private BigDecimal totalAmount;
        private List<Collection> transactions;

        // Getters and Setters
        public Integer getMemberId() { return memberId; }
        public void setMemberId(Integer memberId) { this.memberId = memberId; }

        public String getMemberName() { return memberName; }
        public void setMemberName(String memberName) { this.memberName = memberName; }

        public Integer getTransactionCount() { return transactionCount; }
        public void setTransactionCount(Integer transactionCount) {
            this.transactionCount = transactionCount;
        }

        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public List<Collection> getTransactions() { return transactions; }
        public void setTransactions(List<Collection> transactions) {
            this.transactions = transactions;
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Collection collection = new Collection();
        collection.setCollectDate(LocalDate.now());

        model.addAttribute("collection", collection);
        model.addAttribute("members", memberService.getActiveMembers());
        model.addAttribute("leaders", memberService.getManagers());
        model.addAttribute("pageTitle", "Add Collection");

        return "collection/add";
    }

    @PostMapping("/add")
    public String addCollection(@ModelAttribute Collection collection,
                                RedirectAttributes redirectAttributes) {
        try {
            collection.setMonth(collection.getCollectDate().getMonthValue());
            collection.setYear(collection.getCollectDate().getYear());

            collectionService.saveCollection(collection);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Collection added successfully!");
            return "redirect:/collections";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error adding collection: " + e.getMessage());
            return "redirect:/collections/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Collection collection = collectionService.getCollectionById(id);
        if (collection == null) {
            return "redirect:/collections";
        }

        model.addAttribute("collection", collection);
        model.addAttribute("members", memberService.getActiveMembers());
        model.addAttribute("leaders", memberService.getManagers());
        model.addAttribute("pageTitle", "Edit Collection");

        return "collection/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCollection(@PathVariable Integer id,
                                   @ModelAttribute Collection collection,
                                   RedirectAttributes redirectAttributes) {
        try {
            collection.setCollectionId(id);
            collection.setMonth(collection.getCollectDate().getMonthValue());
            collection.setYear(collection.getCollectDate().getYear());

            collectionService.updateCollection(collection);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Collection updated successfully!");
            return "redirect:/collections";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error updating collection: " + e.getMessage());
            return "redirect:/collections/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCollection(@PathVariable Integer id,
                                   RedirectAttributes redirectAttributes) {
        try {
            collectionService.deleteCollection(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Collection deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting collection: " + e.getMessage());
        }
        return "redirect:/collections";
    }
}