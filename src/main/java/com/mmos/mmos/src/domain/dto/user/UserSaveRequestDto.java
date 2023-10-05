package com.mmos.mmos.src.domain.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSaveRequestDto {

    private String id;
    private String pwd;
    private String name;
    private String nickname;
    private Long studentId;
    private Long majorIdx;   // user -> university -> major
    private String email;

    public UserSaveRequestDto(UserSaveRequestDto requestDto, String encryptedPwd) {
        this.id = requestDto.id;
        this.pwd = encryptedPwd;
        this.name = requestDto.getName();
        this.nickname = requestDto.nickname;
        this.studentId = requestDto.studentId;
        this.majorIdx = requestDto.majorIdx;
        this.email = requestDto.email;
    }
}
