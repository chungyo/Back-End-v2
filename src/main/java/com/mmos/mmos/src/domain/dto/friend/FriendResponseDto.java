package com.mmos.mmos.src.domain.dto.friend;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.entity.Friend;
import com.mmos.mmos.src.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendResponseDto {

    private Long idx;
    private Long friendIdx;
    private Boolean isFixed;
    private Integer friendStatus;
    private HttpResponseStatus status;
    private User user;

    public FriendResponseDto(Friend friend, HttpResponseStatus status) {
        this.idx = friend.getFriendIndex();
        this.isFixed = friend.getFriendIsFixed();
        this.friendStatus = friend.getFriendStatus();
        this.friendIdx = friend.getFriendUserIndex();
        this.status = status;
    }

    public FriendResponseDto(Friend friend, User user) {
        this.idx = friend.getFriendIndex();
        this.isFixed = friend.getFriendIsFixed();
        this.friendStatus = friend.getFriendStatus();
        this.friendIdx = friend.getFriendUserIndex();
        this.user = user;
    }

    public FriendResponseDto(HttpResponseStatus status) {
        this.status = status;
    }
}
