package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long major_index;

    @Column
    private Long major_name;

    @Column
    private Boolean major_status = true;

    @ManyToOne
    @JoinColumn(name = "univ_index")
    private University university;
}
