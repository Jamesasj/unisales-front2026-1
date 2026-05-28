package com.unisales.piemanager.turma.dto;

import java.time.Instant;
import java.util.List;

public class TurmaResponse {
    private Long id;
    private String nome;
    private List<IdNome> cursos;
    private IdNome disciplina;
    private IdNome semestre;
    private List<UserSummary> alunos;
    private List<UserSummary> professores;
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

    public List<IdNome> getCursos() {
        return cursos;
    }

    public void setCursos(List<IdNome> cursos) {
        this.cursos = cursos;
    }

    public IdNome getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(IdNome disciplina) {
        this.disciplina = disciplina;
    }

    public IdNome getSemestre() {
        return semestre;
    }

    public void setSemestre(IdNome semestre) {
        this.semestre = semestre;
    }

    public List<UserSummary> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<UserSummary> alunos) {
        this.alunos = alunos;
    }

    public List<UserSummary> getProfessores() {
        return professores;
    }

    public void setProfessores(List<UserSummary> professores) {
        this.professores = professores;
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
