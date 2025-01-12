package com.dionext.ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class AiRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long aiModelId;
    private Long aiPromptId;

    private String externalDomain;
    private String externalEntity;
    private String externalVariant;
    private String externalId;

    @Column(columnDefinition="TEXT")
    private String systemPrompt;
    @Column(columnDefinition="TEXT")
    private String userPrompt;
    @Column(precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(columnDefinition="TEXT")
    private String result;

    private Long duration; //ms
    private LocalDateTime dateTime;
    private Long promptTokens;
    private Long generationTokens;
    @Column(precision = 40, scale = 20)
    private BigDecimal cost;
}
