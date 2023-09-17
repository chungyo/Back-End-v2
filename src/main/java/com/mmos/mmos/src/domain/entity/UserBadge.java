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
    private Long userbadge_index;

    @ManyToOne
    @JoinColumn(name = "user_index")
    private User user;

    @ManyToOne
    @JoinColumn(name = "badge_index")
    private Badge badge;

    @Column
    private boolean userbadge_status = true;

    @Column
    private boolean userbadge_is_visible = true;


    public Long getUserbadge_index() {
        return userbadge_index;
    }

    public UserBadge(User user, Badge badge) {
        this.user = user;
        this.badge = badge;
    }
}
