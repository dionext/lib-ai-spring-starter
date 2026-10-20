package com.dionext.ai.services;

import com.dionext.ai.entity.AiPrompt;
import com.dionext.ai.repositories.AiPromptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AiPromptService {

    @Autowired
    private final AiPromptRepository aiPromptRepository;

    public AiPromptService(AiPromptRepository aiPromptRepository) {
        this.aiPromptRepository = aiPromptRepository;
    }

    public List<AiPrompt> findAll() {
        return aiPromptRepository.findAll();
    }
    
    public Page<AiPrompt> findAll(Pageable pageable) {
        return aiPromptRepository.findAll(pageable);
    }
    
    public Page<AiPrompt> search(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return aiPromptRepository.findAll(pageable);
        }
        return aiPromptRepository.findBySearchTerm(searchTerm.trim(), pageable);
    }

    public Optional<AiPrompt> findById(Long id) {
        return aiPromptRepository.findById(id);
    }

    public AiPrompt save(AiPrompt aiPrompt) {
        return aiPromptRepository.save(aiPrompt);
    }

    public void deleteById(Long id) {
        aiPromptRepository.deleteById(id);
    }
} 