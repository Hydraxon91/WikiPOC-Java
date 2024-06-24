package com.hydraxon91.backend.services.Authentication;

import com.hydraxon91.backend.models.UserModels.ApplicationUser;
import com.hydraxon91.backend.models.UserModels.Role;
import com.hydraxon91.backend.models.UserModels.UserProfile;
import com.hydraxon91.backend.repositories.RoleRepository.RoleRepository;
import com.hydraxon91.backend.repositories.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthService implements IAuthService{
    
    private final UserRepository _userRepository;
    private final RoleRepository _roleRepository;
    private final PasswordEncoder _passwordEncoder;
    private final ITokenServices _tokenServices;

    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this._passwordEncoder = passwordEncoder;
        this._userRepository = userRepository;
        this._roleRepository = roleRepository;
        this._tokenServices = tokenService;
    }

    @Override
    public CompletableFuture<AuthResult> registerAsync(String email, String username, String password) {

        return CompletableFuture.supplyAsync(() -> {
            // Check if userName or email is already taken
            if (_userRepository.findByUsername(username).isPresent()) {
                AuthResult result = new AuthResult(false, email, username, null);
                result.addErrorMessage("username", "Username already exists");
                return result;
            }
            if (_userRepository.findByEmail(email).isPresent()) {
                AuthResult result = new AuthResult(false, email, username, null);
                result.addErrorMessage("email", "Email already registered");
                return result;
            }

            try {
                // Create new ApplicationUser
                ApplicationUser newUser = new ApplicationUser(email, username, _passwordEncoder.encode(password));

                // Assign ROLE_USER to the user
                Optional<Role> userRoleOptional = _roleRepository.findByName("USER");
                if (userRoleOptional.isEmpty()) {
                    AuthResult result = new AuthResult(false, email, username, null);
                    result.addErrorMessage("role", "Role 'USER' not found");
                    return result;
                }
                Role userRole = userRoleOptional.get();
                newUser.setRole(userRole);

                // Save ApplicationUser to Db
                ApplicationUser savedUser = _userRepository.save(newUser);

                // Create UserProfile
                UserProfile userProfile = new UserProfile(username, "base_picture");
                userProfile.setUser(savedUser);
                userProfile.setUserId(savedUser.getId());

                // Set UserProfile to ApplicationUser
                savedUser.setProfile(userProfile);

                // Save the updated savedUser (with profile) back to the database
                ApplicationUser updatedUser = _userRepository.save(savedUser);

                // Generate JWT Token
                String token = _tokenServices.createToken(updatedUser, userRole);

                // Create and return AuthResult
                AuthResult result = new AuthResult(true, updatedUser.getEmail(), updatedUser.getUsername(), token);
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
                    .orElseGet(() -> _userRepository.findByEmail(usernameOrEmail)
                            .orElse(null)); // Return null if user not found

            if (user == null) {
                AuthResult result = new AuthResult(false, null, usernameOrEmail, null);
                result.addErrorMessage("usernameOrEmail", "User not found");
                return result;
            }
            
            // Validate Password
            if (!_passwordEncoder.matches(password, user.getPassword())) {
                AuthResult result = new AuthResult(false, null, usernameOrEmail, null);
                result.addErrorMessage("password", "Incorrect password");
                return result;
            }

            // Get roles associated with the user
            Role role = user.getRole();
            if (role == null) {
                role = _roleRepository.findByName("USER").get();
            }
            
            // Generate JWT token
            String token = _tokenServices.createToken(user, role);
            
            // Create and Return AuthResult
            AuthResult result = new AuthResult(true, user.getEmail(), user.getUsername(), token);
            return result;
        });
    }
}
