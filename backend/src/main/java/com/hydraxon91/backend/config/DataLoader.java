package com.hydraxon91.backend.config;

import com.hydraxon91.backend.models.UserModels.ApplicationUser;
import com.hydraxon91.backend.models.UserModels.Role;
import com.hydraxon91.backend.models.UserModels.UserProfile;
import com.hydraxon91.backend.repositories.RoleRepository.RoleRepository;
import com.hydraxon91.backend.repositories.UserRepository.UserRepository;
import com.hydraxon91.backend.repositories.UserRepository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository; // Assuming you have a RoleRepository

    @Override
    public void run(String... args) throws Exception {
        // Check if default roles exist, and create if not
        createDefaultRoles();
    }

    private void createDefaultRoles() {
        // Check if USER role with specific name exists
        Optional<Role> userRoleOptional = roleRepository.findByName("USER");

        if (userRoleOptional.isEmpty()) {
            // Create USER role if it doesn't exist
            Role role = new Role();
            role.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
            role.setName("USER"); // Or whatever your role details are
            roleRepository.save(role);
        }
    }

}

