package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendar_index;

    @Column
    @ColumnDefault("true")
    private boolean calendar_status;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Planner> calendar_planners;
}
