package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tier_index;

    @Column
    private String tier_name;

    @Column
    private String tier_image;

    @Column
    private Long tier_exp;

    @Column
    private Long tier_top_ratio;


}
