package com.dionext.ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AiPrompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, columnDefinition="TEXT")
    private String name;

    @Column(columnDefinition="TEXT")
    private String systemPromptTempl;
    @Column(columnDefinition="TEXT")
    private String userPromptTempl;
}
