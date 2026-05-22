package com.unisales.piemanager.avaliacao;

import com.unisales.piemanager.avaliacao.model.Avaliacao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByProjetoId(Long projetoId);

    List<Avaliacao> findByAvaliadorId(Long avaliadorId);

    boolean existsByProjetoIdAndAvaliadorId(Long projetoId, Long avaliadorId);

    boolean existsByProjetoId(Long projetoId);

    boolean existsByIdAndAvaliadorEmailIgnoreCase(Long id, String email);

    boolean existsByIdAndProjetoIdAndAvaliadorEmailIgnoreCase(Long id, Long projetoId, String email);

    Optional<Avaliacao> findByIdAndProjetoId(Long id, Long projetoId);
}
