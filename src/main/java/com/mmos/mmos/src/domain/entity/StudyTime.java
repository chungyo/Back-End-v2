package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
public class StudyTime {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studytimeIndex;

    @Column
    @ColumnDefault("current_timestamp")
    private Timestamp studytimeStartTime;

    @Column
    @ColumnDefault("current_timestamp on update current_timestamp")
    private Timestamp studytimeEndTime;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "planIndex")
    private Plan plan;

    public StudyTime(Timestamp studytime_start_time, Timestamp studytime_end_time, Plan plan) {
        this.studytimeStartTime = studytime_start_time;
        this.studytimeEndTime = studytime_end_time;
        this.plan = plan;
    }

    public void updateEndTime(Timestamp endTime) {
        this.studytimeEndTime = endTime;
    }
}
