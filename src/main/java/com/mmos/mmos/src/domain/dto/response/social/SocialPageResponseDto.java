package com.mmos.mmos.src.domain.dto.response.social;

import com.mmos.mmos.src.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SocialPageResponseDto {
    List<RankingSectionDto> ranking = new ArrayList<>();
    List<FriendSectionDto> friend = new ArrayList<>();

    public SocialPageResponseDto(List<User> friends, List<User> top3) {
        for (User user : top3) {
            this.ranking.add(new RankingSectionDto(user));
        }

        for (User user : friends) {
            this.friend.add(new FriendSectionDto(user));
        }
    }
}
