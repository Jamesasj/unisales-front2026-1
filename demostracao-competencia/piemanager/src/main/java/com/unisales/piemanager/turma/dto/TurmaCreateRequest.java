package com.unisales.piemanager.turma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class TurmaCreateRequest {

    @NotBlank(message = "nome is required")
    @Size(max = 120, message = "nome must have up to 120 chars")
    private String nome;

    @NotEmpty(message = "cursoIds is required")
    private Set<Long> cursoIds;

    @NotNull(message = "disciplinaId is required")
    private Long disciplinaId;

    @NotNull(message = "semestreId is required")
    private Long semestreId;

    @NotEmpty(message = "professorIds is required")
    private Set<Long> professorIds;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Long> getCursoIds() {
        return cursoIds;
    }

    public void setCursoIds(Set<Long> cursoIds) {
        this.cursoIds = cursoIds;
    }

    public Long getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public Long getSemestreId() {
        return semestreId;
    }

    public void setSemestreId(Long semestreId) {
        this.semestreId = semestreId;
    }

    public Set<Long> getProfessorIds() {
        return professorIds;
    }

    public void setProfessorIds(Set<Long> professorIds) {
        this.professorIds = professorIds;
    }
}
