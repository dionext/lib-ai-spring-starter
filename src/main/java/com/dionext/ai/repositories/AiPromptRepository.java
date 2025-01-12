package com.dionext.ai.repositories;

import com.dionext.ai.entity.AiPrompt;
import com.dionext.ai.entity.AiRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface AiPromptRepository extends JpaRepository<AiPrompt, Long> {
    Optional<AiPrompt> findByName(String name);
}