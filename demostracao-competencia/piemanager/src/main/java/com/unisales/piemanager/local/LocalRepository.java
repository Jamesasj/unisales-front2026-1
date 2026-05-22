package com.unisales.piemanager.local;

import com.unisales.piemanager.local.model.Local;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Local, Long> {
    Optional<Local> findByNumeroIgnoreCase(String numero);

    boolean existsByNumeroIgnoreCase(String numero);

    boolean existsByNumeroIgnoreCaseAndIdNot(String numero, Long id);
}
