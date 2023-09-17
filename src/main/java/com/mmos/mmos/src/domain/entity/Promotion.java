package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

/*홍보게시판*/
@Entity
@Getter
@NoArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotion_index;

    @Column
    private boolean promotion_status = true;

    @ManyToOne
    @JoinColumn(name = "study_index")
    private Study study;
}