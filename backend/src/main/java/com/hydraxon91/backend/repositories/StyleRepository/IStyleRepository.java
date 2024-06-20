package com.hydraxon91.backend.repositories.StyleRepository;

import com.hydraxon91.backend.models.StyleModel.StyleModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface IStyleRepository {
    StyleModel getStyles();
    void updateStyles(StyleModel updatedStyles, MultipartFile logoPictureFile);
}
