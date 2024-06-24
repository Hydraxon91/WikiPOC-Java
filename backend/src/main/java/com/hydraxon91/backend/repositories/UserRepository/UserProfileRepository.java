package com.hydraxon91.backend.repositories.UserRepository;

import com.hydraxon91.backend.models.UserModels.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByUserName(String username);
    Optional<UserProfile> findByUserId(UUID userId);
}
