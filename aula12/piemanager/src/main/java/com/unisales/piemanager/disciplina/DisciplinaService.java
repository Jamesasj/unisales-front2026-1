package com.unisales.piemanager.disciplina;

import com.unisales.piemanager.common.exception.BusinessException;
import com.unisales.piemanager.common.exception.ResourceNotFoundException;
import com.unisales.piemanager.curso.CursoService;
import com.unisales.piemanager.curso.model.Curso;
import com.unisales.piemanager.disciplina.dto.DisciplinaCreateRequest;
import com.unisales.piemanager.disciplina.dto.DisciplinaResponse;
import com.unisales.piemanager.disciplina.dto.DisciplinaUpdateRequest;
import com.unisales.piemanager.disciplina.model.Disciplina;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final CursoService cursoService;

    public DisciplinaService(DisciplinaRepository disciplinaRepository, CursoService cursoService) {
        this.disciplinaRepository = disciplinaRepository;
        this.cursoService = cursoService;
    }

    @Transactional
    public DisciplinaResponse create(DisciplinaCreateRequest request, String actor) {
        Curso curso = cursoService.getEntityById(request.getCursoId());
        String nome = request.getNome().trim();

        if (disciplinaRepository.existsByNomeIgnoreCaseAndCursoId(nome, curso.getId())) {
            throw new BusinessException("Disciplina nome already exists for this curso");
        }

        Disciplina disciplina = new Disciplina();
        disciplina.setNome(nome);
        disciplina.setCurso(curso);
        disciplina.setCreatedBy(defaultActor(actor));
        disciplina.setUpdatedBy(defaultActor(actor));

        return toResponse(disciplinaRepository.save(disciplina));
    }

    @Transactional(readOnly = true)
    public List<DisciplinaResponse> findAll() {
        return disciplinaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DisciplinaResponse findById(Long id) {
        return toResponse(getEntityById(id));
    }

    @Transactional
    public DisciplinaResponse update(Long id, DisciplinaUpdateRequest request, String actor) {
        Disciplina disciplina = getEntityById(id);

        String nome = disciplina.getNome();
        if (request.getNome() != null && !request.getNome().isBlank()) {
            nome = request.getNome().trim();
        }

        Curso curso = disciplina.getCurso();
        if (request.getCursoId() != null) {
            curso = cursoService.getEntityById(request.getCursoId());
        }

        if (disciplinaRepository.existsByNomeIgnoreCaseAndCursoIdAndIdNot(nome, curso.getId(), id)) {
            throw new BusinessException("Disciplina nome already exists for this curso");
        }

        disciplina.setNome(nome);
        disciplina.setCurso(curso);
        disciplina.setUpdatedBy(defaultActor(actor));

        return toResponse(disciplinaRepository.save(disciplina));
    }

    @Transactional
    public void delete(Long id) {
        Disciplina disciplina = getEntityById(id);
        disciplinaRepository.delete(disciplina);
    }

    @Transactional(readOnly = true)
    public Disciplina getEntityById(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina not found"));
    }

    @Transactional(readOnly = true)
    public DisciplinaResponse toResponse(Disciplina disciplina) {
        DisciplinaResponse response = new DisciplinaResponse();
        response.setId(disciplina.getId());
        response.setNome(disciplina.getNome());
        response.setCursoId(disciplina.getCurso().getId());
        response.setCursoNome(disciplina.getCurso().getNome());
        response.setCreatedAt(disciplina.getCreatedAt());
        response.setCreatedBy(disciplina.getCreatedBy());
        response.setUpdatedAt(disciplina.getUpdatedAt());
        response.setUpdatedBy(disciplina.getUpdatedBy());
        return response;
    }

    private String defaultActor(String actor) {
        return (actor == null || actor.isBlank()) ? "system" : actor;
    }
}
