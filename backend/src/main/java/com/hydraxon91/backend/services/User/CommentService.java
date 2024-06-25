package com.hydraxon91.backend.services.User;

import com.hydraxon91.backend.models.UserModels.Comment;
import com.hydraxon91.backend.repositories.UserRepository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Optional<Comment> getById(UUID id) {
        return commentRepository.findById(id);
    }

    public void add(Comment comment) {
        if (comment.getReplyToCommentId() != null) {
            Comment parentComment = commentRepository.findById(comment.getReplyToCommentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));
            parentComment.getReplies().add(comment);
            commentRepository.save(parentComment);
        } else {
            commentRepository.save(comment);
        }
    }

    public void update(UUID commentId, String updatedContent) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        existingComment.setContent(updatedContent);
        existingComment.setEdited(true);
        commentRepository.save(existingComment);
    }

    public void delete(UUID id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        commentRepository.delete(existingComment);
    }
}
