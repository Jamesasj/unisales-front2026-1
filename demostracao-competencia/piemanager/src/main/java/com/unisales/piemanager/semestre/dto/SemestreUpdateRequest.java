package com.unisales.piemanager.semestre.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class SemestreUpdateRequest {

    @Size(max = 120, message = "nome must have up to 120 chars")
    private String nome;

    private LocalDate dataInicio;

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
