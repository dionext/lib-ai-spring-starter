package com.dionext.ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "airequest")
public class AiRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aiModelId")
    private AiModel aiModel;

    @ManyToOne
    @JoinColumn(name = "aiPromptId")
    private AiPrompt aiPrompt;

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
    private Long completionTokens;
    private Long totalTokens;
    @Column(precision = 40, scale = 20)
    private BigDecimal cost;
    
    // Getter and setter for aiModelId
    public Long getAiModelId() {
        return aiModel != null ? aiModel.getId() : null;
    }
    
    public void setAiModelId(Long aiModelId) {
        if (aiModelId == null) {
            this.aiModel = null;
        } else if (this.aiModel == null || !aiModelId.equals(this.aiModel.getId())) {
            AiModel model = new AiModel();
            model.setId(aiModelId);
            this.aiModel = model;
        }
    }
    
    // Getter and setter for aiPromptId
    public Long getAiPromptId() {
        return aiPrompt != null ? aiPrompt.getId() : null;
    }
    
    public void setAiPromptId(Long aiPromptId) {
        if (aiPromptId == null) {
            this.aiPrompt = null;
        } else if (this.aiPrompt == null || !aiPromptId.equals(this.aiPrompt.getId())) {
            AiPrompt prompt = new AiPrompt();
            prompt.setId(aiPromptId);
            this.aiPrompt = prompt;
        }
    }
}
