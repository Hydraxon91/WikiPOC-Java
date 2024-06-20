package com.hydraxon91.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pictures")
public class ProfilePictureSettings {
    private String path;
    private String pathContainer;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathContainer() {
        return pathContainer;
    }

    public void setPathContainer(String pathContainer) {
        this.pathContainer = pathContainer;
    }
    
}
