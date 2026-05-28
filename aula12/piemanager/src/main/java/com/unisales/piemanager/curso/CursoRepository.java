package com.unisales.piemanager.curso;

import com.unisales.piemanager.curso.model.Curso;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    Optional<Curso> findByNomeIgnoreCase(String nome);
}
