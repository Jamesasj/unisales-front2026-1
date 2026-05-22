package com.unisales.piemanager.semestre;

import com.unisales.piemanager.common.exception.BusinessException;
import com.unisales.piemanager.common.exception.ResourceNotFoundException;
import com.unisales.piemanager.semestre.dto.SemestreCreateRequest;
import com.unisales.piemanager.semestre.dto.SemestreResponse;
import com.unisales.piemanager.semestre.dto.SemestreUpdateRequest;
import com.unisales.piemanager.semestre.model.Semestre;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SemestreService {

    private final SemestreRepository semestreRepository;

    public SemestreService(SemestreRepository semestreRepository) {
        this.semestreRepository = semestreRepository;
    }

    @Transactional
    public SemestreResponse create(SemestreCreateRequest request, String actor) {
        String nome = request.getNome().trim();
        if (semestreRepository.existsByNomeIgnoreCase(nome)) {
            throw new BusinessException("Semestre nome already exists");
        }
        validatePeriodo(request.getDataInicio(), request.getDataFim());

        Semestre semestre = new Semestre();
        semestre.setNome(nome);
        semestre.setDataInicio(request.getDataInicio());
        semestre.setDataFim(request.getDataFim());
        semestre.setCreatedBy(defaultActor(actor));
        semestre.setUpdatedBy(defaultActor(actor));

        return toResponse(semestreRepository.save(semestre));
    }

    @Transactional(readOnly = true)
    public List<SemestreResponse> findAll() {
        return semestreRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SemestreResponse findById(Long id) {
        return toResponse(getEntityById(id));
    }

    @Transactional
    public SemestreResponse update(Long id, SemestreUpdateRequest request, String actor) {
        Semestre semestre = getEntityById(id);

        if (request.getNome() != null && !request.getNome().isBlank()) {
            String nome = request.getNome().trim();
            if (!nome.equalsIgnoreCase(semestre.getNome()) && semestreRepository.existsByNomeIgnoreCase(nome)) {
                throw new BusinessException("Semestre nome already exists");
            }
            semestre.setNome(nome);
        }

        if (request.getDataInicio() != null) {
            semestre.setDataInicio(request.getDataInicio());
        }
        if (request.getDataFim() != null) {
            semestre.setDataFim(request.getDataFim());
        }

        validatePeriodo(semestre.getDataInicio(), semestre.getDataFim());
        semestre.setUpdatedBy(defaultActor(actor));

        return toResponse(semestreRepository.save(semestre));
    }

    @Transactional
    public void delete(Long id) {
        Semestre semestre = getEntityById(id);
        semestreRepository.delete(semestre);
    }

    @Transactional(readOnly = true)
    public Semestre getEntityById(Long id) {
        return semestreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Semestre not found"));
    }

    @Transactional(readOnly = true)
    public SemestreResponse toResponse(Semestre semestre) {
        SemestreResponse response = new SemestreResponse();
        response.setId(semestre.getId());
        response.setNome(semestre.getNome());
        response.setDataInicio(semestre.getDataInicio());
        response.setDataFim(semestre.getDataFim());
        response.setCreatedAt(semestre.getCreatedAt());
        response.setCreatedBy(semestre.getCreatedBy());
        response.setUpdatedAt(semestre.getUpdatedAt());
        response.setUpdatedBy(semestre.getUpdatedBy());
        return response;
    }

    private void validatePeriodo(java.time.LocalDate inicio, java.time.LocalDate fim) {
        if (inicio != null && fim != null && fim.isBefore(inicio)) {
            throw new BusinessException("dataFim must be greater than or equal to dataInicio");
        }
    }

    private String defaultActor(String actor) {
        return (actor == null || actor.isBlank()) ? "system" : actor;
    }
}
