package com.hydraxon91.backend.controllers.articlecontrollers;

import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import com.hydraxon91.backend.models.ArticleModels.Paragraph;
import com.hydraxon91.backend.services.ArticleServices.ArticlePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/article-pages")
public class ArticlePageController {

    private final ArticlePageService articlePageService;

    @Autowired
    public ArticlePageController(ArticlePageService articlePageService) {
        this.articlePageService = articlePageService;
    }

    @GetMapping
    public ResponseEntity<List<ArticlePage>> getAllArticlePages() {
        List<ArticlePage> articlePages = articlePageService.getAllArticlePages();
        return ResponseEntity.ok(articlePages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticlePage> getArticlePageById(@PathVariable UUID id) {
        ArticlePage articlePage = articlePageService.getArticlePageById(id);
        if (articlePage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(articlePage);
    }

    @PostMapping
    public ResponseEntity<ArticlePage> createArticlePage(@RequestBody ArticlePage articlePage) {
        ArticlePage createdArticlePage = articlePageService.createArticlePage(articlePage);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdArticlePage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticlePage> updateArticlePage(@PathVariable UUID id, @RequestBody ArticlePage updatedArticlePage) {
        ArticlePage existingArticlePage = articlePageService.getArticlePageById(id);
        if (existingArticlePage == null) {
            return ResponseEntity.notFound().build();
        }
        updatedArticlePage.setId(id);
        ArticlePage savedArticlePage = articlePageService.updateArticlePage(id, updatedArticlePage);
        return ResponseEntity.ok(savedArticlePage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticlePage(@PathVariable UUID id) {
        boolean deleted = articlePageService.deleteArticlePage(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/paragraphs")
    public ResponseEntity<List<Paragraph>> getParagraphsByArticlePageId(@PathVariable UUID id) {
        List<Paragraph> paragraphs = articlePageService.getParagraphsByArticlePageId(id);
        return ResponseEntity.ok(paragraphs);
    }
}
