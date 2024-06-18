package com.hydraxon91.backend.controllers.authcontroller;

import com.hydraxon91.backend.services.Authentication.AuthResult;
import com.hydraxon91.backend.services.Authentication.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthService authService;
    
    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResult> register(@RequestParam String email, @RequestParam String username, @RequestParam String password) {
        try {
            CompletableFuture<AuthResult> authResultCompletableFuture = authService.registerAsync(email, username, password);
            AuthResult authResult = authResultCompletableFuture.get();
            if (authResult.isSuccess()) {
                return ResponseEntity.ok(authResult);
            }
            else return ResponseEntity.badRequest().body(authResult);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResult> login(@RequestParam String usernameOrEmail, @RequestParam String password) {
        try {
            CompletableFuture<AuthResult> authResultFuture = authService.loginAsync(usernameOrEmail, password);
            AuthResult authResult = authResultFuture.get(); // Block and wait for CompletableFuture to complete
            if (authResult.isSuccess()) {
                return ResponseEntity.ok(authResult);
            } else {
                return ResponseEntity.badRequest().body(authResult);
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
