package com.hydraxon91.backend.services.Authentication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuthResult {
    private final boolean success;
    private final String email;
    private final String username;
    private final String token;
    private final Map<String, String> errorMessages;
    
    public AuthResult(boolean success, String email, String username, String token) {
        this.success = success;
        this.email = email;
        this.username = username;
        this.token = token;
        this.errorMessages = new HashMap<>();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Map<String, String> getErrorMessages() {
        return Collections.unmodifiableMap(errorMessages);
    }

    public void addErrorMessage(String key, String message) {
        errorMessages.put(key, message);
    }
}
