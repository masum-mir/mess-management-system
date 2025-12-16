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

@GetMapping
public String listCollections(@RequestParam(required = false) Integer month,
                              @RequestParam(required = false) Integer year,
                              Model model) {
    LocalDate now = LocalDate.now();
    int selectedMonth = (month != null) ? month : now.getMonthValue();
    int selectedYear = (year != null) ? year : now.getYear();

    List<MemberCollectionDto> memberSummaries = collectionService.getMemberSummaryByMonth(selectedMonth, selectedYear);

    List<MemberCollectionDto> memberCollections = new ArrayList<>();
    BigDecimal totalAmount = BigDecimal.ZERO;
    int totalTransactions = 0;

    for (MemberCollectionDto summary : memberSummaries) {

        List<Collection> transactions = collectionService.getMemberTransactions(
                summary.getMemberId(), selectedMonth, selectedYear);

        MemberCollectionDto collectionDto = new MemberCollectionDto();
        collectionDto.setMemberId(summary.getMemberId());
        collectionDto.setMemberName(summary.getMemberName());
        collectionDto.setTransactionCount(summary.getTransactionCount());
        collectionDto.setTotalAmount(summary.getTotalAmount());
        collectionDto.setTransactions(transactions);

        memberCollections.add(collectionDto);
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

    return "collection/collection-list";
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