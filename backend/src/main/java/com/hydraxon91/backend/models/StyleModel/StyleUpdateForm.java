package com.hydraxon91.backend.models.StyleModel;

import org.springframework.web.multipart.MultipartFile;

public class StyleUpdateForm {
    private StyleModel styleModel;
    private MultipartFile logoPictureFile;

    // Getters and setters
    public StyleModel getStyleModel() {
        return styleModel;
    }

    public void setStyleModel(StyleModel styleModel) {
        this.styleModel = styleModel;
    }

    public MultipartFile getLogoPictureFile() {
        return logoPictureFile;
    }

    public void setLogoPictureFile(MultipartFile logoPictureFile) {
        this.logoPictureFile = logoPictureFile;
    }
}
