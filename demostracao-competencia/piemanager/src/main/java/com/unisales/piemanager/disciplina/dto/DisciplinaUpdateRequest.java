package com.unisales.piemanager.disciplina.dto;

import jakarta.validation.constraints.Size;

public class DisciplinaUpdateRequest {

    @Size(max = 120, message = "nome must have up to 120 chars")
    private String nome;

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
