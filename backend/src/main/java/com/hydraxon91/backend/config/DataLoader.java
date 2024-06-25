package com.hydraxon91.backend.config;

import com.hydraxon91.backend.models.ArticleModels.Category;
import com.hydraxon91.backend.models.UserModels.ApplicationUser;
import com.hydraxon91.backend.models.UserModels.Role;
import com.hydraxon91.backend.models.UserModels.UserProfile;
import com.hydraxon91.backend.repositories.ArticleRepositories.CategoryRepository;
import com.hydraxon91.backend.repositories.RoleRepository.RoleRepository;
import com.hydraxon91.backend.repositories.UserRepository.UserRepository;
import com.hydraxon91.backend.repositories.UserRepository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository; // Assuming you have a RoleRepository
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private UserProfileRepository userProfileRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.user.username}")
    private String adminUsername;

    @Value("${admin.user.password}")
    private String adminPassword;

    @Value("${admin.user.email}")
    private String adminEmail;

    @Value("${test.user.username}")
    private String testUsername;

    @Value("${test.user.password}")
    private String testPassword;

    @Value("${test.user.email}")
    private String testEmail;

    @Override
    public void run(String... args) throws Exception {
        // Check if default roles exist, and create if not
        createDefaultRoles();
        
        createAdminUser();

        createTestUser();

        createBaseCategories();
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

        // Check if ADMIN role with specific name exists
        Optional<Role> adminRoleOptional = roleRepository.findByName("ADMIN");

        if (adminRoleOptional.isEmpty()) {
            // Create ADMIN role if it doesn't exist
            Role role = new Role();
            role.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
            role.setName("ADMIN"); // Or whatever your role details are
            roleRepository.save(role);
        }
    }

    private void createBaseCategories() {
        List<String> categoryNames = Arrays.asList("Technology", "Science", "Politics");
        
        for (String categoryName : categoryNames) {
            // Check if category already exists
            if (categoryRepository.findByCategoryName(categoryName).isEmpty()) {
                // Create category
                Category category = new Category(categoryName);
                categoryRepository.save(category);
            }
        }
    }
    
    private void createAdminUser() {
        // Check if admin user already exists
        Optional<ApplicationUser> adminUserOptional = userRepository.findByUsername(adminUsername);
        if (adminUserOptional.isEmpty()) {
            // Create admin user
            ApplicationUser adminUser = new ApplicationUser();
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setUsername(adminUsername);
            adminUser.setEmail(adminEmail);
            adminUser.setActive(true);
            adminUser.setCreatedDate(LocalDateTime.now()); // Set current date/time
            adminUser.setLastModifiedDate(LocalDateTime.now());
            ApplicationUser savedUser = userRepository.save(adminUser);
            // Only proceed if the adminUser has a valid ID
            if (savedUser.getId() != null) {
                // Create admin profile
                UserProfile adminProfile = new UserProfile(adminUsername, null);
                adminProfile.setUser(savedUser);
                
                // Set UserProfile to ApplicationUser
                savedUser.setProfile(adminProfile);

                // Assign roles to admin user
                Optional<Role> adminRoleOptional = roleRepository.findByName("ADMIN");
                adminRoleOptional.ifPresent(role -> savedUser.setRole(role));

                // Save the updated savedUser (with profile) back to the database
                userRepository.save(savedUser);
                // Save admin profile
                // userProfileRepository.save(adminProfile);
            }
        } 
    }

    private void createTestUser() {
        // Check if admin user already exists
        Optional<ApplicationUser> adminUserOptional = userRepository.findByUsername(testUsername);
        if (adminUserOptional.isEmpty()) {
            // Create admin user
            ApplicationUser testUser = new ApplicationUser();
            testUser.setPassword(passwordEncoder.encode(testPassword));
            testUser.setUsername(testUsername);
            testUser.setEmail(testEmail);
            testUser.setActive(true);
            testUser.setCreatedDate(LocalDateTime.now()); // Set current date/time
            testUser.setLastModifiedDate(LocalDateTime.now());
            ApplicationUser savedUser = userRepository.save(testUser);
            // Only proceed if the adminUser has a valid ID
            if (savedUser.getId() != null) {
                // Create admin profile
                UserProfile adminProfile = new UserProfile(testUsername, null);
                adminProfile.setUser(savedUser);

                // Set UserProfile to ApplicationUser
                savedUser.setProfile(adminProfile);

                // Assign roles to admin user
                Optional<Role> testRoleOptional = roleRepository.findByName("USER");
                testRoleOptional.ifPresent(role -> savedUser.setRole(role));

                // Save the updated savedUser (with profile) back to the database
                userRepository.save(savedUser);
                // Save admin profile
                // userProfileRepository.save(adminProfile);
            }
        }
    }

}

