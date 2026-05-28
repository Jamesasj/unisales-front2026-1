package com.unisales.piemanager.projeto;

import com.unisales.piemanager.common.exception.BusinessException;
import com.unisales.piemanager.common.exception.ResourceNotFoundException;
import com.unisales.piemanager.local.LocalService;
import com.unisales.piemanager.local.model.Local;
import com.unisales.piemanager.projeto.dto.ProjetoCreateRequest;
import com.unisales.piemanager.projeto.dto.ProjetoResponse;
import com.unisales.piemanager.projeto.dto.ProjetoUpdateRequest;
import com.unisales.piemanager.projeto.model.Projeto;
import com.unisales.piemanager.semestre.SemestreService;
import com.unisales.piemanager.semestre.model.Semestre;
import com.unisales.piemanager.turma.TurmaRepository;
import com.unisales.piemanager.turma.TurmaService;
import com.unisales.piemanager.turma.model.Turma;
import com.unisales.piemanager.user.UserRepository;
import com.unisales.piemanager.user.model.Profile;
import com.unisales.piemanager.user.model.User;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final TurmaService turmaService;
    private final SemestreService semestreService;
    private final LocalService localService;
    private final UserRepository userRepository;
    private final TurmaRepository turmaRepository;

    public ProjetoService(ProjetoRepository projetoRepository,
                          TurmaService turmaService,
                          SemestreService semestreService,
                          LocalService localService,
                          UserRepository userRepository,
                          TurmaRepository turmaRepository) {
        this.projetoRepository = projetoRepository;
        this.turmaService = turmaService;
        this.semestreService = semestreService;
        this.localService = localService;
        this.userRepository = userRepository;
        this.turmaRepository = turmaRepository;
    }

    @Transactional
    public ProjetoResponse create(ProjetoCreateRequest request, String actorEmail) {
        Turma turma = turmaService.getEntityById(request.getTurmaId());
        Semestre semestre = semestreService.getEntityById(request.getSemestreId());
        Local local = localService.getEntityById(request.getLocalId());
        User professorOrientador = getUserById(request.getProfessorOrientadorId(), "Professor orientador not found");

        validateTurmaSemestre(turma, semestre);
        validateProfessorOrientador(turma, professorOrientador);
        validateHorario(request.getHorarioInicio(), request.getHorarioFim());
        validateLocalDisponivel(local.getId(), request.getHorarioInicio(), request.getHorarioFim(), null);

        Set<User> integrantes = resolveIntegrantes(turma, semestre, request.getIntegranteIds(), null);
        applyAlunoAutoInclusaoSeNecessario(actorEmail, turma, semestre, integrantes, null);

        Projeto projeto = new Projeto();
        projeto.setNome(request.getNome().trim());
        projeto.setDescricao(request.getDescricao().trim());
        projeto.setTurma(turma);
        projeto.setSemestre(semestre);
        projeto.setProfessorOrientador(professorOrientador);
        projeto.setIntegrantes(integrantes);
        projeto.setLocal(local);
        projeto.setHorarioInicio(request.getHorarioInicio());
        projeto.setHorarioFim(request.getHorarioFim());
        projeto.setCreatedBy(defaultActor(actorEmail));
        projeto.setUpdatedBy(defaultActor(actorEmail));

        return toResponse(projetoRepository.save(projeto));
    }

    @Transactional(readOnly = true)
    public List<ProjetoResponse> findAll(Long turmaId, Long semestreId, Long localId) {
        if (turmaId != null) {
            return projetoRepository.findByTurmaId(turmaId).stream().map(this::toResponse).toList();
        }
        if (semestreId != null) {
            return projetoRepository.findBySemestreId(semestreId).stream().map(this::toResponse).toList();
        }
        if (localId != null) {
            return projetoRepository.findByLocalId(localId).stream().map(this::toResponse).toList();
        }
        return projetoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProjetoResponse findById(Long id) {
        return toResponse(getEntityById(id));
    }

    @Transactional
    public ProjetoResponse update(Long id, ProjetoUpdateRequest request, String actorEmail) {
        Projeto projeto = getEntityById(id);

        Turma turma = projeto.getTurma();
        if (request.getTurmaId() != null) {
            turma = turmaService.getEntityById(request.getTurmaId());
            projeto.setTurma(turma);
        }

        Semestre semestre = projeto.getSemestre();
        if (request.getSemestreId() != null) {
            semestre = semestreService.getEntityById(request.getSemestreId());
            projeto.setSemestre(semestre);
        }

        validateTurmaSemestre(turma, semestre);

        if (request.getNome() != null && !request.getNome().isBlank()) {
            projeto.setNome(request.getNome().trim());
        }
        if (request.getDescricao() != null && !request.getDescricao().isBlank()) {
            projeto.setDescricao(request.getDescricao().trim());
        }

        User professorOrientador = projeto.getProfessorOrientador();
        if (request.getProfessorOrientadorId() != null) {
            professorOrientador = getUserById(request.getProfessorOrientadorId(), "Professor orientador not found");
            projeto.setProfessorOrientador(professorOrientador);
        }
        validateProfessorOrientador(turma, professorOrientador);

        Local local = projeto.getLocal();
        if (request.getLocalId() != null) {
            local = localService.getEntityById(request.getLocalId());
            projeto.setLocal(local);
        }

        Instant horarioInicio = projeto.getHorarioInicio();
        if (request.getHorarioInicio() != null) {
            horarioInicio = request.getHorarioInicio();
            projeto.setHorarioInicio(horarioInicio);
        }

        Instant horarioFim = projeto.getHorarioFim();
        if (request.getHorarioFim() != null) {
            horarioFim = request.getHorarioFim();
            projeto.setHorarioFim(horarioFim);
        }

        validateHorario(horarioInicio, horarioFim);
        validateLocalDisponivel(local.getId(), horarioInicio, horarioFim, projeto.getId());

        if (request.getIntegranteIds() != null) {
            Set<User> integrantes = resolveIntegrantes(turma, semestre, request.getIntegranteIds(), projeto.getId());
            projeto.setIntegrantes(integrantes);
        }

        projeto.setUpdatedBy(defaultActor(actorEmail));
        return toResponse(projetoRepository.save(projeto));
    }

    @Transactional
    public void delete(Long id) {
        Projeto projeto = getEntityById(id);
        projetoRepository.delete(projeto);
    }

    @Transactional
    public ProjetoResponse addIntegranteMe(Long projetoId, String alunoEmail) {
        Projeto projeto = getEntityById(projetoId);
        User aluno = userRepository.findByEmail(normalizeEmail(alunoEmail))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (aluno.getProfile() != Profile.ALUNO) {
            throw new BusinessException("Only ALUNO can self-join projeto");
        }

        if (!turmaRepository.existsByIdAndAlunosId(projeto.getTurma().getId(), aluno.getId())) {
            throw new BusinessException("Aluno must belong to projeto turma");
        }

        if (projetoRepository.existsAlunoEmProjetoDaTurmaSemestreExcluindoProjeto(
                projeto.getTurma().getId(), projeto.getSemestre().getId(), aluno.getId(), projeto.getId())) {
            throw new BusinessException("Aluno already belongs to another projeto in turma and semestre");
        }

        if (!projeto.getIntegrantes().stream().anyMatch(u -> u.getId().equals(aluno.getId()))) {
            projeto.getIntegrantes().add(aluno);
        }

        validateIntegranteCount(projeto.getIntegrantes());
        projeto.setUpdatedBy(aluno.getEmail());
        return toResponse(projetoRepository.save(projeto));
    }

    @Transactional
    public ProjetoResponse removeIntegranteMe(Long projetoId, String alunoEmail) {
        Projeto projeto = getEntityById(projetoId);
        User aluno = userRepository.findByEmail(normalizeEmail(alunoEmail))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (aluno.getProfile() != Profile.ALUNO) {
            throw new BusinessException("Only ALUNO can leave projeto");
        }

        boolean removed = projeto.getIntegrantes().removeIf(u -> u.getId().equals(aluno.getId()));
        if (!removed) {
            throw new BusinessException("Aluno is not integrante of projeto");
        }

        validateIntegranteCount(projeto.getIntegrantes());
        projeto.setUpdatedBy(aluno.getEmail());
        return toResponse(projetoRepository.save(projeto));
    }

    @Transactional
    public ProjetoResponse addIntegrante(Long projetoId, Long alunoId, String actorEmail) {
        Projeto projeto = getEntityById(projetoId);
        User aluno = getUserById(alunoId, "User not found");

        if (aluno.getProfile() != Profile.ALUNO) {
            throw new BusinessException("Only ALUNO can be integrante of projeto");
        }

        if (!turmaRepository.existsByIdAndAlunosId(projeto.getTurma().getId(), aluno.getId())) {
            throw new BusinessException("Aluno must belong to projeto turma");
        }

        if (projetoRepository.existsAlunoEmProjetoDaTurmaSemestreExcluindoProjeto(
                projeto.getTurma().getId(), projeto.getSemestre().getId(), aluno.getId(), projeto.getId())) {
            throw new BusinessException("Aluno already belongs to another projeto in turma and semestre");
        }

        boolean alreadyIntegrante = projeto.getIntegrantes().stream().anyMatch(u -> u.getId().equals(aluno.getId()));
        if (alreadyIntegrante) {
            throw new BusinessException("Aluno already belongs to this projeto");
        }

        projeto.getIntegrantes().add(aluno);
        validateIntegranteCount(projeto.getIntegrantes());
        projeto.setUpdatedBy(defaultActor(actorEmail));
        return toResponse(projetoRepository.save(projeto));
    }

    @Transactional
    public ProjetoResponse removeIntegrante(Long projetoId, Long alunoId, String actorEmail) {
        Projeto projeto = getEntityById(projetoId);
        User aluno = getUserById(alunoId, "User not found");

        if (aluno.getProfile() != Profile.ALUNO) {
            throw new BusinessException("Only ALUNO can be removed from projeto");
        }

        boolean removed = projeto.getIntegrantes().removeIf(u -> u.getId().equals(aluno.getId()));
        if (!removed) {
            throw new BusinessException("Aluno is not integrante of projeto");
        }

        validateIntegranteCount(projeto.getIntegrantes());
        projeto.setUpdatedBy(defaultActor(actorEmail));
        return toResponse(projetoRepository.save(projeto));
    }

    @Transactional(readOnly = true)
    public Projeto getEntityById(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto not found"));
    }

    @Transactional(readOnly = true)
    public boolean isProfessorInProjectTurma(Long projetoId, String email) {
        return projetoRepository.existsByIdAndTurmaProfessoresEmailIgnoreCase(projetoId, normalizeEmail(email));
    }

    @Transactional(readOnly = true)
    public boolean isAlunoIntegrante(Long projetoId, String email) {
        return projetoRepository.existsByIdAndIntegrantesEmailIgnoreCase(projetoId, normalizeEmail(email));
    }

    @Transactional(readOnly = true)
    public boolean isProfessorOrientador(Long projetoId, String email) {
        return projetoRepository.existsByIdAndProfessorOrientadorEmailIgnoreCase(projetoId, normalizeEmail(email));
    }

    @Transactional(readOnly = true)
    public ProjetoResponse toResponse(Projeto projeto) {
        ProjetoResponse response = new ProjetoResponse();
        response.setId(projeto.getId());
        response.setNome(projeto.getNome());
        response.setDescricao(projeto.getDescricao());
        response.setTurma(toIdNome(projeto.getTurma().getId(), projeto.getTurma().getNome()));
        response.setSemestre(toIdNome(projeto.getSemestre().getId(), projeto.getSemestre().getNome()));
        response.setProfessorOrientador(toUserSummary(projeto.getProfessorOrientador()));
        response.setIntegrantes(projeto.getIntegrantes().stream().map(this::toUserSummary).toList());
        response.setLocal(toIdNome(projeto.getLocal().getId(), projeto.getLocal().getNumero()));
        response.setHorarioInicio(projeto.getHorarioInicio());
        response.setHorarioFim(projeto.getHorarioFim());
        response.setCreatedAt(projeto.getCreatedAt());
        response.setCreatedBy(projeto.getCreatedBy());
        response.setUpdatedAt(projeto.getUpdatedAt());
        response.setUpdatedBy(projeto.getUpdatedBy());
        return response;
    }

    private Set<User> resolveIntegrantes(Turma turma, Semestre semestre, Set<Long> integranteIds, Long projetoId) {
        if (integranteIds == null || integranteIds.isEmpty()) {
            throw new BusinessException("At least 3 integrantes are required");
        }

        Set<User> integrantes = new LinkedHashSet<>(userRepository.findAllById(integranteIds));
        if (integrantes.size() != integranteIds.size()) {
            throw new ResourceNotFoundException("One or more integrantes were not found");
        }

        boolean invalidProfile = integrantes.stream().anyMatch(u -> u.getProfile() != Profile.ALUNO);
        if (invalidProfile) {
            throw new BusinessException("All integranteIds must have profile ALUNO");
        }

        boolean integranteNaoMatriculado = integrantes.stream()
                .anyMatch(aluno -> !turmaRepository.existsByIdAndAlunosId(turma.getId(), aluno.getId()));
        if (integranteNaoMatriculado) {
            throw new BusinessException("All integrantes must belong to turma");
        }

        for (User aluno : integrantes) {
            boolean alreadyInAnotherProject;
            if (projetoId == null) {
                alreadyInAnotherProject = projetoRepository.existsAlunoEmProjetoDaTurmaSemestre(
                        turma.getId(), semestre.getId(), aluno.getId());
            } else {
                alreadyInAnotherProject = projetoRepository.existsAlunoEmProjetoDaTurmaSemestreExcluindoProjeto(
                        turma.getId(), semestre.getId(), aluno.getId(), projetoId);
            }
            if (alreadyInAnotherProject) {
                throw new BusinessException("Aluno already belongs to another projeto in turma and semestre");
            }
        }

        validateIntegranteCount(integrantes);
        return integrantes;
    }

    private void applyAlunoAutoInclusaoSeNecessario(String actorEmail,
                                                    Turma turma,
                                                    Semestre semestre,
                                                    Set<User> integrantes,
                                                    Long projetoId) {
        if (actorEmail == null || actorEmail.isBlank()) {
            return;
        }

        userRepository.findByEmail(normalizeEmail(actorEmail)).ifPresent(actor -> {
            if (actor.getProfile() != Profile.ALUNO) {
                return;
            }

            if (!turmaRepository.existsByIdAndAlunosId(turma.getId(), actor.getId())) {
                throw new BusinessException("Aluno can only create projeto in own turma");
            }

            boolean alreadyInAnotherProject;
            if (projetoId == null) {
                alreadyInAnotherProject = projetoRepository.existsAlunoEmProjetoDaTurmaSemestre(
                        turma.getId(), semestre.getId(), actor.getId());
            } else {
                alreadyInAnotherProject = projetoRepository.existsAlunoEmProjetoDaTurmaSemestreExcluindoProjeto(
                        turma.getId(), semestre.getId(), actor.getId(), projetoId);
            }

            if (alreadyInAnotherProject) {
                throw new BusinessException("Aluno already belongs to another projeto in turma and semestre");
            }

            integrantes.add(actor);
            validateIntegranteCount(integrantes);
        });
    }

    private void validateProfessorOrientador(Turma turma, User professorOrientador) {
        if (professorOrientador.getProfile() != Profile.PROFESSOR) {
            throw new BusinessException("professorOrientadorId must have profile PROFESSOR");
        }

        if (!turmaRepository.existsByIdAndProfessoresId(turma.getId(), professorOrientador.getId())) {
            throw new BusinessException("Professor orientador must belong to turma professores");
        }
    }

    private void validateTurmaSemestre(Turma turma, Semestre semestre) {
        if (!turma.getSemestre().getId().equals(semestre.getId())) {
            throw new BusinessException("Projeto semestre must be equal to turma semestre");
        }
    }

    private void validateLocalDisponivel(Long localId, Instant horarioInicio, Instant horarioFim, Long projetoId) {
        boolean conflito;
        if (projetoId == null) {
            conflito = projetoRepository.existsConflitoHorarioNoLocal(localId, horarioInicio, horarioFim);
        } else {
            conflito = projetoRepository.existsConflitoHorarioNoLocalExcluindoProjeto(
                    localId, horarioInicio, horarioFim, projetoId);
        }

        if (conflito) {
            throw new BusinessException("Local is already booked for the given horario");
        }
    }

    private void validateHorario(Instant horarioInicio, Instant horarioFim) {
        if (horarioInicio == null || horarioFim == null || !horarioInicio.isBefore(horarioFim)) {
            throw new BusinessException("horarioInicio must be before horarioFim");
        }
    }

    private void validateIntegranteCount(Set<User> integrantes) {
        int size = integrantes.size();
        if (size < 3 || size > 7) {
            throw new BusinessException("Projeto must have between 3 and 7 integrantes");
        }
    }

    private User getUserById(Long id, String notFoundMessage) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }

    private ProjetoResponse.IdNome toIdNome(Long id, String nome) {
        ProjetoResponse.IdNome idNome = new ProjetoResponse.IdNome();
        idNome.setId(id);
        idNome.setNome(nome);
        return idNome;
    }

    private ProjetoResponse.UserSummary toUserSummary(User user) {
        ProjetoResponse.UserSummary summary = new ProjetoResponse.UserSummary();
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
