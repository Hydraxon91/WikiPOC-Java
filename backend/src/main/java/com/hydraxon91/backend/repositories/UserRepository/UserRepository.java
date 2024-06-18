package com.hydraxon91.backend.repositories.UserRepository;

import com.hydraxon91.backend.models.UserModels.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<ApplicationUser, UUID>{
    Optional<ApplicationUser> findByEmail(String email);
    Optional<ApplicationUser> findByUsername(String username);

    @Modifying
    @Query("UPDATE ApplicationUser u SET u.username = ?1 WHERE u.id = ?2")
    int updateUsernameById(String username, UUID id);
}
