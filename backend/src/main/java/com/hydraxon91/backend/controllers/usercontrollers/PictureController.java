package com.hydraxon91.backend.controllers.usercontrollers;

import com.hydraxon91.backend.config.ProfilePictureSettings;
import com.hydraxon91.backend.models.UserModels.UserProfile;
import com.hydraxon91.backend.services.User.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/public/picture")
public class PictureController {
    
    public static Logger logger = Logger.getLogger(PictureController.class.getName());
    
    private final ProfilePictureSettings profilePictureSettings;
    private final UserProfileService userProfileService;
    
     @Autowired
    public PictureController(ProfilePictureSettings profilePictureSettings, UserProfileService userProfileService) {
         this.profilePictureSettings = profilePictureSettings;
         this.userProfileService = userProfileService;
     }

    @GetMapping("/profile/{profileName}")
    public ResponseEntity<Resource> getPicture(@PathVariable String profileName) {
        logger.info("Fetching profile for username: " + profileName);
        Optional<UserProfile> profileOptional = userProfileService.getByUsername(profileName);
        
        if(profileOptional.isEmpty()) {
            logger.warning("Profile not found for username: " + profileName);
            return ResponseEntity.notFound().build();
        }

        UserProfile profile = profileOptional.get();
        String pictureName = profile.getProfilePicture();
        logger.info("Profile picture name: " + pictureName);
         String picturePath = Paths
                 .get(profilePictureSettings.getPathContainer(), pictureName).toString();
         Path path = Paths.get(picturePath);
         logger.info("Path: " + picturePath);
         
         if(!Files.exists(path)) {
             logger.warning("Picture file not found at path: " + picturePath);
             return ResponseEntity.notFound().build();
         }
         
         try {
             byte[] pictureBytes = Files.readAllBytes(path);
             Resource pictureResource = new ByteArrayResource(pictureBytes);
             logger.info("Picture file read successfully from path: " + picturePath);

             String contentType = Files.probeContentType(path);
             if (contentType == null) {
                 contentType = "application/octet-stream"; // Default if content type could not be determined
                 logger.info("Content type could not be determined, defaulting to application/octet-stream");
             } else {
                 logger.info("Content type determined: " + contentType);
             }

             HttpHeaders headers = new HttpHeaders();
             headers.add(HttpHeaders.CONTENT_TYPE, contentType);
             
             return ResponseEntity.ok()
                     .headers(headers)
                     .contentLength(pictureBytes.length)
                     .body(pictureResource);
         }
         catch (IOException e) {
             logger.severe("Failed to read picture file: " + e.getMessage());
             return ResponseEntity.internalServerError().build();
         }
    }
    
    @GetMapping("/logo/{pictureName}")
    public ResponseEntity<Resource> getLogo(@PathVariable String pictureName){
         String picturePath = Paths.get(profilePictureSettings.getPathContainer(), "logo", pictureName).toString();
         Path path = Paths.get(picturePath);

        if(!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            byte[] pictureBytes = Files.readAllBytes(path);
            Resource pictureResource = new ByteArrayResource(pictureBytes);
            
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Default if content type could not be determined
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pictureBytes.length)
                    .body(pictureResource);
            
        } catch (IOException e) {return ResponseEntity.internalServerError().build();}
    }
}
