package com.mms.service;

import com.mms.model.Category;
import com.mms.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getActiveCategories() {
        return categoryRepository.findActive();
    }

    public List<Category> getNonMealCategories() {
        return categoryRepository.findNonMealCategories();
    }

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }
}