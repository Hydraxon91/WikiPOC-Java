package com.hydraxon91.backend.services.Authentication;

import com.hydraxon91.backend.models.UserModels.ApplicationUser;
import com.hydraxon91.backend.models.UserModels.Role;
import com.hydraxon91.backend.models.UserModels.UserProfile;
import com.hydraxon91.backend.repositories.UserRepository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AuthService implements IAuthService{
    
    private final UserRepository _userRepository;
    private final PasswordEncoder _passwordEncoder;
    private final ITokenServices _tokenServices;
    
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this._passwordEncoder = passwordEncoder;
        this._userRepository = userRepository;
        this._tokenServices = tokenService;
    }

    @Override
    public CompletableFuture<AuthResult> registerAsync(String email, String username, String password) {
        
        return CompletableFuture.supplyAsync(() -> {
            // Check if userName or email is already taken
            if(_userRepository.findByUsername(username).isPresent()){
                AuthResult result = new AuthResult(false, email, username, null);
                result.addErrorMessage("username", "Username already exists");
                return result;
            }
            if (_userRepository.findByEmail(email).isPresent()) {
                AuthResult result = new AuthResult(false, email, username, null);
                result.addErrorMessage("email", "Email already registered");
                return result;
            }

            // Create new ApplicationUser
            ApplicationUser newUser = new ApplicationUser();
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setPassword(_passwordEncoder.encode(password));
            
            // Create UserProfile
            UserProfile userProfile = new UserProfile(newUser.getId().toString(), username, "base_picture");
            userProfile.setUser(newUser);
            
            // Set UserProfile to ApplicationUser
            newUser.setProfile(userProfile);
            
            try {
                // Save to Db
                ApplicationUser savedUser = _userRepository.save(newUser);

                // Generate JWT Token 
                Role role = new Role();
                role.setName("ROLE_USER");
                String token = _tokenServices.createToken(savedUser, role);

                // Create and return AuthResult
                AuthResult result = new AuthResult(true, savedUser.getEmail(), savedUser.getUsername(), token);
                return result;
            } catch (DataAccessException ex) {
                // Handle database exception
                AuthResult result = new AuthResult(false, email, username, null);
                result.addErrorMessage("database", "Failed to register user");
                return result;
            }
            
        });
    }
    
    @Override
    public CompletableFuture<AuthResult> loginAsync(String usernameOrEmail, String password) { 
        return CompletableFuture.supplyAsync(() ->{
            // Retrieve user by username or email
            ApplicationUser user = _userRepository.findByUsername(usernameOrEmail)
                    .orElse(_userRepository.findByEmail(usernameOrEmail)
                            .orElseThrow(() -> new RuntimeException("User not found")));
            
            // Validate Password
            if (!_passwordEncoder.matches(password, user.getPassword())) {
                AuthResult result = new AuthResult(false, null, usernameOrEmail, null);
                result.addErrorMessage("password", "Incorrect password");
                return result;
            }

            // Get roles associated with the user
            Role role = user.getRole();
            
            // Generate JWT token
            String token = _tokenServices.createToken(user, role);
            
            // Create and Return AuthResult
            AuthResult result = new AuthResult(true, user.getEmail(), user.getUsername(), token);
            return result;
        });
    }
}
