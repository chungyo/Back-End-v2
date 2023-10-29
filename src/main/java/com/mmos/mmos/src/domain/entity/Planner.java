package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
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

    public Planner(LocalDate planner_date, Calendar calendar) {
        this.plannerDate = planner_date;
        this.calendar = calendar;
    }



//    public Planner(PlannerResponseDto responseDto) {
//        this.plannerIndex = responseDto.getIdx();
//        this.plannerDate = responseDto.getDate();
//        this.plannerDailyStudyTime = responseDto.getDailyStudyTime();
//    }

    public void addPlan(Plan plan) {
        this.plannerPlans.add(plan);
    }

    public void addTime(Long time) {
        this.plannerDailyStudyTime += time;
    }

    public void minusTime(Long time) {
        this.plannerDailyStudyTime -= time;
    }

    public void updateDailyScheduleNum(Boolean status){
        if(status){
            this.plannerDailyScheduleNum++;
        }
        else{
            this.plannerDailyScheduleNum--;
        }
    }
}
