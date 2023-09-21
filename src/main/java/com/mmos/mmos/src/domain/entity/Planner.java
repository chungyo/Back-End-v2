package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@NoArgsConstructor
@DynamicInsert
public class Planner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plannerIndex;

    @Column
    private LocalDate plannerDate;

    @Column
    @ColumnDefault("null")
    private String plannerMemo;

    @Column
    @ColumnDefault("0")
    private Long plannerDailyStudyTime;

    @Column
    @ColumnDefault("0")
    private Long plannerDailyScheduleNum;

    @Column
    private Boolean plannerIsPublic = true;

    @OneToMany(mappedBy = "planner", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Plan> plannerPlans = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "calendarIndex")
    private Calendar calendar;

    @Column
    private Long plannerDday;

    @Builder
    public Planner(LocalDate planner_date, Calendar calendar) {
        this.plannerDate = planner_date;
        this.calendar = calendar;
    }

    public void addPlan(Plan plan) {
        this.plannerPlans.add(plan);
    }
}
