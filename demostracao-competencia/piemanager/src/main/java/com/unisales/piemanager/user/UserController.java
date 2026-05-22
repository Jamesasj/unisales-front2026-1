package com.unisales.piemanager.user;

import com.unisales.piemanager.common.api.ApiResponse;
import com.unisales.piemanager.user.dto.UserCreateRequest;
import com.unisales.piemanager.user.dto.UserResponse;
import com.unisales.piemanager.user.dto.UserUpdateRequest;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> create(@Valid @RequestBody UserCreateRequest request, Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("User created", userService.create(request, actor));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR')")
    public ApiResponse<List<UserResponse>> findAll() {
        return ApiResponse.success("Users fetched", userService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSOR') or @userService.isOwner(#id, authentication.name)")
    public ApiResponse<UserResponse> findById(@PathVariable Long id) {
        return ApiResponse.success("User fetched", userService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#id, authentication.name)")
    public ApiResponse<UserResponse> update(@PathVariable Long id,
                                            @Valid @RequestBody UserUpdateRequest request,
                                            Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("User updated", userService.update(id, request, actor));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.success("User deleted", null);
    }
}
