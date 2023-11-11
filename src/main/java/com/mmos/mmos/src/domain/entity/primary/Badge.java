package com.mmos.mmos.src.domain.entity.primary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column
    private Integer badgeLevel; // 뱃지 획득 난이도
}
