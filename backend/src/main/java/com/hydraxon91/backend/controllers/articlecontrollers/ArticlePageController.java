package com.hydraxon91.backend.controllers.articlecontrollers;

import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import com.hydraxon91.backend.models.ArticleModels.ArticlePageTitleAndIdProjection;
import com.hydraxon91.backend.models.ArticleModels.Paragraph;
import com.hydraxon91.backend.models.Forms.ArticlePageWithImagesInputModel;
import com.hydraxon91.backend.models.Forms.ImageFormModel;
import com.hydraxon91.backend.models.Forms.WPWithImagesOutputModel;
import com.hydraxon91.backend.services.ArticleServices.ArticlePageProjection;
import com.hydraxon91.backend.services.ArticleServices.ArticlePageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/article-pages")
public class ArticlePageController {

    private static final Logger logger = LoggerFactory.getLogger(ArticlePageController.class);

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
        try {
            WPWithImagesOutputModel outputModel = articlePageService.getArticlePageById(id);
            if (outputModel == null || outputModel.getArticlePage() == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(outputModel);
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getbyslug/{slug}")
    public ResponseEntity<WPWithImagesOutputModel> getArticlePageBySlug(@PathVariable String slug) {
        try {
            WPWithImagesOutputModel outputModel = articlePageService.getArticleBySlug(slug);
            if (outputModel == null || outputModel.getArticlePage() == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(outputModel);
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/addAdmin")
    public ResponseEntity<ArticlePage> createArticlePageAdmin(
            @ModelAttribute ArticlePageWithImagesInputModel inputModel) {
        try {
            if (inputModel.getImages() != null) {
                for (ImageFormModel image : inputModel.getImages()) {
                    logger.info("Received image: {}", image); // Adjust the logging format as per your ImageFormModel
                }
            }
            
            ArticlePage createdArticlePage = articlePageService.createArticlePageAdmin(inputModel);
            return ResponseEntity.ok(createdArticlePage);
        } catch (Exception e) {
            logger.error("Error creating article page", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/addUser")
    public ResponseEntity<ArticlePage> createArticlePageUser(
            @ModelAttribute ArticlePageWithImagesInputModel inputModel) {
        try {
            ArticlePage createdArticlePage = articlePageService.createArticlePageUser(inputModel);
            return ResponseEntity.ok(createdArticlePage);
        } catch (Exception e) {
            logger.error("Error creating article page", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ArticlePage> updateArticlePage(
            @PathVariable UUID id,
            @ModelAttribute ArticlePageWithImagesInputModel inputModel) {
        try {
            ArticlePage updatedArticlePage = articlePageService.updateArticlePage(id, inputModel);
            return ResponseEntity.ok(updatedArticlePage);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating article page with id: " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            logger.error("IO Exception while updating article page with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
