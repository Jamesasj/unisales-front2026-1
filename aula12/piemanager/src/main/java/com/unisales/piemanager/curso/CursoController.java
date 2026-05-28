package com.unisales.piemanager.curso;

import com.unisales.piemanager.common.api.ApiResponse;
import com.unisales.piemanager.curso.dto.CursoCreateRequest;
import com.unisales.piemanager.curso.dto.CursoResponse;
import com.unisales.piemanager.curso.dto.CursoUpdateRequest;
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
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CursoResponse> create(@Valid @RequestBody CursoCreateRequest request,
                                             Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Curso created", cursoService.create(request, actor));
    }

    @GetMapping
    public ApiResponse<List<CursoResponse>> findAll() {
        return ApiResponse.success("Cursos fetched", cursoService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<CursoResponse> findById(@PathVariable Long id) {
        return ApiResponse.success("Curso fetched", cursoService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CursoResponse> update(@PathVariable Long id,
                                             @Valid @RequestBody CursoUpdateRequest request,
                                             Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Curso updated", cursoService.update(id, request, actor));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        cursoService.delete(id);
        return ApiResponse.success("Curso deleted", null);
    }
}
