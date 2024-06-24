package com.hydraxon91.backend.controllers.stylecontroller;

import com.hydraxon91.backend.models.StyleModel.StyleModel;
import com.hydraxon91.backend.models.StyleModel.StyleUpdateForm;
import com.hydraxon91.backend.repositories.StyleRepository.IStyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/style")
public class StyleController {
    @Autowired
    private IStyleRepository styleRepository;
    
    @GetMapping
    public ResponseEntity<StyleModel> getStyles(){
        StyleModel styles = styleRepository.getStyles();
        return ResponseEntity.ok(styles);
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateStyles(@ModelAttribute StyleUpdateForm styleUpdateForm) {
        if (styleUpdateForm.getStyleModel() == null) {
            return ResponseEntity.badRequest().body("Invalid request. StyleModel object is null.");
        }
        try {
            styleRepository.updateStyles(styleUpdateForm.getStyleModel(), styleUpdateForm.getLogoPictureFile());
            return ResponseEntity.ok().body("StyleContext updated successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while updating the StyleContext: " + ex.getMessage());
        }
    }

}
