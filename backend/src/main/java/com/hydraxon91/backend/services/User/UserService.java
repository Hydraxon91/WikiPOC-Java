package com.hydraxon91.backend.services.User;

import com.hydraxon91.backend.models.UserModels.ApplicationUser;
import com.hydraxon91.backend.repositories.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public ApplicationUser loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
