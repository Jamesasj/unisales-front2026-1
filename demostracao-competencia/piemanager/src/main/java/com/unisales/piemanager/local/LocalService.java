package com.unisales.piemanager.local;

import com.unisales.piemanager.common.exception.BusinessException;
import com.unisales.piemanager.common.exception.ResourceNotFoundException;
import com.unisales.piemanager.local.dto.LocalCreateRequest;
import com.unisales.piemanager.local.dto.LocalResponse;
import com.unisales.piemanager.local.dto.LocalUpdateRequest;
import com.unisales.piemanager.local.model.Local;
import com.unisales.piemanager.projeto.ProjetoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalService {

    private final LocalRepository localRepository;
    private final ProjetoRepository projetoRepository;

    public LocalService(LocalRepository localRepository, ProjetoRepository projetoRepository) {
        this.localRepository = localRepository;
        this.projetoRepository = projetoRepository;
    }

    @Transactional
    public LocalResponse create(LocalCreateRequest request, String actor) {
        String numero = request.getNumero().trim();
        if (localRepository.existsByNumeroIgnoreCase(numero)) {
            throw new BusinessException("Local numero already exists");
        }

        Local local = new Local();
        local.setNumero(numero);
        local.setCreatedBy(defaultActor(actor));
        local.setUpdatedBy(defaultActor(actor));
        return toResponse(localRepository.save(local));
    }

    @Transactional(readOnly = true)
    public List<LocalResponse> findAll() {
        return localRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public LocalResponse findById(Long id) {
        return toResponse(getEntityById(id));
    }

    @Transactional
    public LocalResponse update(Long id, LocalUpdateRequest request, String actor) {
        Local local = getEntityById(id);

        if (request.getNumero() != null && !request.getNumero().isBlank()) {
            String numero = request.getNumero().trim();
            if (localRepository.existsByNumeroIgnoreCaseAndIdNot(numero, id)) {
                throw new BusinessException("Local numero already exists");
            }
            local.setNumero(numero);
        }

        local.setUpdatedBy(defaultActor(actor));
        return toResponse(localRepository.save(local));
    }

    @Transactional
    public void delete(Long id) {
        Local local = getEntityById(id);

        if (!projetoRepository.findByLocalId(id).isEmpty()) {
            throw new BusinessException("Cannot delete local with projeto linked");
        }

        localRepository.delete(local);
    }

    @Transactional(readOnly = true)
    public Local getEntityById(Long id) {
        return localRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local not found"));
    }

    @Transactional(readOnly = true)
    public LocalResponse toResponse(Local local) {
        LocalResponse response = new LocalResponse();
        response.setId(local.getId());
        response.setNumero(local.getNumero());
        response.setCreatedAt(local.getCreatedAt());
        response.setCreatedBy(local.getCreatedBy());
        response.setUpdatedAt(local.getUpdatedAt());
        response.setUpdatedBy(local.getUpdatedBy());
        return response;
    }

    private String defaultActor(String actor) {
        return (actor == null || actor.isBlank()) ? "system" : actor;
    }
}
