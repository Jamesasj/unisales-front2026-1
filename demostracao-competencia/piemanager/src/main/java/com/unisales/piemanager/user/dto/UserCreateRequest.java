package com.unisales.piemanager.user.dto;

import com.unisales.piemanager.user.model.Profile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserCreateRequest {

    @NotBlank(message = "username is required")
    @Size(max = 120, message = "username must have up to 120 chars")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Size(max = 180, message = "email must have up to 180 chars")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 120, message = "password must be between 6 and 120 chars")
    private String password;

    @NotNull(message = "profile is required")
    private Profile profile;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
