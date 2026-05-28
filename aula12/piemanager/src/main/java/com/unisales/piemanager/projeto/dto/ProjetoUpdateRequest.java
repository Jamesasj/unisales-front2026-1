package com.unisales.piemanager.projeto.dto;

import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

public class ProjetoUpdateRequest {

    @Size(max = 160, message = "nome must have up to 160 chars")
    private String nome;

    @Size(max = 2000, message = "descricao must have up to 2000 chars")
    private String descricao;

    private Long turmaId;

    private Long semestreId;

    private Long professorOrientadorId;

    private Set<Long> integranteIds;

    private Long localId;

    private Instant horarioInicio;

    private Instant horarioFim;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public Long getSemestreId() {
        return semestreId;
    }

    public void setSemestreId(Long semestreId) {
        this.semestreId = semestreId;
    }

    public Long getProfessorOrientadorId() {
        return professorOrientadorId;
    }

    public void setProfessorOrientadorId(Long professorOrientadorId) {
        this.professorOrientadorId = professorOrientadorId;
    }

    public Set<Long> getIntegranteIds() {
        return integranteIds;
    }

    public void setIntegranteIds(Set<Long> integranteIds) {
        this.integranteIds = integranteIds;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public Instant getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(Instant horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public Instant getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(Instant horarioFim) {
        this.horarioFim = horarioFim;
    }
}
