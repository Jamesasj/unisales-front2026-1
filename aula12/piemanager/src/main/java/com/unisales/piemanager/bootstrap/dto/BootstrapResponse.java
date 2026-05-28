package com.unisales.piemanager.bootstrap.dto;

import com.unisales.piemanager.user.dto.UserResponse;

public class BootstrapResponse {
    private boolean adminCreated;
    private UserResponse adminUser;
    private int usersSeeded;
    private int semestresSeeded;
    private int cursosSeeded;
    private int disciplinasSeeded;
    private int turmasSeeded;
    private int locaisSeeded;
    private int projetosSeeded;
    private int avaliacoesSeeded;

    public BootstrapResponse(boolean adminCreated,
                             UserResponse adminUser,
                             int usersSeeded,
                             int semestresSeeded,
                             int cursosSeeded,
                             int disciplinasSeeded,
                             int turmasSeeded,
                             int locaisSeeded,
                             int projetosSeeded,
                             int avaliacoesSeeded) {
        this.adminCreated = adminCreated;
        this.adminUser = adminUser;
        this.usersSeeded = usersSeeded;
        this.semestresSeeded = semestresSeeded;
        this.cursosSeeded = cursosSeeded;
        this.disciplinasSeeded = disciplinasSeeded;
        this.turmasSeeded = turmasSeeded;
        this.locaisSeeded = locaisSeeded;
        this.projetosSeeded = projetosSeeded;
        this.avaliacoesSeeded = avaliacoesSeeded;
    }

    public boolean isAdminCreated() {
        return adminCreated;
    }

    public UserResponse getAdminUser() {
        return adminUser;
    }

    public int getUsersSeeded() {
        return usersSeeded;
    }

    public int getSemestresSeeded() {
        return semestresSeeded;
    }

    public int getCursosSeeded() {
        return cursosSeeded;
    }

    public int getDisciplinasSeeded() {
        return disciplinasSeeded;
    }

    public int getTurmasSeeded() {
        return turmasSeeded;
    }

    public int getLocaisSeeded() {
        return locaisSeeded;
    }

    public int getProjetosSeeded() {
        return projetosSeeded;
    }

    public int getAvaliacoesSeeded() {
        return avaliacoesSeeded;
    }
}
