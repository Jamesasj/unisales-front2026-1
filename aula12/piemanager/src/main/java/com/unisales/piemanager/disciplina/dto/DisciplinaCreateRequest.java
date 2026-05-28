package com.unisales.piemanager.disciplina.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DisciplinaCreateRequest {

    @NotBlank(message = "nome is required")
    @Size(max = 120, message = "nome must have up to 120 chars")
    private String nome;

    @NotNull(message = "cursoId is required")
    private Long cursoId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }
}
