package com.unisales.piemanager.projeto.dto;

import java.time.Instant;
import java.util.List;

public class ProjetoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private IdNome turma;
    private IdNome semestre;
    private UserSummary professorOrientador;
    private List<UserSummary> integrantes;
    private IdNome local;
    private Instant horarioInicio;
    private Instant horarioFim;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public IdNome getTurma() {
        return turma;
    }

    public void setTurma(IdNome turma) {
        this.turma = turma;
    }

    public IdNome getSemestre() {
        return semestre;
    }

    public void setSemestre(IdNome semestre) {
        this.semestre = semestre;
    }

    public UserSummary getProfessorOrientador() {
        return professorOrientador;
    }

    public void setProfessorOrientador(UserSummary professorOrientador) {
        this.professorOrientador = professorOrientador;
    }

    public List<UserSummary> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<UserSummary> integrantes) {
        this.integrantes = integrantes;
    }

    public IdNome getLocal() {
        return local;
    }

    public void setLocal(IdNome local) {
        this.local = local;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public static class IdNome {
        private Long id;
        private String nome;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    public static class UserSummary {
        private Long id;
        private String username;
        private String email;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
