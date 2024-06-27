package com.hydraxon91.backend.controllers.articlecontrollers;

import com.hydraxon91.backend.models.ArticleModels.ArticleComment;
import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import com.hydraxon91.backend.models.ArticleModels.ArticlePageTitleAndIdProjection;
import com.hydraxon91.backend.models.ArticleModels.Paragraph;
import com.hydraxon91.backend.models.Forms.ArticlePageWithImagesInputModel;
import com.hydraxon91.backend.models.Forms.ImageFormModel;
import com.hydraxon91.backend.models.Forms.WPWithImagesOutputModel;
import com.hydraxon91.backend.models.UserModels.ApplicationUser;
import com.hydraxon91.backend.services.ArticleServices.ArticlePageProjection;
import com.hydraxon91.backend.services.ArticleServices.ArticlePageService;
import com.hydraxon91.backend.services.Authentication.TokenService;
import com.hydraxon91.backend.services.User.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/article-pages")
public class ArticlePageController {

    private static final Logger logger = LoggerFactory.getLogger(ArticlePageController.class);

    private final ArticlePageService articlePageService;
    
    private final CommentService commentService;

    private final TokenService tokenService;

    @Autowired
    public ArticlePageController(ArticlePageService articlePageService, CommentService commentService, TokenService tokenService) {
        this.articlePageService = articlePageService;
        this.commentService = commentService;
        this.tokenService = tokenService;
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
    
    // Comments

    @PostMapping("{id}/comment")
    public ResponseEntity<?> postComment(@PathVariable UUID id, @RequestBody ArticleComment comment, @AuthenticationPrincipal UserDetails userDetails) {
        ApplicationUser currentUser = (ApplicationUser) userDetails;
        String userId = currentUser.getId().toString();

        logger.debug("Posting comment by user: {}", userId);

        try {
            WPWithImagesOutputModel wpWithImagesOutputModel = articlePageService.getArticlePageById(id);
            if (wpWithImagesOutputModel.getArticlePage() == null) {
                logger.error("ArticlePage not found for id: {}", id);
                return ResponseEntity.status(404).body("ArticlePage not found");
            }
            logger.info("ArticlePage found: {}", wpWithImagesOutputModel.getArticlePage());

            // comment.getUserProfile().setId(UUID.fromString(userId));
            comment.setArticlePage(wpWithImagesOutputModel.getArticlePage());
            
            logger.info("Comment before saving: {}", comment.getReplyToCommentId());
            
            commentService.addArticleComment(comment);
            
            logger.debug("Comment posted successfully");
            
            return ResponseEntity.ok().body("Comment posted successfully");
        } catch (EntityNotFoundException e) {
            logger.error("EntityNotFoundException: {}", e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IOException e) {
            logger.error("IOException: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Exception while posting the comment", e);
            return ResponseEntity.status(500).body("An error occurred while posting the comment");
        }
    }

    @PutMapping("{id}/comment/{commentId}")
    public ResponseEntity<?> editComment(@PathVariable UUID id, @PathVariable UUID commentId, @RequestBody String updatedContent, @AuthenticationPrincipal UserDetails userDetails) {
        ApplicationUser currentUser = (ApplicationUser) userDetails;
        String userId = currentUser.getId().toString();
        String role = tokenService.extractRole(userDetails.getUsername());

        ArticleComment existingComment = (ArticleComment) commentService.getById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (isAuthorizedToEditOrDeleteComment(userId, role, existingComment)) {
            commentService.update(commentId, updatedContent);
            return ResponseEntity.ok().body("Comment edited successfully");
        }

        return ResponseEntity.status(401).body("Unauthorized to update this comment");
    }

    @DeleteMapping("{id}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID id, @PathVariable UUID commentId, @AuthenticationPrincipal UserDetails userDetails) {
        ApplicationUser currentUser = (ApplicationUser) userDetails;
        String userId = currentUser.getId().toString();
        String role = tokenService.extractRole(userDetails.getUsername());

        ArticleComment existingComment = (ArticleComment) commentService.getById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (isAuthorizedToEditOrDeleteComment(userId, role, existingComment)) {
            commentService.delete(commentId);
            return ResponseEntity.ok().body("Comment deleted successfully");
        }

        return ResponseEntity.status(401).body("Unauthorized to delete this comment");
    }
    
    private boolean isAuthorizedToEditOrDeleteComment(String userId, String role, ArticleComment comment) {
        return userId.equals(comment.getUserProfile().getId().toString()) || role.equals("ADMIN");
    }
    
}
