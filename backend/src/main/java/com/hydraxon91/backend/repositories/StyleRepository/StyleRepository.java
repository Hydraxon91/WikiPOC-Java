package com.hydraxon91.backend.repositories.StyleRepository;

import com.hydraxon91.backend.config.ProfilePictureSettings;
import com.hydraxon91.backend.models.StyleModel.StyleModel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Repository
public class StyleRepository implements IStyleRepository{
    @Autowired
    private StyleJpaRepository styleJpaRepository;

    @Autowired
    private ProfilePictureSettings profilePictureSettings;

    @Override
    @Transactional
    public StyleModel getStyles() {
        return styleJpaRepository.findFirstByOrderById();
    }

    @Override
    @Transactional
    public void updateStyles(StyleModel updatedStyles, MultipartFile logoPictureFile) {
        StyleModel existingStyles = styleJpaRepository.findFirstByOrderById();
        if (existingStyles != null) {
            if (logoPictureFile != null && !logoPictureFile.isEmpty()) {
                try {
                    String fileName = "logo/logo" + logoPictureFile.getOriginalFilename().substring(logoPictureFile.getOriginalFilename().lastIndexOf('.'));
                    File file = new File(profilePictureSettings.getPathContainer(), fileName);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(logoPictureFile.getBytes());
                    }
                    updatedStyles.setLogo(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to store logo file", e);
                }
            }
            existingStyles.setArticleColor(updatedStyles.getArticleColor());
            existingStyles.setBodyColor(updatedStyles.getBodyColor());
            existingStyles.setArticleRightColor(updatedStyles.getArticleRightColor());
            existingStyles.setArticleRightInnerColor(updatedStyles.getArticleRightInnerColor());
            existingStyles.setWikiName(updatedStyles.getWikiName());
            existingStyles.setFooterListLinkTextColor(updatedStyles.getFooterListLinkTextColor());
            existingStyles.setFooterListTextColor(updatedStyles.getFooterListTextColor());
            existingStyles.setFontFamily(updatedStyles.getFontFamily());
            if (updatedStyles.getLogo() != null) {
                existingStyles.setLogo(updatedStyles.getLogo());
            }
            styleJpaRepository.save(existingStyles);
        } else {
            styleJpaRepository.save(updatedStyles);
        }
    }

    }
