package com.unisales.piemanager.user;

import com.unisales.piemanager.common.exception.BusinessException;
import com.unisales.piemanager.common.exception.ResourceNotFoundException;
import com.unisales.piemanager.user.dto.UserCreateRequest;
import com.unisales.piemanager.user.dto.UserResponse;
import com.unisales.piemanager.user.dto.UserUpdateRequest;
import com.unisales.piemanager.user.model.User;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse create(UserCreateRequest request, String actor) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BusinessException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProfile(request.getProfile());
        user.setCreatedBy(defaultActor(actor));
        user.setUpdatedBy(defaultActor(actor));

        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return toResponse(getEntityById(id));
    }

    @Transactional(readOnly = true)
    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request, String actor) {
        User user = getEntityById(id);

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername().trim());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getProfile() != null) {
            user.setProfile(request.getProfile());
        }

        user.setUpdatedBy(defaultActor(actor));
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        User user = getEntityById(id);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Long id, String email) {
        return userRepository.findById(id)
                .map(user -> user.getEmail().equalsIgnoreCase(email))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        response.setCreatedBy(user.getCreatedBy());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setUpdatedBy(user.getUpdatedBy());
        response.setProfile(user.getProfile());
        return response;
    }

    private User getEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private String defaultActor(String actor) {
        return (actor == null || actor.isBlank()) ? "system" : actor;
    }
}
