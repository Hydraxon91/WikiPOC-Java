package com.hydraxon91.backend.repositories.ArticleRepositories;

import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import com.hydraxon91.backend.models.ArticleModels.ArticlePageTitleAndIdProjection;
import com.hydraxon91.backend.services.ArticleServices.ArticlePageProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArticlePageRepository extends JpaRepository<ArticlePage, UUID> {
    boolean existsBySlug(String slug);
    Optional<ArticlePage> findBySlug(String slug);
    List<ArticlePage> findByCategoryId(UUID categoryId);
    long countByCategoryId(UUID categoryId);
    List<ArticlePage> findByApprovedIsTrue();
    List<ArticlePage> findByApprovedIsFalseAndIsNewPageTrue();
    List<ArticlePage> findByApprovedIsFalseAndIsNewPageFalse();
    List<ArticlePageTitleAndIdProjection> findTitlesAndIdsByApprovedIsFalseAndIsNewPageTrue();
    List<ArticlePageTitleAndIdProjection> findTitlesAndIdsByApprovedIsFalseAndIsNewPageFalse();
    
    @Query("SELECT ap.title AS title, ap.slug AS slug FROM ArticlePage ap")
    List<ArticlePageProjection> findArticleTitlesAndSlugs();
}
