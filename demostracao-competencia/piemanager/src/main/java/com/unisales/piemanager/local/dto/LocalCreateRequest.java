package com.unisales.piemanager.local.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LocalCreateRequest {

    @NotBlank(message = "numero is required")
    @Size(max = 40, message = "numero must have up to 40 chars")
    private String numero;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
