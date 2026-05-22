package com.unisales.piemanager.projeto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

public class ProjetoCreateRequest {

    @NotBlank(message = "nome is required")
    @Size(max = 160, message = "nome must have up to 160 chars")
    private String nome;

    @NotBlank(message = "descricao is required")
    @Size(max = 2000, message = "descricao must have up to 2000 chars")
    private String descricao;

    @NotNull(message = "turmaId is required")
    private Long turmaId;

    @NotNull(message = "semestreId is required")
    private Long semestreId;

    @NotNull(message = "professorOrientadorId is required")
    private Long professorOrientadorId;

    @NotEmpty(message = "integranteIds is required")
    private Set<Long> integranteIds;

    @NotNull(message = "localId is required")
    private Long localId;

    @NotNull(message = "horarioInicio is required")
    private Instant horarioInicio;

    @NotNull(message = "horarioFim is required")
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
