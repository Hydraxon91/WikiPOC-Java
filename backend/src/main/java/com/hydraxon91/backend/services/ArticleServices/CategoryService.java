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

    public Category addCategory(String categoryName) throws IllegalArgumentException {
        Optional<Category> existingCategory = getCategoryByName(categoryName);
        if (existingCategory.isPresent()) {
            throw new IllegalArgumentException("Category already exists");
        }

        Category newCategory = new Category();
        newCategory.setCategoryName(categoryName);
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
}
