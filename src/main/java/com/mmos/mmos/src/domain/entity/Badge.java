package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Long badgeIndex;

    @Column
    private String badgeName;

    @Column
    private String badgeIcon;

    @Column
    private String badgeInfo;

    @Column
    private Long badgeExp;

    @Column
    private Boolean isBadgeTime;

    @Column
    private String badgePurpose;

    @JsonManagedReference
    @OneToMany(mappedBy = "badge", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserBadge> badgeUserbadges = new ArrayList<>();

}
