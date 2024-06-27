package com.hydraxon91.backend.controllers.articlecontrollers;

import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import com.hydraxon91.backend.models.ArticleModels.ArticlePageTitleAndIdProjection;
import com.hydraxon91.backend.models.ArticleModels.Paragraph;
import com.hydraxon91.backend.models.Forms.ImageFormModel;
import com.hydraxon91.backend.models.Forms.WPWithImagesOutputModel;
import com.hydraxon91.backend.services.ArticleServices.ArticlePageProjection;
import com.hydraxon91.backend.services.ArticleServices.ArticlePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/unapproved-new-pages")
    public ResponseEntity<List<ArticlePage>> getUnapprovedUserSubmittedNewPages() {
        List<ArticlePage> pages = articlePageService.getUnapprovedUserSubmittedNewPages();
        return ResponseEntity.ok(pages);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/unapproved-updates")
    public ResponseEntity<List<ArticlePage>> getUnapprovedUserSubmittedUpdates() {
        List<ArticlePage> pages = articlePageService.getUnapprovedUserSubmittedUpdates();
        return ResponseEntity.ok(pages);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/unapproved-new-page-titles")
    public ResponseEntity<List<ArticlePageTitleAndIdProjection>> getUnapprovedNewPageTitlesAndIds() {
        List<ArticlePageTitleAndIdProjection> titlesAndIds = articlePageService.getUnapprovedNewPageTitlesAndIds();
        return ResponseEntity.ok(titlesAndIds);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/unapproved-update-titles")
    public ResponseEntity<List<ArticlePageTitleAndIdProjection>> getUnapprovedUpdateTitlesAndIds() {
        List<ArticlePageTitleAndIdProjection> titlesAndIds = articlePageService.getUnapprovedUpdateTitlesAndIds();
        return ResponseEntity.ok(titlesAndIds);
    }

    @PostMapping("/add")
    public ResponseEntity<ArticlePage> createArticlePage(@RequestBody ArticlePage articlePage, @RequestPart("images") List<ImageFormModel> images) {
        try {
            ArticlePage createdArticlePage = articlePageService.createArticlePage(articlePage, images);
            return ResponseEntity.ok(createdArticlePage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ArticlePage> updateArticlePage(
            @PathVariable UUID id,
            @RequestPart("articlePage") ArticlePage articlePage,
            @RequestPart("images") List<ImageFormModel> images) {
        try {
            ArticlePage updatedArticlePage = articlePageService.updateArticlePage(id, articlePage, images);
            return ResponseEntity.ok(updatedArticlePage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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
