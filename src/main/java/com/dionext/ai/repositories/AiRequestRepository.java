package com.dionext.ai.repositories;

import com.dionext.ai.entity.AiRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface AiRequestRepository extends JpaRepository<AiRequest, Long> {
    Collection<AiRequest> findByExternalDomainAndExternalEntityAndExternalVariant(
            String externalDomain, String externalEntity, String externalVariant);
    Collection<AiRequest> findByExternalDomainAndExternalEntityAndExternalVariantAndExternalId(
            String externalDomain, String externalEntity, String externalVariant, String externalId);
    Collection<AiRequest> findByExternalDomainAndExternalEntityAndExternalVariantAndExternalIdAndAiModelIdAndAiPromptId(
            String externalDomain, String externalEntity, String externalVariant, String externalId, Long aiModelId, Long aiPromptId);
}