package com.dionext.ai.repositories;

import com.dionext.ai.entity.AiRequest;
import com.dionext.ai.entity.TmpAiRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TmpAiRequestRepository extends JpaRepository<TmpAiRequest, Long> {
    Collection<AiRequest> findByExternalDomainAndExternalEntityAndExternalVariantAndExternalId(
            String externalDomain, String externalEntity, String externalVariant, String externalId);
}