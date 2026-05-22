package com.unisales.piemanager.local;

import com.unisales.piemanager.common.api.ApiResponse;
import com.unisales.piemanager.local.dto.LocalCreateRequest;
import com.unisales.piemanager.local.dto.LocalResponse;
import com.unisales.piemanager.local.dto.LocalUpdateRequest;
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
@RequestMapping("/api/locais")
public class LocalController {

    private final LocalService localService;

    public LocalController(LocalService localService) {
        this.localService = localService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','COORDENADOR')")
    public ApiResponse<LocalResponse> create(@Valid @RequestBody LocalCreateRequest request,
                                             Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Local created", localService.create(request, actor));
    }

    @GetMapping
    public ApiResponse<List<LocalResponse>> findAll() {
        return ApiResponse.success("Locais fetched", localService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<LocalResponse> findById(@PathVariable Long id) {
        return ApiResponse.success("Local fetched", localService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','COORDENADOR')")
    public ApiResponse<LocalResponse> update(@PathVariable Long id,
                                             @Valid @RequestBody LocalUpdateRequest request,
                                             Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Local updated", localService.update(id, request, actor));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','COORDENADOR')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        localService.delete(id);
        return ApiResponse.success("Local deleted", null);
    }
}
