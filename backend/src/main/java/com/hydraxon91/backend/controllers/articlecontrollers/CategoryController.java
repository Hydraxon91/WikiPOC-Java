package com.hydraxon91.backend.controllers.articlecontrollers;

import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import com.hydraxon91.backend.models.ArticleModels.Category;
import com.hydraxon91.backend.services.ArticleServices.ArticlePageService;
import com.hydraxon91.backend.services.ArticleServices.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    @Autowired
    private ArticlePageService articlePageService;

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}/articlePages")
    public List<ArticlePage> getArticlePagesByCategoryId(@PathVariable UUID categoryId) {
        return articlePageService.findArticlePagesByCategoryId(categoryId);
    }

    @GetMapping("/{categorySlug}/approvedNotArchivedArticlePages")
    public ResponseEntity<List<ArticlePage>> getApprovedPagesByCategory(@PathVariable String categorySlug) {
        List<ArticlePage> approvedPages = articlePageService.findApprovedAndNotArchivedPagesByCategoryId(categorySlug);
        return ResponseEntity.ok(approvedPages);
    }

    @GetMapping("/{categoryId}/articlePages/count")
    public ResponseEntity<Long> getArticlePageCountByCategoryId(@PathVariable UUID categoryId) {
        long count = articlePageService.countArticlePagesByCategoryId(categoryId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/getbyname/{categoryName}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String categoryName) {
        Optional<Category> category = categoryService.getCategoryByName(categoryName);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getbyslug/{slug}")
    public ResponseEntity<Category> getCategoryBySlug(@PathVariable String slug) {
        Optional<Category> category = categoryService.getCategoryBySlug(slug);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody String categoryName) {
        try {
            Category newCategory = categoryService.addCategory(categoryName);
            return ResponseEntity.status(201).body(newCategory);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(409).body(null);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {
        boolean result = categoryService.deleteCategory(categoryId);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
