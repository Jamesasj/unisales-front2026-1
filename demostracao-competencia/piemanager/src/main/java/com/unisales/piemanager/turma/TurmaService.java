package com.unisales.piemanager.turma;

import com.unisales.piemanager.common.exception.BusinessException;
import com.unisales.piemanager.common.exception.ResourceNotFoundException;
import com.unisales.piemanager.curso.CursoRepository;
import com.unisales.piemanager.curso.model.Curso;
import com.unisales.piemanager.disciplina.DisciplinaService;
import com.unisales.piemanager.disciplina.model.Disciplina;
import com.unisales.piemanager.semestre.SemestreService;
import com.unisales.piemanager.semestre.model.Semestre;
import com.unisales.piemanager.turma.dto.TurmaCreateRequest;
import com.unisales.piemanager.turma.dto.TurmaResponse;
import com.unisales.piemanager.turma.dto.TurmaUpdateRequest;
import com.unisales.piemanager.turma.model.Turma;
import com.unisales.piemanager.user.UserRepository;
import com.unisales.piemanager.user.model.Profile;
import com.unisales.piemanager.user.model.User;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final CursoRepository cursoRepository;
    private final DisciplinaService disciplinaService;
    private final SemestreService semestreService;
    private final UserRepository userRepository;

    public TurmaService(TurmaRepository turmaRepository,
                        CursoRepository cursoRepository,
                        DisciplinaService disciplinaService,
                        SemestreService semestreService,
                        UserRepository userRepository) {
        this.turmaRepository = turmaRepository;
        this.cursoRepository = cursoRepository;
        this.disciplinaService = disciplinaService;
        this.semestreService = semestreService;
        this.userRepository = userRepository;
    }

    @Transactional
    public TurmaResponse create(TurmaCreateRequest request, String actor) {
        Semestre semestre = semestreService.getEntityById(request.getSemestreId());
        String nome = request.getNome().trim();
        if (turmaRepository.existsByNomeIgnoreCaseAndSemestreId(nome, semestre.getId())) {
            throw new BusinessException("Turma nome already exists for this semestre");
        }

        Set<Curso> cursos = resolveCursos(request.getCursoIds());
        Disciplina disciplina = disciplinaService.getEntityById(request.getDisciplinaId());
        validateDisciplinaNosCursos(disciplina, cursos);

        Set<User> professores = resolveProfessores(request.getProfessorIds());

        Turma turma = new Turma();
        turma.setNome(nome);
        turma.setSemestre(semestre);
        turma.setCursos(cursos);
        turma.setDisciplina(disciplina);
        turma.setProfessores(professores);
        turma.setCreatedBy(defaultActor(actor));
        turma.setUpdatedBy(defaultActor(actor));

        return toResponse(turmaRepository.save(turma));
    }

    @Transactional(readOnly = true)
    public List<TurmaResponse> findAll() {
        return turmaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TurmaResponse findById(Long id) {
        return toResponse(getEntityById(id));
    }

    @Transactional
    public TurmaResponse update(Long id, TurmaUpdateRequest request, String actor) {
        Turma turma = getEntityById(id);

        Semestre semestre = turma.getSemestre();
        if (request.getSemestreId() != null) {
            semestre = semestreService.getEntityById(request.getSemestreId());
            turma.setSemestre(semestre);
        }

        String nome = turma.getNome();
        if (request.getNome() != null && !request.getNome().isBlank()) {
            nome = request.getNome().trim();
        }
        if (turmaRepository.existsByNomeIgnoreCaseAndSemestreIdAndIdNot(nome, semestre.getId(), turma.getId())) {
            throw new BusinessException("Turma nome already exists for this semestre");
        }
        turma.setNome(nome);

        Set<Curso> cursos = turma.getCursos();
        if (request.getCursoIds() != null) {
            cursos = resolveCursos(request.getCursoIds());
            turma.setCursos(cursos);
        }

        Disciplina disciplina = turma.getDisciplina();
        if (request.getDisciplinaId() != null) {
            disciplina = disciplinaService.getEntityById(request.getDisciplinaId());
            turma.setDisciplina(disciplina);
        }

        validateDisciplinaNosCursos(disciplina, cursos);

        if (request.getProfessorIds() != null) {
            turma.setProfessores(resolveProfessores(request.getProfessorIds()));
        }

        turma.setUpdatedBy(defaultActor(actor));
        return toResponse(turmaRepository.save(turma));
    }

    @Transactional
    public void delete(Long id) {
        Turma turma = getEntityById(id);
        turmaRepository.delete(turma);
    }

    @Transactional
    public TurmaResponse matricularAluno(Long turmaId, String email) {
        Turma turma = getEntityById(turmaId);

        User aluno = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (aluno.getProfile() != Profile.ALUNO) {
            throw new BusinessException("Only ALUNO profile can enroll in turma");
        }

        boolean alreadyEnrolled = turma.getAlunos().stream().anyMatch(u -> u.getId().equals(aluno.getId()));
        if (alreadyEnrolled) {
            throw new BusinessException("Aluno already enrolled in turma");
        }

        turma.getAlunos().add(aluno);
        turma.setUpdatedBy(aluno.getEmail());
        return toResponse(turmaRepository.save(turma));
    }

    @Transactional
    public TurmaResponse addAluno(Long turmaId, Long alunoId, String actor) {
        Turma turma = getEntityById(turmaId);

        User aluno = userRepository.findById(alunoId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (aluno.getProfile() != Profile.ALUNO) {
            throw new BusinessException("Only ALUNO profile can be added to turma");
        }

        boolean alreadyEnrolled = turma.getAlunos().stream().anyMatch(u -> u.getId().equals(aluno.getId()));
        if (alreadyEnrolled) {
            throw new BusinessException("Aluno already enrolled in turma");
        }

        turma.getAlunos().add(aluno);
        turma.setUpdatedBy(defaultActor(actor));
        return toResponse(turmaRepository.save(turma));
    }

    @Transactional
    public TurmaResponse removeAluno(Long turmaId, Long alunoId, String actor) {
        Turma turma = getEntityById(turmaId);

        User aluno = userRepository.findById(alunoId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (aluno.getProfile() != Profile.ALUNO) {
            throw new BusinessException("Only ALUNO profile can be removed from turma");
        }

        boolean removed = turma.getAlunos().removeIf(u -> u.getId().equals(aluno.getId()));
        if (!removed) {
            throw new BusinessException("Aluno is not enrolled in turma");
        }

        turma.setUpdatedBy(defaultActor(actor));
        return toResponse(turmaRepository.save(turma));
    }

    @Transactional(readOnly = true)
    public Turma getEntityById(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turma not found"));
    }

    @Transactional(readOnly = true)
    public TurmaResponse toResponse(Turma turma) {
        TurmaResponse response = new TurmaResponse();
        response.setId(turma.getId());
        response.setNome(turma.getNome());
        response.setCursos(turma.getCursos().stream().map(this::toIdNomeCurso).toList());
        response.setDisciplina(toIdNomeDisciplina(turma.getDisciplina()));
        response.setSemestre(toIdNomeSemestre(turma.getSemestre()));
        response.setAlunos(turma.getAlunos().stream().map(this::toUserSummary).toList());
        response.setProfessores(turma.getProfessores().stream().map(this::toUserSummary).toList());
        response.setCreatedAt(turma.getCreatedAt());
        response.setCreatedBy(turma.getCreatedBy());
        response.setUpdatedAt(turma.getUpdatedAt());
        response.setUpdatedBy(turma.getUpdatedBy());
        return response;
    }

    private Set<Curso> resolveCursos(Set<Long> cursoIds) {
        if (cursoIds == null || cursoIds.isEmpty()) {
            throw new BusinessException("At least one curso is required");
        }

        Set<Curso> cursos = new LinkedHashSet<>(cursoRepository.findAllById(cursoIds));
        if (cursos.size() != cursoIds.size()) {
            throw new ResourceNotFoundException("One or more cursos were not found");
        }
        return cursos;
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

    private void validateDisciplinaNosCursos(Disciplina disciplina, Set<Curso> cursos) {
        boolean pertence = cursos.stream().anyMatch(curso -> curso.getId().equals(disciplina.getCurso().getId()));
        if (!pertence) {
            throw new BusinessException("Disciplina must belong to at least one curso in turma");
        }
    }

    private TurmaResponse.IdNome toIdNomeCurso(Curso curso) {
        TurmaResponse.IdNome idNome = new TurmaResponse.IdNome();
        idNome.setId(curso.getId());
        idNome.setNome(curso.getNome());
        return idNome;
    }

    private TurmaResponse.IdNome toIdNomeDisciplina(Disciplina disciplina) {
        TurmaResponse.IdNome idNome = new TurmaResponse.IdNome();
        idNome.setId(disciplina.getId());
        idNome.setNome(disciplina.getNome());
        return idNome;
    }

    private TurmaResponse.IdNome toIdNomeSemestre(Semestre semestre) {
        TurmaResponse.IdNome idNome = new TurmaResponse.IdNome();
        idNome.setId(semestre.getId());
        idNome.setNome(semestre.getNome());
        return idNome;
    }

    private TurmaResponse.UserSummary toUserSummary(User user) {
        TurmaResponse.UserSummary summary = new TurmaResponse.UserSummary();
        summary.setId(user.getId());
        summary.setUsername(user.getUsername());
        summary.setEmail(user.getEmail());
        return summary;
    }

    private String defaultActor(String actor) {
        return (actor == null || actor.isBlank()) ? "system" : actor;
    }
}
