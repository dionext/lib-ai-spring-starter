package com.dionext.ai.services;

import com.dionext.ai.entity.AiRequest;
import com.dionext.ai.repositories.AiRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AiRequestWebService {

    @Autowired
    private final AiRequestRepository aiRequestRepository;

    public AiRequestWebService(AiRequestRepository aiRequestRepository) {
        this.aiRequestRepository = aiRequestRepository;
    }

    public List<AiRequest> findAll() {
        return aiRequestRepository.findAll();
    }
    
    public Page<AiRequest> findAll(Pageable pageable) {
        return aiRequestRepository.findAll(pageable);
    }
    
    public Page<AiRequest> search(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return aiRequestRepository.findAll(pageable);
        }
        return aiRequestRepository.findBySearchTerm(searchTerm.trim(), pageable);
    }

    public Optional<AiRequest> findById(Long id) {
        return aiRequestRepository.findById(id);
    }

    public AiRequest save(AiRequest aiRequest) {
        return aiRequestRepository.save(aiRequest);
    }

    public void deleteById(Long id) {
        aiRequestRepository.deleteById(id);
    }
} 