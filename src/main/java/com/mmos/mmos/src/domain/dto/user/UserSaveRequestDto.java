package com.mmos.mmos.src.domain.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequestDto {

    private String id;
    private String pwd;
    private String name;
    private String nickname;
    private Long studentId;
    private Long majorIdx;   // user -> university -> major
    private String email;

}
