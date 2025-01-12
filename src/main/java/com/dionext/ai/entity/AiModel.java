package com.dionext.ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class AiModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="TEXT")
    private String provider;
    @Column(columnDefinition="TEXT")
    private String model;

    //Price per 1M tokens
    @Column(precision = 40, scale = 20)
    private BigDecimal pricePer1MInput;
    @Column(precision = 40, scale = 20)
    private BigDecimal pricePer1MOutput;
    @Column(precision = 40, scale = 20)
    private BigDecimal pricePer1MAll;
    @Column(precision = 40, scale = 20)
    private BigDecimal pricePerRequest;
}
