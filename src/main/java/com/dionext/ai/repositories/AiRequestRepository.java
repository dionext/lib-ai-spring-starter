package com.dionext.ai.repositories;

import com.dionext.ai.entity.AiModel;
import com.dionext.ai.entity.AiPrompt;
import com.dionext.ai.entity.AiRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface AiRequestRepository extends JpaRepository<AiRequest, Long> {
    Collection<AiRequest> findByExternalDomainAndExternalEntityAndExternalVariant(
            String externalDomain, String externalEntity, String externalVariant);
    Collection<AiRequest> findByExternalDomainAndExternalEntityAndExternalVariantAndExternalId(
            String externalDomain, String externalEntity, String externalVariant, String externalId);
    Collection<AiRequest> findByExternalDomainAndExternalEntityAndExternalVariantAndExternalIdAndAiModelAndAiPrompt(
            String externalDomain, String externalEntity, String externalVariant, String externalId, AiModel aiModel, AiPrompt aiPrompt);
    
    // Keep the old method for backward compatibility
    @Query("SELECT r FROM AiRequest r WHERE r.externalDomain = :externalDomain AND r.externalEntity = :externalEntity " +
           "AND r.externalVariant = :externalVariant AND r.externalId = :externalId " +
           "AND r.aiModel.id = :aiModelId AND r.aiPrompt.id = :aiPromptId")
    Collection<AiRequest> findByExternalDomainAndExternalEntityAndExternalVariantAndExternalIdAndAiModelIdAndAiPromptId(
            String externalDomain, String externalEntity, String externalVariant, String externalId, Long aiModelId, Long aiPromptId);
            
    @Query("SELECT r FROM AiRequest r WHERE " +
           "(:search IS NULL OR CAST(r.id AS string) LIKE %:search% OR " +
           "COALESCE(r.systemPrompt, '') LIKE %:search% OR " +
           "COALESCE(r.userPrompt, '') LIKE %:search% OR " +
           "COALESCE(r.result, '') LIKE %:search% OR " +
           "COALESCE(r.externalDomain, '') LIKE %:search% OR " +
           "COALESCE(r.externalEntity, '') LIKE %:search% OR " +
           "COALESCE(r.externalVariant, '') LIKE %:search% OR " +
           "COALESCE(r.externalId, '') LIKE %:search%)")
    Page<AiRequest> findBySearchTerm(@Param("search") String search, Pageable pageable);
}