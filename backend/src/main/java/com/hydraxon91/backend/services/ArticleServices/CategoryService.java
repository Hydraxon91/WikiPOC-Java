package com.hydraxon91.backend.services.ArticleServices;

import com.hydraxon91.backend.models.ArticleModels.Category;
import com.hydraxon91.backend.repositories.ArticleRepositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryByName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    public Optional<Category> getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    public Category addCategory(String categoryName) throws IllegalArgumentException {
        Optional<Category> existingCategory = getCategoryByName(categoryName);
        if (existingCategory.isPresent()) {
            throw new IllegalArgumentException("Category already exists");
        }

        Category newCategory = new Category();
        newCategory.setCategoryName(categoryName);
        String slug = generateSlug(categoryName);
        newCategory.setSlug(slug);
        return categoryRepository.save(newCategory);
    }

    public boolean deleteCategory(UUID categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            categoryRepository.deleteById(categoryId);
            return true;
        }
        return false;
    }

    public String generateSlug(String title) {
        String baseSlug = title.toLowerCase().replaceAll("\\s+", "-");
        String finalSlug = baseSlug;
        int suffix = 1;

        while (isSlugExists(finalSlug)) {
            finalSlug = baseSlug + "-" + suffix++;
        }

        return finalSlug;
    }

    public boolean isSlugExists(String slug) {
        return categoryRepository.existsBySlug(slug);
    }
}
