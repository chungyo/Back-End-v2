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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "friendUserIndex")
    private User friend;


    public Friend(Integer friendStatus, User send, User receive) {
        this.friendStatus = friendStatus;
        this.user = send;
        this.friend = receive;
    }

    public void updateStatus(Integer friendStatus) {
        this.friendStatus = friendStatus;
    }

    public void updateIsFixed(boolean isFixed) {
        this.friendIsFixed = isFixed;
    }
}
