package com.hydraxon91.backend.controllers.authcontroller;

import com.hydraxon91.backend.models.UserModels.ApplicationUser;
import com.hydraxon91.backend.repositories.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    
    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<ApplicationUser>> getAllUsers(){
        List<ApplicationUser> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationUser> getUserById(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ApplicationUser> createUser(@RequestBody ApplicationUser user) {
        ApplicationUser createdUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationUser> updateUser(@PathVariable UUID id, @RequestBody ApplicationUser user) {
        if (!userRepository.existsById(id)) {return ResponseEntity.notFound().build();}
        user.setId(id);
        ApplicationUser updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {
        if (!userRepository.existsById(id)) {return ResponseEntity.notFound().build();}
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
