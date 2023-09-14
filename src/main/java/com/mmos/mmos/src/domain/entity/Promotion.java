package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotion_index;

    @Column
    @ColumnDefault("true")
    private boolean promotion_status;

    @ManyToOne
    @JoinColumn(name = "study_index")
    private Study study;
}