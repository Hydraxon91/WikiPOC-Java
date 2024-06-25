package com.hydraxon91.backend.controllers.usercontrollers;

import com.hydraxon91.backend.models.UserModels.Comment;
import com.hydraxon91.backend.services.User.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-comments")
public class CommentController {
    
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable UUID id) {
        Optional<Comment> comment = commentService.getById(id);

        if (comment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comment.get());
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<?> editComment(@PathVariable UUID id, @RequestBody String updatedContent, Principal principal) {
        String userId = getUserIdFromPrincipal(principal);
        Comment existingComment = commentService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (isAuthorizedToEditOrDeleteComment(userId, existingComment)) {
            commentService.update(id, updatedContent);
            return ResponseEntity.ok().body("Comment edited successfully");
        }

        return ResponseEntity.status(401).body("Unauthorized to update this comment");
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID id, Principal principal) {
        String userId = getUserIdFromPrincipal(principal);
        Comment existingComment = commentService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (isAuthorizedToEditOrDeleteComment(userId, existingComment)) {
            commentService.delete(id);
            return ResponseEntity.ok().body("Comment deleted successfully");
        }

        return ResponseEntity.status(401).body("Unauthorized to delete this comment");
    }

    private String getUserIdFromPrincipal(Principal principal) {
        // Assuming the principal's name is the user ID; this may vary based on your setup
        return principal.getName();
    }

    private boolean isAuthorizedToEditOrDeleteComment(String userId, Comment comment) {
        return userId.equals(comment.getUserProfile().getId().toString()) || isAdmin();
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
