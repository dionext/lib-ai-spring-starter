package com.dionext.ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AiPrompt {
    public static double DEFAULT_TEMPERATURE = 0.8;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, columnDefinition="TEXT")
    private String name;

    //The sampling temperature to use that controls the apparent creativity of generated completions. Higher values will make output more random while lower values will make results more focused and deterministic. It is not recommended to modify temperature and top_p for the same completions request as the interaction of these two settings is difficult to predict.
   // @Column(precision = 5, scale = 2)
   // private double temperature = 0.8;

    @Column(columnDefinition="TEXT")
    private String systemPromptTempl;
    @Column(columnDefinition="TEXT")
    private String userPromptTempl;
}
