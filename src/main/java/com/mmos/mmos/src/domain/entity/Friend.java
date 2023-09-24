package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendIndex;

    @Column
    private Boolean friendIsFixed = false;

    @ManyToOne
    @JoinColumn(name = "userIndex")
    private User user;
}
