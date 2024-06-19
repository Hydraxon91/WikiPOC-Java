package com.hydraxon91.backend.services.User;

import com.hydraxon91.backend.models.UserModels.UserProfile;
import com.hydraxon91.backend.repositories.UserRepository.UserProfileRepository;
import com.hydraxon91.backend.security.JwtTokenFilter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserProfileService {
    
    private final UserProfileRepository userProfileRepository;
    
    @Value("${pictures.path-container}")
    private String picturesPathContainer;
    
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public Optional<UserProfile> getById(UUID id) {
        return userProfileRepository.findById(id);
    }

    public Optional<UserProfile> getByUsername(String username) {
        return userProfileRepository.findByUserName(username);
    }

    public Optional<UserProfile> getByUserId(UUID userId) {
        return userProfileRepository.findByUserId(userId);
    }
    
    @Transactional
    public UserProfile updateUserProfile(UUID existingId, String displayName, MultipartFile profilePictureFile) throws IOException {
        UserProfile existingProfile = userProfileRepository.findById(existingId).orElse(null);

        if (existingProfile == null) {
            throw new IllegalArgumentException("UserProfile with ID " + existingId + " not found.");
        }
        
        existingProfile.setDisplayName(displayName);
        if (profilePictureFile != null && !profilePictureFile.isEmpty()) {
            String fileName = "profile_pictures/" + existingProfile.getUserName() + "_pfp" + getExtension(profilePictureFile.getOriginalFilename());
            Path filePath = Paths.get(picturesPathContainer, fileName);
            File file = new File(filePath.toString());
            file.getParentFile().mkdirs();
            
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(profilePictureFile.getBytes());
            }
            
            existingProfile.setProfilePicture(fileName);
        }
        
        userProfileRepository.save(existingProfile);
        
        return existingProfile;
    }

    @Transactional
    public void deleteProfile(UUID id) {
        UserProfile existingProfile = userProfileRepository.findById(id)
                .orElse(null);

        if (existingProfile != null) {
            // Delete the profile picture directory if it exists
            deleteProfilePictureDirectory(existingProfile.getUserName());

            // Delete the user profile
            userProfileRepository.deleteById(id);
        }
    }

    private void deleteProfilePictureDirectory(String username) {
        Path directoryPath = Paths.get(picturesPathContainer, "profile_pictures", username);
        File directory = directoryPath.toFile();

        if (directory.exists() && directory.isDirectory()) {
            try {
                Files.walk(directory.toPath())
                        .map(Path::toFile)
                        .forEach(File::delete);

                // Delete the directory itself
                Files.deleteIfExists(directoryPath);
            } catch (IOException e) {
                // Handle exceptions (logging, rethrowing, etc.)
                e.printStackTrace();
            }
        }
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.'));
    }
}
