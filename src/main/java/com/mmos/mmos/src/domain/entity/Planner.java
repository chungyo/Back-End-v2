package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Planner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planner_index;

    @Column
    private Date planner_date;

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
    @ColumnDefault("true")
    private boolean planner_is_public;

    @Column
    @ColumnDefault("true")
    private boolean planner_status;

    @OneToMany(mappedBy = "planner", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ColumnDefault("true")
    private List<Plan> planner_plans;

    @ManyToOne
    @JoinColumn(name = "user_index")
    private User user;

    @ManyToOne
    @JoinColumn(name = "calendar_index")
    private Calendar calendar;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Weather weather;

    @Column
    private Long planner_dday;

}
