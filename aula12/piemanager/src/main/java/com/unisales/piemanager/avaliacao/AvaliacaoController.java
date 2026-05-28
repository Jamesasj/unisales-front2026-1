package com.unisales.piemanager.avaliacao;

import com.unisales.piemanager.avaliacao.dto.AvaliacaoCreateRequest;
import com.unisales.piemanager.avaliacao.dto.AvaliacaoResponse;
import com.unisales.piemanager.avaliacao.dto.AvaliacaoUpdateRequest;
import com.unisales.piemanager.common.api.ApiResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PROFESSOR','AVALIADOR_EXTERNO','COORDENADOR','ADMIN')")
    public ApiResponse<AvaliacaoResponse> create(@Valid @RequestBody AvaliacaoCreateRequest request,
                                                 Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Avaliacao created", avaliacaoService.create(request, actor));
    }

    @GetMapping
    public ApiResponse<List<AvaliacaoResponse>> findAll(@RequestParam(required = false) Long projetoId,
                                                        @RequestParam(required = false) Long avaliadorId) {
        return ApiResponse.success("Avaliacoes fetched", avaliacaoService.findAll(projetoId, avaliadorId));
    }

    @GetMapping("/{id}")
    public ApiResponse<AvaliacaoResponse> findById(@PathVariable Long id) {
        return ApiResponse.success("Avaliacao fetched", avaliacaoService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COORDENADOR','ADMIN') or @avaliacaoService.isOwnerAvaliador(#id, authentication.name)")
    public ApiResponse<AvaliacaoResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody AvaliacaoUpdateRequest request,
                                                 Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Avaliacao updated", avaliacaoService.update(id, request, actor));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('COORDENADOR','ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        avaliacaoService.delete(id);
        return ApiResponse.success("Avaliacao deleted", null);
    }
}
