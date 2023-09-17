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
    private Long planner_index;

    @Column
    private LocalDate planner_date;

    @Column
    @ColumnDefault("null")
    private String planner_memo;

    @Column
    @ColumnDefault("0")
    private Long planner_daily_study_time;

    @Column
    @ColumnDefault("0")
    private Long planner_daily_schedule_num;

    @Column
    private boolean planner_is_public = true;

    @Column
    private boolean planner_status = true;

    @OneToMany(mappedBy = "planner", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Plan> planner_plans = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "calendar_index")
    private Calendar calendar;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Weather weather;

    @Column
    private Long planner_dday;

    @Builder
    public Planner(LocalDate planner_date, Calendar calendar) {
        this.planner_date = planner_date;
        this.calendar = calendar;
    }
}
