package com.hydraxon91.backend.repositories.ArticleRepositories;

import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticlePageRepository extends JpaRepository<ArticlePage, UUID> {
    boolean existsBySlug(String slug);
}
