package com.hydraxon91.backend.repositories.ArticleRepositories;

import com.hydraxon91.backend.models.ArticleModels.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, UUID> {
    List<Paragraph> findByArticlePage_Id(UUID articlePageId);
}
