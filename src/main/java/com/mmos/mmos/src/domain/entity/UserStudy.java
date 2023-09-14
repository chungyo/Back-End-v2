package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
public class UserStudy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userstudy_index;

    @Column
    @ColumnDefault("true")
    private Boolean userstudy_status;

    @ManyToOne
    @JoinColumn(name = "user_index")
    private User user;

    @ManyToOne
    @JoinColumn(name = "study_index")
    private Study study;
}
