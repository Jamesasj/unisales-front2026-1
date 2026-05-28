package com.unisales.piemanager.curso;

import com.unisales.piemanager.common.exception.BusinessException;
import com.unisales.piemanager.common.exception.ResourceNotFoundException;
import com.unisales.piemanager.curso.dto.CursoCreateRequest;
import com.unisales.piemanager.curso.dto.CursoResponse;
import com.unisales.piemanager.curso.dto.CursoUpdateRequest;
import com.unisales.piemanager.curso.model.Curso;
import com.unisales.piemanager.user.UserRepository;
import com.unisales.piemanager.user.model.Profile;
import com.unisales.piemanager.user.model.User;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final UserRepository userRepository;

    public CursoService(CursoRepository cursoRepository, UserRepository userRepository) {
        this.cursoRepository = cursoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CursoResponse create(CursoCreateRequest request, String actor) {
        String nome = request.getNome().trim();
        if (cursoRepository.existsByNomeIgnoreCase(nome)) {
            throw new BusinessException("Curso nome already exists");
        }

        User coordenador = findUserById(request.getCoordenadorId(), "Coordenador not found");
        validateCoordenador(coordenador);

        Set<User> professores = resolveProfessores(request.getProfessorIds());

        Curso curso = new Curso();
        curso.setNome(nome);
        curso.setCoordenador(coordenador);
        curso.setProfessores(professores);
        curso.setCreatedBy(defaultActor(actor));
        curso.setUpdatedBy(defaultActor(actor));

        return toResponse(cursoRepository.save(curso));
    }

    @Transactional(readOnly = true)
    public List<CursoResponse> findAll() {
        return cursoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CursoResponse findById(Long id) {
        return toResponse(getEntityById(id));
    }

    @Transactional
    public CursoResponse update(Long id, CursoUpdateRequest request, String actor) {
        Curso curso = getEntityById(id);

        if (request.getNome() != null && !request.getNome().isBlank()) {
            String nome = request.getNome().trim();
            if (cursoRepository.existsByNomeIgnoreCaseAndIdNot(nome, id)) {
                throw new BusinessException("Curso nome already exists");
            }
            curso.setNome(nome);
        }

        if (request.getCoordenadorId() != null) {
            User coordenador = findUserById(request.getCoordenadorId(), "Coordenador not found");
            validateCoordenador(coordenador);
            curso.setCoordenador(coordenador);
        }

        if (request.getProfessorIds() != null) {
            curso.setProfessores(resolveProfessores(request.getProfessorIds()));
        }

        curso.setUpdatedBy(defaultActor(actor));
        return toResponse(cursoRepository.save(curso));
    }

    @Transactional
    public void delete(Long id) {
        Curso curso = getEntityById(id);
        cursoRepository.delete(curso);
    }

    @Transactional(readOnly = true)
    public Curso getEntityById(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso not found"));
    }

    @Transactional(readOnly = true)
    public CursoResponse toResponse(Curso curso) {
        CursoResponse response = new CursoResponse();
        response.setId(curso.getId());
        response.setNome(curso.getNome());
        response.setCoordenador(toUserSummary(curso.getCoordenador()));
        response.setProfessores(curso.getProfessores().stream().map(this::toUserSummary).toList());
        response.setCreatedAt(curso.getCreatedAt());
        response.setCreatedBy(curso.getCreatedBy());
        response.setUpdatedAt(curso.getUpdatedAt());
        response.setUpdatedBy(curso.getUpdatedBy());
        return response;
    }

    private Set<User> resolveProfessores(Set<Long> professorIds) {
        if (professorIds == null || professorIds.isEmpty()) {
            throw new BusinessException("At least one professor is required");
        }

        Set<User> professores = new LinkedHashSet<>(userRepository.findAllById(professorIds));
        if (professores.size() != professorIds.size()) {
            throw new ResourceNotFoundException("One or more professores were not found");
        }

        boolean invalidProfile = professores.stream().anyMatch(u -> u.getProfile() != Profile.PROFESSOR);
        if (invalidProfile) {
            throw new BusinessException("All professorIds must have profile PROFESSOR");
        }

        return professores;
    }

    private void validateCoordenador(User coordenador) {
        if (coordenador.getProfile() != Profile.COORDENADOR) {
            throw new BusinessException("coordenadorId must have profile COORDENADOR");
        }
    }

    private User findUserById(Long id, String message) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    private CursoResponse.UserSummary toUserSummary(User user) {
        CursoResponse.UserSummary summary = new CursoResponse.UserSummary();
        summary.setId(user.getId());
        summary.setUsername(user.getUsername());
        summary.setEmail(user.getEmail());
        summary.setProfile(user.getProfile());
        return summary;
    }

    private String defaultActor(String actor) {
        return (actor == null || actor.isBlank()) ? "system" : actor;
    }
}
