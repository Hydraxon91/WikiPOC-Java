package com.hydraxon91.backend.repositories.RoleRepository;

import com.hydraxon91.backend.models.UserModels.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    
}
