package com.mmos.mmos.src.domain.dto.response.study;

import com.mmos.mmos.src.domain.entity.Project;
import com.mmos.mmos.src.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSectionInProjectDto {
    // 유저 인덱스
    private Long idx;
    // 이름
    private String name;
    // 아이디
    private String id;
    // 참여 여부
    private Boolean isAttend;

    public MemberSectionInProjectDto(User user, Project project) {
        this.idx = user.getUserIndex();
        this.name = user.getUserName();
        this.id = user.getUserId();
        this.isAttend = project.getProjectIsAttend();
    }
}
