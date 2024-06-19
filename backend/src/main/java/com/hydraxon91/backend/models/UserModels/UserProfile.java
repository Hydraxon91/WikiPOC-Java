package com.hydraxon91.backend.models.UserModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonIgnore
    @Column(name = "user_id")
    private UUID userId;

    private String userName;
    private String profilePicture;
    private String displayName;

    @JsonIgnore
    @OneToOne(mappedBy = "userProfile")
    private ApplicationUser user;

    // Required no-argument constructor
    public UserProfile() {
        // No specific initialization needed for a no-arg constructor
    }

    public UserProfile(String userName, String profilePicture) {
        this.userName = userName;
        this.profilePicture = profilePicture;
        this.displayName = userName;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }
}
