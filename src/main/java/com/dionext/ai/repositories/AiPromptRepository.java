package com.dionext.ai.repositories;

import com.dionext.ai.entity.AiPrompt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AiPromptRepository extends JpaRepository<AiPrompt, Long> {
    //Optional<AiPrompt> findByName(String name);
    
    @Query("SELECT p FROM AiPrompt p WHERE " +
           "(:search IS NULL OR CAST(p.id AS string) LIKE %:search% OR " +
           "COALESCE(p.name, '') LIKE %:search% OR " +
           "COALESCE(p.systemPromptTempl, '') LIKE %:search% OR " +
           "COALESCE(p.userPromptTempl, '') LIKE %:search%)")
    Page<AiPrompt> findBySearchTerm(@Param("search") String search, Pageable pageable);
}