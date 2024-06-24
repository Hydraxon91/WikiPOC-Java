package com.hydraxon91.backend.services.User;

import com.hydraxon91.backend.models.UserModels.UserComment;
import com.hydraxon91.backend.repositories.UserRepository.UserCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserCommentService {

    private final UserCommentRepository userCommentRepository;

    @Autowired
    public UserCommentService(UserCommentRepository userCommentRepository) {
        this.userCommentRepository = userCommentRepository;
    }

    public Optional<UserComment> getById(UUID id) {
        return userCommentRepository.findById(id);
    }

    public void add(UserComment comment) {
        if (comment.getReplyToCommentId() != null) {
            UserComment parentComment = userCommentRepository.findById(comment.getReplyToCommentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));
            parentComment.getReplies().add(comment);
            userCommentRepository.save(parentComment);
        } else {
            userCommentRepository.save(comment);
        }
    }

    public void update(UUID commentId, String updatedContent) {
        UserComment existingComment = userCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        existingComment.setContent(updatedContent);
        existingComment.setEdited(true);
        userCommentRepository.save(existingComment);
    }

    public void delete(UUID id) {
        UserComment existingComment = userCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        userCommentRepository.delete(existingComment);
    }
}
