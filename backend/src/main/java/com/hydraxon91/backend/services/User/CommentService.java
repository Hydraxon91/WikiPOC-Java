package com.hydraxon91.backend.services.User;

import com.hydraxon91.backend.models.ArticleModels.ArticleComment;
import com.hydraxon91.backend.models.UserModels.Comment;
import com.hydraxon91.backend.repositories.UserRepository.UserCommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    private final UserCommentRepository userCommentRepository;

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    
    @Autowired
    public CommentService(UserCommentRepository userCommentRepository) {
        this.userCommentRepository = userCommentRepository;
    }

    public Optional<Comment> getById(UUID id) {
        return userCommentRepository.findById(id);
    }

    public void add(Comment comment) {
        if (comment.getReplyToCommentId() != null) {
            logger.info("Adding reply to comment with ID: {}", comment.getReplyToCommentId());
            Comment parentComment = userCommentRepository.findById(comment.getReplyToCommentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));
            parentComment.getReplies().add(comment);
            userCommentRepository.save(parentComment);
            logger.info("Reply added and parent comment saved.");
        } else {
            userCommentRepository.save(comment);
            logger.info("Comment saved without parent.");
        }
    }

    public void addArticleComment(ArticleComment articleComment) {
        if (articleComment.getReplyToCommentId() != null) {
            logger.info("Adding article comment reply to comment with ID: {}", articleComment.getReplyToCommentId());
            Comment parentComment = userCommentRepository.findById(articleComment.getReplyToCommentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));
            
            articleComment.setReplyToComment(parentComment);
            articleComment.setReplyToCommentId(parentComment.getUuid());
            userCommentRepository.save(articleComment);  // Save the reply comment

            parentComment.getReplies().add(articleComment);  // Add reply to parent's replies
            userCommentRepository.save(parentComment);  // Save the parent comment
            logger.info("Article comment reply added and parent comment saved.");
        } else {
            userCommentRepository.save(articleComment);
            logger.info("Article comment saved without parent.");
        }
    }

    public void update(UUID commentId, String updatedContent) {
        logger.info("Updating comment with ID: {}", commentId);
        Comment existingComment = userCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        existingComment.setContent(updatedContent);
        existingComment.setEdited(true);
        userCommentRepository.save(existingComment);
        logger.info("Comment updated and saved.");
    }

    public void delete(UUID id) {
        logger.info("Deleting comment with ID: {}", id);
        Comment existingComment = userCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        userCommentRepository.delete(existingComment);
        logger.info("Comment deleted.");
    }
}
