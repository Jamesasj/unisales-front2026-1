package com.unisales.piemanager.avaliacao;

import com.unisales.piemanager.avaliacao.dto.AvaliacaoCreateRequest;
import com.unisales.piemanager.avaliacao.dto.AvaliacaoResponse;
import com.unisales.piemanager.avaliacao.dto.AvaliacaoUpdateRequest;
import com.unisales.piemanager.avaliacao.dto.ProjetoAvaliacaoCreateRequest;
import com.unisales.piemanager.avaliacao.dto.ProjetoAvaliacaoUpdateRequest;
import com.unisales.piemanager.avaliacao.model.Avaliacao;
import com.unisales.piemanager.common.exception.BusinessException;
import com.unisales.piemanager.common.exception.ResourceNotFoundException;
import com.unisales.piemanager.projeto.ProjetoService;
import com.unisales.piemanager.projeto.model.Projeto;
import com.unisales.piemanager.user.UserRepository;
import com.unisales.piemanager.user.model.Profile;
import com.unisales.piemanager.user.model.User;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ProjetoService projetoService;
    private final UserRepository userRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository,
                            ProjetoService projetoService,
                            UserRepository userRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.projetoService = projetoService;
        this.userRepository = userRepository;
    }

    @Transactional
    public AvaliacaoResponse create(AvaliacaoCreateRequest request, String actorEmail) {
        Projeto projeto = projetoService.getEntityById(request.getProjetoId());
        User avaliador = getUserById(request.getAvaliadorId(), "Avaliador not found");

        validateAvaliadorProfile(avaliador);
        validateNota(request.getNota());

        if (avaliacaoRepository.existsByProjetoIdAndAvaliadorId(projeto.getId(), avaliador.getId())) {
            throw new BusinessException("Avaliador already evaluated this projeto");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setProjeto(projeto);
        avaliacao.setAvaliador(avaliador);
        avaliacao.setNota(request.getNota());
        avaliacao.setComentario(request.getComentario().trim());
        avaliacao.setCreatedBy(defaultActor(actorEmail));
        avaliacao.setUpdatedBy(defaultActor(actorEmail));

        return toResponse(avaliacaoRepository.save(avaliacao));
    }

    @Transactional
    public AvaliacaoResponse createByProjeto(Long projetoId,
                                             ProjetoAvaliacaoCreateRequest request,
                                             String actorEmail) {
        Projeto projeto = projetoService.getEntityById(projetoId);
        User avaliador = getUserByEmail(actorEmail, "Avaliador not found");

        validateAvaliadorProfile(avaliador);
        validateNota(request.getNota());

        if (avaliacaoRepository.existsByProjetoIdAndAvaliadorId(projeto.getId(), avaliador.getId())) {
            throw new BusinessException("Avaliador already evaluated this projeto");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setProjeto(projeto);
        avaliacao.setAvaliador(avaliador);
        avaliacao.setNota(request.getNota());
        avaliacao.setComentario(request.getComentario().trim());
        avaliacao.setCreatedBy(defaultActor(actorEmail));
        avaliacao.setUpdatedBy(defaultActor(actorEmail));

        return toResponse(avaliacaoRepository.save(avaliacao));
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponse> findAll(Long projetoId, Long avaliadorId) {
        if (projetoId != null) {
            return avaliacaoRepository.findByProjetoId(projetoId).stream().map(this::toResponse).toList();
        }
        if (avaliadorId != null) {
            return avaliacaoRepository.findByAvaliadorId(avaliadorId).stream().map(this::toResponse).toList();
        }
        return avaliacaoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AvaliacaoResponse findById(Long id) {
        return toResponse(getEntityById(id));
    }

    @Transactional
    public AvaliacaoResponse update(Long id, AvaliacaoUpdateRequest request, String actorEmail) {
        Avaliacao avaliacao = getEntityById(id);

        if (request.getAvaliadorId() != null) {
            User novoAvaliador = getUserById(request.getAvaliadorId(), "Avaliador not found");
            validateAvaliadorProfile(novoAvaliador);

            boolean jaExiste = avaliacaoRepository.existsByProjetoIdAndAvaliadorId(
                    avaliacao.getProjeto().getId(), novoAvaliador.getId());
            if (jaExiste && !novoAvaliador.getId().equals(avaliacao.getAvaliador().getId())) {
                throw new BusinessException("Avaliador already evaluated this projeto");
            }

            avaliacao.setAvaliador(novoAvaliador);
        }

        if (request.getNota() != null) {
            validateNota(request.getNota());
            avaliacao.setNota(request.getNota());
        }

        if (request.getComentario() != null && !request.getComentario().isBlank()) {
            avaliacao.setComentario(request.getComentario().trim());
        }

        avaliacao.setUpdatedBy(defaultActor(actorEmail));
        return toResponse(avaliacaoRepository.save(avaliacao));
    }

    @Transactional
    public AvaliacaoResponse updateByProjeto(Long projetoId,
                                             Long avaliacaoId,
                                             ProjetoAvaliacaoUpdateRequest request,
                                             String actorEmail) {
        Avaliacao avaliacao = getEntityByIdAndProjetoId(avaliacaoId, projetoId);

        validateNota(request.getNota());
        avaliacao.setNota(request.getNota());

        if (request.getComentario() != null && !request.getComentario().isBlank()) {
            avaliacao.setComentario(request.getComentario().trim());
        }

        avaliacao.setUpdatedBy(defaultActor(actorEmail));
        return toResponse(avaliacaoRepository.save(avaliacao));
    }

    @Transactional
    public void delete(Long id) {
        Avaliacao avaliacao = getEntityById(id);
        avaliacaoRepository.delete(avaliacao);
    }

    @Transactional
    public void deleteByProjeto(Long projetoId, Long avaliacaoId) {
        Avaliacao avaliacao = getEntityByIdAndProjetoId(avaliacaoId, projetoId);
        avaliacaoRepository.delete(avaliacao);
    }

    @Transactional(readOnly = true)
    public boolean isOwnerAvaliador(Long id, String email) {
        return avaliacaoRepository.existsByIdAndAvaliadorEmailIgnoreCase(id, normalizeEmail(email));
    }

    @Transactional(readOnly = true)
    public boolean isOwnerAvaliadorByProjeto(Long projetoId, Long avaliacaoId, String email) {
        return avaliacaoRepository.existsByIdAndProjetoIdAndAvaliadorEmailIgnoreCase(
                avaliacaoId, projetoId, normalizeEmail(email));
    }

    @Transactional(readOnly = true)
    public Avaliacao getEntityById(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliacao not found"));
    }

    @Transactional(readOnly = true)
    public Avaliacao getEntityByIdAndProjetoId(Long avaliacaoId, Long projetoId) {
        return avaliacaoRepository.findByIdAndProjetoId(avaliacaoId, projetoId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliacao not found"));
    }

    @Transactional(readOnly = true)
    public AvaliacaoResponse toResponse(Avaliacao avaliacao) {
        AvaliacaoResponse response = new AvaliacaoResponse();
        response.setId(avaliacao.getId());
        response.setProjeto(toIdNome(avaliacao.getProjeto().getId(), avaliacao.getProjeto().getNome()));
        response.setAvaliador(toUserSummary(avaliacao.getAvaliador()));
        response.setNota(avaliacao.getNota());
        response.setComentario(avaliacao.getComentario());
        response.setCreatedAt(avaliacao.getCreatedAt());
        response.setCreatedBy(avaliacao.getCreatedBy());
        response.setUpdatedAt(avaliacao.getUpdatedAt());
        response.setUpdatedBy(avaliacao.getUpdatedBy());
        return response;
    }

    private void validateAvaliadorProfile(User user) {
        if (user.getProfile() != Profile.PROFESSOR && user.getProfile() != Profile.AVALIADOR_EXTERNO) {
            throw new BusinessException("avaliadorId must have profile PROFESSOR or AVALIADOR_EXTERNO");
        }
    }

    private void validateNota(BigDecimal nota) {
        if (nota == null) {
            throw new BusinessException("nota is required");
        }

        if (nota.compareTo(BigDecimal.ZERO) < 0 || nota.compareTo(BigDecimal.TEN) > 0) {
            throw new BusinessException("nota must be between 0 and 10");
        }
    }

    private User getUserById(Long id, String notFoundMessage) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }

    private User getUserByEmail(String email, String notFoundMessage) {
        return userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }

    private AvaliacaoResponse.IdNome toIdNome(Long id, String nome) {
        AvaliacaoResponse.IdNome idNome = new AvaliacaoResponse.IdNome();
        idNome.setId(id);
        idNome.setNome(nome);
        return idNome;
    }

    private AvaliacaoResponse.UserSummary toUserSummary(User user) {
        AvaliacaoResponse.UserSummary summary = new AvaliacaoResponse.UserSummary();
        summary.setId(user.getId());
        summary.setUsername(user.getUsername());
        summary.setEmail(user.getEmail());
        return summary;
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private String defaultActor(String actor) {
        return (actor == null || actor.isBlank()) ? "system" : actor;
    }
}
