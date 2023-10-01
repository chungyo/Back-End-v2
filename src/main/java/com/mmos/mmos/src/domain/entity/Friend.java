package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Integer friendStatus;

    // 내 역매핑
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userIndex")
    private User user;

    // 친구 인덱스
    @Column
    private Long friendUserIndex;

    public Friend(Integer friendStatus, Long friendIndex, User user) {
        this.friendStatus = friendStatus;
        this.friendUserIndex = friendIndex;
        this.user = user;
    }

    public void updateStatus(Integer friendStatus) {
        this.friendStatus = friendStatus;
    }

    public void updateIsFixedToTrue() {
        this.friendIsFixed = true;
    }

    public void updateIsFixedToFalse() {
        this.friendIsFixed = false;
    }
}
