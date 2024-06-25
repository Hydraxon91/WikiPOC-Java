package com.hydraxon91.backend.services.ArticleServices;

import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import com.hydraxon91.backend.models.ArticleModels.Paragraph;
import com.hydraxon91.backend.repositories.ArticleRepositories.ArticlePageRepository;
import com.hydraxon91.backend.repositories.ArticleRepositories.ParagraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ArticlePageService {

    private final ArticlePageRepository articlePageRepository;
    private final ParagraphRepository paragraphRepository;

    @Autowired
    public ArticlePageService(ArticlePageRepository articlePageRepository, ParagraphRepository paragraphRepository) {
        this.articlePageRepository = articlePageRepository;
        this.paragraphRepository = paragraphRepository;
    }

    public List<ArticlePage> getAllArticlePages() {
        return articlePageRepository.findAll();
    }

    public ArticlePage getArticlePageById(UUID id) {
        return articlePageRepository.findById(id).orElse(null);
    }

    public ArticlePage createArticlePage(ArticlePage articlePage) {
        // Implement creation logic, including saving paragraphs if needed
        return articlePageRepository.save(articlePage);
    }

    public ArticlePage updateArticlePage(UUID id, ArticlePage updatedArticlePage) {
        // Implement update logic, including updating paragraphs if needed
        return articlePageRepository.save(updatedArticlePage);
    }

    public boolean deleteArticlePage(UUID id) {
        try {
            articlePageRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Implement methods for handling paragraphs related to ArticlePage

    public List<Paragraph> getParagraphsByArticlePageId(UUID articlePageId) {
        // Implement method to fetch paragraphs by articlePageId
        return paragraphRepository.findByArticlePage_Id(articlePageId);
    }
}