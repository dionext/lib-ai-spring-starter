package com.dionext.ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "aimodel")
public class AiModel {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="TEXT")
    private String provider;
    @Column(columnDefinition="TEXT")
    private String providerUrl;
    @Column(columnDefinition="TEXT")
    private String model;
    @Column(columnDefinition="TEXT")
    private String modelUrl;
    @Column
    @Enumerated(EnumType.STRING)
    private AiLaunchType launchType = AiLaunchType.PROVIDER;
    @Column(columnDefinition="TEXT")
    private String broker;
    @Column(columnDefinition="TEXT")
    private String brokerUrl;
    @Column(columnDefinition="TEXT")
    private String chatClientBean;

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
