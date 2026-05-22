package com.unisales.piemanager.projeto;

import com.unisales.piemanager.projeto.model.Projeto;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    List<Projeto> findByTurmaId(Long turmaId);

    Optional<Projeto> findByNomeIgnoreCaseAndTurmaIdAndSemestreId(String nome, Long turmaId, Long semestreId);

    List<Projeto> findBySemestreId(Long semestreId);

    List<Projeto> findByLocalId(Long localId);

    boolean existsByIdAndIntegrantesId(Long projetoId, Long userId);

    @Query("""
            select case when count(p) > 0 then true else false end
            from Projeto p
            where p.local.id = :localId
              and p.horarioInicio < :horarioFim
              and p.horarioFim > :horarioInicio
            """)
    boolean existsConflitoHorarioNoLocal(@Param("localId") Long localId,
                                         @Param("horarioInicio") Instant horarioInicio,
                                         @Param("horarioFim") Instant horarioFim);

    @Query("""
            select case when count(p) > 0 then true else false end
            from Projeto p
            where p.local.id = :localId
              and p.horarioInicio < :horarioFim
              and p.horarioFim > :horarioInicio
              and p.id <> :projetoId
            """)
    boolean existsConflitoHorarioNoLocalExcluindoProjeto(@Param("localId") Long localId,
                                                         @Param("horarioInicio") Instant horarioInicio,
                                                         @Param("horarioFim") Instant horarioFim,
                                                         @Param("projetoId") Long projetoId);

    @Query("""
            select case when count(p) > 0 then true else false end
            from Projeto p
            join p.integrantes i
            where p.turma.id = :turmaId
              and p.semestre.id = :semestreId
              and i.id = :alunoId
            """)
    boolean existsAlunoEmProjetoDaTurmaSemestre(@Param("turmaId") Long turmaId,
                                                @Param("semestreId") Long semestreId,
                                                @Param("alunoId") Long alunoId);

    @Query("""
            select case when count(p) > 0 then true else false end
            from Projeto p
            join p.integrantes i
            where p.turma.id = :turmaId
              and p.semestre.id = :semestreId
              and i.id = :alunoId
              and p.id <> :projetoId
            """)
    boolean existsAlunoEmProjetoDaTurmaSemestreExcluindoProjeto(@Param("turmaId") Long turmaId,
                                                                @Param("semestreId") Long semestreId,
                                                                @Param("alunoId") Long alunoId,
                                                                @Param("projetoId") Long projetoId);

    boolean existsByIdAndTurmaProfessoresEmailIgnoreCase(Long projetoId, String email);

    boolean existsByIdAndIntegrantesEmailIgnoreCase(Long projetoId, String email);

    boolean existsByIdAndProfessorOrientadorEmailIgnoreCase(Long projetoId, String email);
}
