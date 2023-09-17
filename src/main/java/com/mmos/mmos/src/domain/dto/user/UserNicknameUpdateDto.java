package com.mmos.mmos.src.domain.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserNicknameUpdateDto {

    private String prevNickname;
    private String newNickname;

}
