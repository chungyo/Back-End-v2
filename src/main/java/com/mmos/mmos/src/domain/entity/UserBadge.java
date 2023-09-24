package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userbadgeIndex;

    @ManyToOne
    @JoinColumn(name = "userIndex")
    private User user;

    @ManyToOne
    @JoinColumn(name = "badgeIndex")
    private Badge badge;

    @Column
    private Boolean userbadgeIsVisible = true;

    public UserBadge(User user, Badge badge) {
        this.user = user;
        this.badge = badge;
    }
}
