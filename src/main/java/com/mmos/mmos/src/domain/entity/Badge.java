package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private Long badge_exp;

    @Column
    private Boolean is_badge_time;

    @Column
    private Boolean badge_status = true;

    @Column
    private String badge_purpose;

    @OneToMany(mappedBy = "badge", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserBadge> badge_userbadges = new ArrayList<>();

}
