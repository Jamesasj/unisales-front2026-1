package com.unisales.piemanager.disciplina;

import com.unisales.piemanager.common.api.ApiResponse;
import com.unisales.piemanager.disciplina.dto.DisciplinaCreateRequest;
import com.unisales.piemanager.disciplina.dto.DisciplinaResponse;
import com.unisales.piemanager.disciplina.dto.DisciplinaUpdateRequest;
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
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DisciplinaResponse> create(@Valid @RequestBody DisciplinaCreateRequest request,
                                                  Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Disciplina created", disciplinaService.create(request, actor));
    }

    @GetMapping
    public ApiResponse<List<DisciplinaResponse>> findAll() {
        return ApiResponse.success("Disciplinas fetched", disciplinaService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<DisciplinaResponse> findById(@PathVariable Long id) {
        return ApiResponse.success("Disciplina fetched", disciplinaService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DisciplinaResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody DisciplinaUpdateRequest request,
                                                  Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Disciplina updated", disciplinaService.update(id, request, actor));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        disciplinaService.delete(id);
        return ApiResponse.success("Disciplina deleted", null);
    }
}
