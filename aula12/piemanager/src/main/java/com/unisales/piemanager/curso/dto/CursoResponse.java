package com.unisales.piemanager.curso.dto;

import com.unisales.piemanager.user.model.Profile;
import java.time.Instant;
import java.util.List;

public class CursoResponse {
    private Long id;
    private String nome;
    private UserSummary coordenador;
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

    public UserSummary getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(UserSummary coordenador) {
        this.coordenador = coordenador;
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

    public static class UserSummary {
        private Long id;
        private String username;
        private String email;
        private Profile profile;

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

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }
    }
}
