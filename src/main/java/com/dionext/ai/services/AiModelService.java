package com.dionext.ai.services;

import com.dionext.ai.entity.AiModel;
import com.dionext.ai.repositories.AiModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AiModelService {

    @Autowired
    private final AiModelRepository aiModelRepository;

    public AiModelService(AiModelRepository aiModelRepository) {
        this.aiModelRepository = aiModelRepository;
    }

    public List<AiModel> findAll() {
        return aiModelRepository.findAll();
    }
    
    public Page<AiModel> findAll(Pageable pageable) {
        return aiModelRepository.findAll(pageable);
    }
    
    public Page<AiModel> search(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return aiModelRepository.findAll(pageable);
        }
        return aiModelRepository.findBySearchTerm(searchTerm.trim(), pageable);
    }

    public Optional<AiModel> findById(Long id) {
        return aiModelRepository.findById(id);
    }

    public AiModel save(AiModel aiModel) {
        return aiModelRepository.save(aiModel);
    }

    public void deleteById(Long id) {
        aiModelRepository.deleteById(id);
    }
}
