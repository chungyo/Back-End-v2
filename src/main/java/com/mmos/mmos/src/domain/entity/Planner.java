package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mmos.mmos.src.domain.dto.planner.PlannerResponseDto;
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

    @JsonManagedReference
    @OneToMany(mappedBy = "planner", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Plan> plannerPlans = new ArrayList<>();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "calendarIndex")
    private Calendar calendar;

    @Builder
    public Planner(LocalDate planner_date, Calendar calendar) {
        this.plannerDate = planner_date;
        this.calendar = calendar;
    }

    public Planner(PlannerResponseDto responseDto) {
        this.plannerIndex = responseDto.getIdx();
        this.plannerDate = responseDto.getDate();
        this.plannerMemo = responseDto.getMemo();
        this.plannerDailyStudyTime = responseDto.getDailyStudyTime();
    }

    public void addPlan(Plan plan) {
        this.plannerPlans.add(plan);
    }

    public void setMemo(String plannerMemo) {
        this.plannerMemo = plannerMemo;
    }

    public void addTime(Long time) {
        this.plannerDailyStudyTime += time;
    }

}
