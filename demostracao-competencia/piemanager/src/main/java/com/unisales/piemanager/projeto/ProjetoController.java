package com.unisales.piemanager.projeto;

import com.unisales.piemanager.avaliacao.AvaliacaoService;
import com.unisales.piemanager.avaliacao.dto.AvaliacaoResponse;
import com.unisales.piemanager.avaliacao.dto.ProjetoAvaliacaoCreateRequest;
import com.unisales.piemanager.avaliacao.dto.ProjetoAvaliacaoUpdateRequest;
import com.unisales.piemanager.common.api.ApiResponse;
import com.unisales.piemanager.projeto.dto.ProjetoCreateRequest;
import com.unisales.piemanager.projeto.dto.ProjetoIntegranteRequest;
import com.unisales.piemanager.projeto.dto.ProjetoResponse;
import com.unisales.piemanager.projeto.dto.ProjetoUpdateRequest;
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
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;
    private final AvaliacaoService avaliacaoService;

    public ProjetoController(ProjetoService projetoService, AvaliacaoService avaliacaoService) {
        this.projetoService = projetoService;
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ALUNO','PROFESSOR','COORDENADOR','ADMIN')")
    public ApiResponse<ProjetoResponse> create(@Valid @RequestBody ProjetoCreateRequest request,
                                               Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Projeto created", projetoService.create(request, actor));
    }

    @GetMapping
    public ApiResponse<List<ProjetoResponse>> findAll(@RequestParam(required = false) Long turmaId,
                                                      @RequestParam(required = false) Long semestreId,
                                                      @RequestParam(required = false) Long localId) {
        return ApiResponse.success("Projetos fetched", projetoService.findAll(turmaId, semestreId, localId));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProjetoResponse> findById(@PathVariable Long id) {
        return ApiResponse.success("Projeto fetched", projetoService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COORDENADOR','ADMIN') or " +
            "(hasRole('PROFESSOR') and @projetoService.isProfessorInProjectTurma(#id, authentication.name)) or " +
            "(hasRole('ALUNO') and @projetoService.isAlunoIntegrante(#id, authentication.name))")
    public ApiResponse<ProjetoResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody ProjetoUpdateRequest request,
                                               Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Projeto updated", projetoService.update(id, request, actor));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('COORDENADOR','ADMIN') or " +
            "(hasRole('PROFESSOR') and @projetoService.isProfessorInProjectTurma(#id, authentication.name)) or " +
            "(hasRole('ALUNO') and @projetoService.isAlunoIntegrante(#id, authentication.name))")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        projetoService.delete(id);
        return ApiResponse.success("Projeto deleted", null);
    }

    @PostMapping("/{id}/integrantes/me")
    @PreAuthorize("hasRole('ALUNO')")
    public ApiResponse<ProjetoResponse> addMe(@PathVariable Long id, Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "";
        return ApiResponse.success("Integrante added", projetoService.addIntegranteMe(id, actor));
    }

    @DeleteMapping("/{id}/integrantes/me")
    @PreAuthorize("hasRole('ALUNO')")
    public ApiResponse<ProjetoResponse> removeMe(@PathVariable Long id, Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "";
        return ApiResponse.success("Integrante removed", projetoService.removeIntegranteMe(id, actor));
    }

    @PostMapping("/{id}/integrantes")
    @PreAuthorize("hasAnyRole('COORDENADOR','ADMIN') or " +
            "(hasRole('PROFESSOR') and @projetoService.isProfessorOrientador(#id, authentication.name))")
    public ApiResponse<ProjetoResponse> addIntegrante(@PathVariable Long id,
                                                      @Valid @RequestBody ProjetoIntegranteRequest request,
                                                      Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Integrante added", projetoService.addIntegrante(id, request.getAlunoId(), actor));
    }

    @DeleteMapping("/{id}/integrantes/{alunoId}")
    @PreAuthorize("hasAnyRole('COORDENADOR','ADMIN') or " +
            "(hasRole('PROFESSOR') and @projetoService.isProfessorOrientador(#id, authentication.name))")
    public ApiResponse<ProjetoResponse> removeIntegrante(@PathVariable Long id,
                                                         @PathVariable Long alunoId,
                                                         Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Integrante removed", projetoService.removeIntegrante(id, alunoId, actor));
    }

    @PostMapping("/{id}/avaliacoes")
    @PreAuthorize("hasAnyRole('PROFESSOR','AVALIADOR_EXTERNO')")
    public ApiResponse<AvaliacaoResponse> createAvaliacao(@PathVariable Long id,
                                                          @Valid @RequestBody ProjetoAvaliacaoCreateRequest request,
                                                          Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "";
        return ApiResponse.success("Avaliacao created", avaliacaoService.createByProjeto(id, request, actor));
    }

    @PutMapping("/{id}/avaliacoes/{avaliacaoId}")
    @PreAuthorize("@avaliacaoService.isOwnerAvaliadorByProjeto(#id, #avaliacaoId, authentication.name)")
    public ApiResponse<AvaliacaoResponse> updateAvaliacao(@PathVariable Long id,
                                                          @PathVariable Long avaliacaoId,
                                                          @Valid @RequestBody ProjetoAvaliacaoUpdateRequest request,
                                                          Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "";
        return ApiResponse.success("Avaliacao updated", avaliacaoService.updateByProjeto(id, avaliacaoId, request, actor));
    }

    @DeleteMapping("/{id}/avaliacoes/{avaliacaoId}")
    @PreAuthorize("hasAnyRole('COORDENADOR','ADMIN') or " +
            "@avaliacaoService.isOwnerAvaliadorByProjeto(#id, #avaliacaoId, authentication.name)")
    public ApiResponse<Void> deleteAvaliacao(@PathVariable Long id,
                                             @PathVariable Long avaliacaoId) {
        avaliacaoService.deleteByProjeto(id, avaliacaoId);
        return ApiResponse.success("Avaliacao deleted", null);
    }
}
