package com.mmos.mmos.src.domain.dto.response.study;

import com.mmos.mmos.src.domain.entity.UserStudy;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSectionInMemberDto {
    // 유저 인덱스
    private Long idx;
    // 이름
    private String name;
    // 아이디
    private String id;
    // 스터디 내 직책
    private Integer status;

    public MemberSectionInMemberDto(UserStudy userStudy) {
        this.idx = userStudy.getUser().getUserIndex();
        this.name = userStudy.getUser().getUsername();
        this.id = userStudy.getUser().getUserId();
        this.status = userStudy.getUserstudyMemberStatus();
    }
}
