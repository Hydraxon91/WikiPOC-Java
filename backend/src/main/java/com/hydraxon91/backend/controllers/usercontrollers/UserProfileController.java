package com.hydraxon91.backend.controllers.usercontrollers;

import com.hydraxon91.backend.models.UserModels.ApplicationUser;
import com.hydraxon91.backend.models.UserModels.Role;
import com.hydraxon91.backend.models.UserModels.UserProfile;
import com.hydraxon91.backend.services.Authentication.TokenService;
import com.hydraxon91.backend.services.User.UserProfileService;
import com.hydraxon91.backend.services.User.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getById(@PathVariable("id") UUID id) {
        Optional<UserProfile> userProfile = userProfileService.getById(id);
        return userProfile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserProfile> getByUsername(@PathVariable String username) {
        Optional<UserProfile> userProfile = userProfileService.getByUsername(username);
        return userProfile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/update")
    public ResponseEntity<UserProfile> updateUserProfile(
            @RequestPart("profile") Map<String, String> profileFormData,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            @AuthenticationPrincipal ApplicationUser applicationUser) throws IOException {

        ApplicationUser currentUser = userService.loadUserByUsername(applicationUser.getUsername());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        String role = currentUser.getRole().getName();
        
        String idStr = profileFormData.get("id");

        try {
            UUID id = UUID.fromString(idStr);
            
            if (!currentUser.getProfile().getId().equals(id) && !role.equals("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            String displayName = profileFormData.get("displayName");

            UserProfile updatedUserProfile = userProfileService.updateUserProfile(id, displayName, profilePicture);

            return ResponseEntity.ok(updatedUserProfile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserProfile());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails){
        
        ApplicationUser currentUser = (ApplicationUser) userDetails;
        String role = tokenService.extractRole(userDetails.getUsername());
        
        if (!role.equals("ADMIN") || !currentUser.getId().equals(id)) {
            return ResponseEntity.status(403).build();
        }

        userProfileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
