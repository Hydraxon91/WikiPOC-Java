package com.hydraxon91.backend.services.Authentication;

import java.util.concurrent.CompletableFuture;

public interface IAutchService {
    CompletableFuture<AuthResult> registerAsync(String email, String username, String password);
    CompletableFuture<AuthResult> loginAsync(String username, String password);
}
