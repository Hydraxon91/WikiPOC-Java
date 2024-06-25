package com.hydraxon91.backend.services.User;

import com.hydraxon91.backend.models.UserModels.Comment;
import com.hydraxon91.backend.repositories.UserRepository.UserCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    private final UserCommentRepository userCommentRepository;

    @Autowired
    public CommentService(UserCommentRepository userCommentRepository) {
        this.userCommentRepository = userCommentRepository;
    }

    public Optional<Comment> getById(UUID id) {
        return userCommentRepository.findById(id);
    }

    public void add(Comment comment) {
        if (comment.getReplyToCommentId() != null) {
            Comment parentComment = userCommentRepository.findById(comment.getReplyToCommentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));
            parentComment.getReplies().add(comment);
            userCommentRepository.save(parentComment);
        } else {
            userCommentRepository.save(comment);
        }
    }

    public void update(UUID commentId, String updatedContent) {
        Comment existingComment = userCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        existingComment.setContent(updatedContent);
        existingComment.setEdited(true);
        userCommentRepository.save(existingComment);
    }

    public void delete(UUID id) {
        Comment existingComment = userCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        userCommentRepository.delete(existingComment);
    }
}
