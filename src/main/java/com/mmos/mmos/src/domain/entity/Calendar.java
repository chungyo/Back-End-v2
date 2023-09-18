package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@NoArgsConstructor
@DynamicInsert
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendar_index;

    @Column
    private int calendar_month;

    @Column
    private Boolean calendar_status = true;

    @Column
    @ColumnDefault("0")
    private Long calendar_monthly_study_time;

    @Column
    @ColumnDefault("0")
    private Long calendar_monthly_completed_plan_num;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Planner> calendar_planners = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_index")
    private User user;

    @Builder
    public Calendar(int month, User user) {
        this.calendar_month = month;
        this.user = user;
    }

    public void addPlanner(Planner planner) {
        this.calendar_planners.add(planner);
    }
}
