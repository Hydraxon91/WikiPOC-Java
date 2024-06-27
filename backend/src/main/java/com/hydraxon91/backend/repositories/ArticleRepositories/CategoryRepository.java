package com.hydraxon91.backend.repositories.ArticleRepositories;

import com.hydraxon91.backend.models.ArticleModels.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByCategoryName(String categoryName);
    Optional<Category> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
