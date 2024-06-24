package com.hydraxon91.backend.repositories.UserRepository;

import com.hydraxon91.backend.models.UserModels.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCommentRepository extends JpaRepository<UserComment, UUID> {
}
