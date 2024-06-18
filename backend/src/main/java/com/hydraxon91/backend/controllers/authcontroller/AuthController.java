package com.hydraxon91.backend.controllers.authcontroller;

import com.hydraxon91.backend.services.Authentication.AuthResult;
import com.hydraxon91.backend.services.Authentication.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/public/auth")
public class AuthController {
    private final IAuthService authService;

    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<AuthResult>> register(@RequestParam String email, @RequestParam String username, @RequestParam String password) {
        return authService.registerAsync(email, username, password)
                .thenApply(authResult -> {
                    if (authResult.isSuccess()) {
                        return ResponseEntity.ok(authResult);
                    } else {
                        return ResponseEntity.badRequest().body(authResult);
                    }
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<AuthResult>> login(@RequestParam String usernameOrEmail, @RequestParam String password) {
        return authService.loginAsync(usernameOrEmail, password)
                .thenApply(authResult -> {
                    if (authResult.isSuccess()) {
                        return ResponseEntity.ok(authResult);
                    } else {
                        return ResponseEntity.badRequest().body(authResult);
                    }
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
