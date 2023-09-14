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
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plan_index;

    @Column
    private String plan_name;

    @Column
    @ColumnDefault("true")
    private boolean plan_is_complete;

    @Column
    @ColumnDefault("false")
    private boolean plan_is_study;

    @Column
    @ColumnDefault("false")
    private boolean plan_is_visible_on_calendar;

    @OneToMany(mappedBy = "studytime", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StudyTime> studytime_times;

    @Column
    @ColumnDefault("true")
    private boolean studytime_status;

    @ManyToOne
    @JoinColumn(name = "planner_index")
    private Planner planner;

    @ManyToOne
    @JoinColumn(name = "userstudy_index")
    private UserStudy userStudy;


}
