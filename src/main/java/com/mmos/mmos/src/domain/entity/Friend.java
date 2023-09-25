package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendIndex;

    @Column
    private Boolean friendIsFixed = false;

    /*
        1: 친구
        2: 내가 친구 요청을 보냄 (send)
        3: 상대방이 나에게 친구 요청을 보냄 (receive)
    */
    @Column
    private int friendStatus;

    @ManyToOne
    @JoinColumn(name = "userIndex")
    private User user;
}
