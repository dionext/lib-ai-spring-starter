package com.dionext.ai.repositories;

import com.dionext.ai.entity.AiModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AiModelRepository extends JpaRepository<AiModel, Long> {
    
    @Query("SELECT m FROM AiModel m WHERE " +
           "(:search IS NULL OR CAST(m.id AS string) LIKE %:search% OR " +
           "COALESCE(m.provider, '') LIKE %:search% OR " +
           "COALESCE(m.model, '') LIKE %:search% OR " +
           "COALESCE(m.providerUrl, '') LIKE %:search% OR " +
           "COALESCE(m.modelUrl, '') LIKE %:search% OR " +
           "COALESCE(m.broker, '') LIKE %:search% OR " +
           "COALESCE(m.brokerUrl, '') LIKE %:search% OR " +
           "COALESCE(m.chatClientBean, '') LIKE %:search% OR " +
           "m.launchType LIKE %:search%)")
    Page<AiModel> findBySearchTerm(@Param("search") String search, Pageable pageable);
}
