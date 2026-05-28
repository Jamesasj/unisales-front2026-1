package com.unisales.piemanager.user.dto;

import com.unisales.piemanager.user.model.Profile;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest {

    @Size(max = 120, message = "username must have up to 120 chars")
    private String username;

    @Size(min = 6, max = 120, message = "password must be between 6 and 120 chars")
    private String password;

    private Profile profile;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
