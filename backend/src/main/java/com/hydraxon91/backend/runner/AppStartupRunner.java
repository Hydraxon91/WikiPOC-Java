package com.hydraxon91.backend.runner;

import com.hydraxon91.backend.config.ConfigLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner{
    
    private final ConfigLogger configLogger;
    
    @Autowired
    public AppStartupRunner(ConfigLogger configLogger) {
        this.configLogger = configLogger;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Log properties during application startup
        configLogger.logProperties();
    }
}
