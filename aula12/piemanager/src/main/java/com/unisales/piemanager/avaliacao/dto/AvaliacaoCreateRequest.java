package com.unisales.piemanager.avaliacao.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class AvaliacaoCreateRequest {

    @NotNull(message = "projetoId is required")
    private Long projetoId;

    @NotNull(message = "avaliadorId is required")
    private Long avaliadorId;

    @NotNull(message = "nota is required")
    @DecimalMin(value = "0.0", message = "nota must be greater than or equal to 0")
    @DecimalMax(value = "10.0", message = "nota must be less than or equal to 10")
    private BigDecimal nota;

    @NotBlank(message = "comentario is required")
    @Size(max = 2000, message = "comentario must have up to 2000 chars")
    private String comentario;

    public Long getProjetoId() {
        return projetoId;
    }

    public void setProjetoId(Long projetoId) {
        this.projetoId = projetoId;
    }

    public Long getAvaliadorId() {
        return avaliadorId;
    }

    public void setAvaliadorId(Long avaliadorId) {
        this.avaliadorId = avaliadorId;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
