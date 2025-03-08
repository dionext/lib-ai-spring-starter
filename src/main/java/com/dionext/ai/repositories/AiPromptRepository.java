package com.dionext.ai.repositories;

import com.dionext.ai.entity.AiPrompt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiPromptRepository extends JpaRepository<AiPrompt, Long> {
    //Optional<AiPrompt> findByName(String name);
}