package com.mmos.mmos.src.domain.entity;

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
    private Long studytime_index;
    @Column
    @ColumnDefault("current_timestamp")
    private Timestamp studytime_start_time;

    @Column
    @ColumnDefault("current_timestamp on update current_timestamp")
    private Timestamp studytime_end_time;

    @Column
    @ColumnDefault("true")
    private boolean studytime_status;

    @ManyToOne
    @JoinColumn(name = "plan_index")
    private Plan plan;



}
