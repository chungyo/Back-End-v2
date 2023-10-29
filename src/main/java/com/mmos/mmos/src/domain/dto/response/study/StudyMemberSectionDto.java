package com.mmos.mmos.src.domain.dto.response.study;

import com.mmos.mmos.src.domain.entity.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StudyMemberSectionDto {

    // 스터디 최대 인원
    private Integer maximumMemberNum;
    // 스터디 현재 인원
    private Integer totalMemberNum;
    // 멤버 리스트
    private List<MemberSectionInMemberDto> memberSection;

    public StudyMemberSectionDto(Study study, List<MemberSectionInMemberDto> memberSection) {
        this.maximumMemberNum = study.getStudyMemberLimit();
        this.totalMemberNum = study.getStudyMemberNum();
        this.memberSection = memberSection;
    }
}
