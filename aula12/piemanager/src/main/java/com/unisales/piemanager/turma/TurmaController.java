package com.unisales.piemanager.turma;

import com.unisales.piemanager.common.api.ApiResponse;
import com.unisales.piemanager.turma.dto.TurmaAlunoRequest;
import com.unisales.piemanager.turma.dto.TurmaCreateRequest;
import com.unisales.piemanager.turma.dto.TurmaResponse;
import com.unisales.piemanager.turma.dto.TurmaUpdateRequest;
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
@RequestMapping("/api/turmas")
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public ApiResponse<TurmaResponse> create(@Valid @RequestBody TurmaCreateRequest request,
                                             Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Turma created", turmaService.create(request, actor));
    }

    @GetMapping
    public ApiResponse<List<TurmaResponse>> findAll() {
        return ApiResponse.success("Turmas fetched", turmaService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<TurmaResponse> findById(@PathVariable Long id) {
        return ApiResponse.success("Turma fetched", turmaService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ApiResponse<TurmaResponse> update(@PathVariable Long id,
                                             @Valid @RequestBody TurmaUpdateRequest request,
                                             Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Turma updated", turmaService.update(id, request, actor));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROFESSOR','ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        turmaService.delete(id);
        return ApiResponse.success("Turma deleted", null);
    }

    @PostMapping("/{id}/matriculas")
    @PreAuthorize("hasRole('ALUNO')")
    public ApiResponse<TurmaResponse> matricular(@PathVariable Long id, Authentication authentication) {
        String email = authentication != null ? authentication.getName() : "";
        return ApiResponse.success("Matricula created", turmaService.matricularAluno(id, email));
    }

    @PostMapping("/{id}/alunos")
    @PreAuthorize("hasAnyRole('PROFESSOR','COORDENADOR','ADMIN')")
    public ApiResponse<TurmaResponse> addAluno(@PathVariable Long id,
                                               @Valid @RequestBody TurmaAlunoRequest request,
                                               Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Aluno added to turma", turmaService.addAluno(id, request.getAlunoId(), actor));
    }

    @DeleteMapping("/{id}/alunos/{alunoId}")
    @PreAuthorize("hasAnyRole('PROFESSOR','COORDENADOR','ADMIN')")
    public ApiResponse<TurmaResponse> removeAluno(@PathVariable Long id,
                                                  @PathVariable Long alunoId,
                                                  Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "system";
        return ApiResponse.success("Aluno removed from turma", turmaService.removeAluno(id, alunoId, actor));
    }
}
