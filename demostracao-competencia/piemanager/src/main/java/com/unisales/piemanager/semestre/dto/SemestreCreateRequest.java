package com.unisales.piemanager.semestre.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class SemestreCreateRequest {

    @NotBlank(message = "nome is required")
    @Size(max = 120, message = "nome must have up to 120 chars")
    private String nome;

    @NotNull(message = "dataInicio is required")
    private LocalDate dataInicio;

    @NotNull(message = "dataFim is required")
    private LocalDate dataFim;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
}
