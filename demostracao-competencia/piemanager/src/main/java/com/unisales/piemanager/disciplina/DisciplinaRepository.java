package com.unisales.piemanager.disciplina;

import com.unisales.piemanager.disciplina.model.Disciplina;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    boolean existsByNomeIgnoreCaseAndCursoId(String nome, Long cursoId);

    boolean existsByNomeIgnoreCaseAndCursoIdAndIdNot(String nome, Long cursoId, Long id);

    Optional<Disciplina> findByNomeIgnoreCaseAndCursoId(String nome, Long cursoId);
}
