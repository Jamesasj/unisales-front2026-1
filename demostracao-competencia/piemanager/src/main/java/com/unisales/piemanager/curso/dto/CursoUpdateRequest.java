package com.unisales.piemanager.curso.dto;

import jakarta.validation.constraints.Size;
import java.util.Set;

public class CursoUpdateRequest {

    @Size(max = 120, message = "nome must have up to 120 chars")
    private String nome;

    private Long coordenadorId;

    private Set<Long> professorIds;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getCoordenadorId() {
        return coordenadorId;
    }

    public void setCoordenadorId(Long coordenadorId) {
        this.coordenadorId = coordenadorId;
    }

    public Set<Long> getProfessorIds() {
        return professorIds;
    }

    public void setProfessorIds(Set<Long> professorIds) {
        this.professorIds = professorIds;
    }
}
