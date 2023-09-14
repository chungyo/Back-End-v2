package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badge_index;

    @Column
    private String badge_name;

    @Column
    private String badge_icon;

    @Column
    private String badge_info;

    @Column
    private String badge_exp;

    @Column
    @ColumnDefault("true")
    private boolean badge_status;
}
