package com.unisales.piemanager.projeto.dto;

import jakarta.validation.constraints.NotNull;

public class ProjetoIntegranteRequest {

    @NotNull(message = "alunoId is required")
    private Long alunoId;

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }
}
