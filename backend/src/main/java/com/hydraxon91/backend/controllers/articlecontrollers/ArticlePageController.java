package com.hydraxon91.backend.controllers.articlecontrollers;

import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import com.hydraxon91.backend.models.ArticleModels.Paragraph;
import com.hydraxon91.backend.models.Forms.WPWithImagesOutputModel;
import com.hydraxon91.backend.services.ArticleServices.ArticlePageProjection;
import com.hydraxon91.backend.services.ArticleServices.ArticlePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
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

    @GetMapping("/titles-and-slugs")
    public ResponseEntity<List<ArticlePageProjection>> getArticleTitlesAndSlugs() {
        List<ArticlePageProjection> articleTitlesAndSlugs = articlePageService.getArticleTitlesAndSlugs();
        return ResponseEntity.ok(articleTitlesAndSlugs);
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<WPWithImagesOutputModel> getArticlePageById(@PathVariable UUID id) {
        WPWithImagesOutputModel outputModel = new WPWithImagesOutputModel();
        outputModel.setArticlePage(articlePageService.getArticlePageById(id));
        if (outputModel.getArticlePage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(outputModel);
    }

    @GetMapping("/getbyslug/{slug}")
    public ResponseEntity<WPWithImagesOutputModel> getArticlePageBySlug(@PathVariable String slug) {
        WPWithImagesOutputModel outputModel = new WPWithImagesOutputModel();
        articlePageService.getArticleBySlug(slug).ifPresent(articlePage -> {
            outputModel.setArticlePage(articlePage);
            // You may add logic here to populate UserSubmittedArticlePage and Images if needed
        });

        if (outputModel.getArticlePage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(outputModel);
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
