package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
    private Long calendarIndex;

    @Column
    private int calendarMonth;

    @Column
    @ColumnDefault("0")
    private Long calendarMonthlyStudyTime;

    @Column
    @ColumnDefault("0")
    private Long calendarMonthlyCompletedPlanNum;

    @JsonManagedReference
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Planner> calendarPlanners = new ArrayList<>();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userIndex")
    private User user;

    public Calendar(int month, User user) {
        this.calendarMonth = month;
        this.user = user;
    }

    public void addPlanner(Planner planner) {
        this.calendarPlanners.add(planner);
    }

    public void addTime(Long time) {
        this.calendarMonthlyStudyTime += time;
    }
}
