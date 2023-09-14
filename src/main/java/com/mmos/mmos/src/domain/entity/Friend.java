package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friend_index;

    @Column
    private boolean friend_is_fixed;

    @Column
    @ColumnDefault("true")
    private boolean friend_status;

    @ManyToOne
    @JoinColumn(name = "user_index")
    private User user;
}
