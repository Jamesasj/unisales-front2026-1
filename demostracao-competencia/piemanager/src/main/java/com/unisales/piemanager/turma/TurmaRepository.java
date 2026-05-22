package com.unisales.piemanager.turma;

import com.unisales.piemanager.turma.model.Turma;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    boolean existsByNomeIgnoreCaseAndSemestreId(String nome, Long semestreId);

    boolean existsByNomeIgnoreCaseAndSemestreIdAndIdNot(String nome, Long semestreId, Long id);

    Optional<Turma> findByNomeIgnoreCaseAndSemestreId(String nome, Long semestreId);

    boolean existsByIdAndAlunosId(Long turmaId, Long alunoId);

    boolean existsByIdAndProfessoresId(Long turmaId, Long professorId);
}
