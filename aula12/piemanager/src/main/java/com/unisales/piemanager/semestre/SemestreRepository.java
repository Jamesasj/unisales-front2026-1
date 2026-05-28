package com.unisales.piemanager.semestre;

import com.unisales.piemanager.semestre.model.Semestre;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemestreRepository extends JpaRepository<Semestre, Long> {
    boolean existsByNomeIgnoreCase(String nome);

    Optional<Semestre> findByNomeIgnoreCase(String nome);
}
