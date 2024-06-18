package com.hydraxon91.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class ConfigLogger {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLogger.class);
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    @Value("${jwt.issuer-signing-key}")
    private String jwtIssuerSigningKey;

    // Add more @Value annotations for other properties you want to log

    // Constructor (optional if you prefer field injection)
    public ConfigLogger() {
        // Empty constructor
    }

    // Method to log the properties during application startup
    public void logProperties() {
        logger.info("spring.datasource.url: {}", dataSourceUrl);
        logger.info("spring.datasource.username: {}", dataSourceUsername);
        logger.info("spring.datasource.password: {}", dataSourcePassword);
        logger.info("jwt.issuer-signing-key: {}", jwtIssuerSigningKey);
    }
    
}
