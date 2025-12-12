package com.mms.controller;

import com.mms.model.Collection;
import com.mms.service.CollectionService;
import com.mms.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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

        List<Collection> collections = collectionService.getCollectionsByMonth(selectedMonth, selectedYear);

        model.addAttribute("collections", collections);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("pageTitle", "Collections");

        return "collection/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Collection collection = new Collection();
        collection.setCollectDate(LocalDate.now());

        model.addAttribute("collection", collection);
        model.addAttribute("members", memberService.getActiveMembers());
        model.addAttribute("leaders", memberService.getLeaders());
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
        model.addAttribute("leaders", memberService.getLeaders());
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