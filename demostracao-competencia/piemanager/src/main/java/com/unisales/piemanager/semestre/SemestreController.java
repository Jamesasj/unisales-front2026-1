package com.unisales.piemanager.semestre;

import com.unisales.piemanager.common.api.ApiResponse;
import com.unisales.piemanager.semestre.dto.SemestreCreateRequest;
import com.unisales.piemanager.semestre.dto.SemestreResponse;
import com.unisales.piemanager.semestre.dto.SemestreUpdateRequest;
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
@RequestMapping("/api/semestres")
public class SemestreController {

    private final SemestreService semestreService;

    public SemestreController(SemestreService semestreService) {
        this.semestreService = semestreService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SemestreResponse> create(@Valid @RequestBody SemestreCreateRequest request,
                                                Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Semestre created", semestreService.create(request, actor));
    }

    @GetMapping
    public ApiResponse<List<SemestreResponse>> findAll() {
        return ApiResponse.success("Semestres fetched", semestreService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<SemestreResponse> findById(@PathVariable Long id) {
        return ApiResponse.success("Semestre fetched", semestreService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SemestreResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody SemestreUpdateRequest request,
                                                Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Semestre updated", semestreService.update(id, request, actor));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        semestreService.delete(id);
        return ApiResponse.success("Semestre deleted", null);
    }
}
