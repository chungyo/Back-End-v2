package com.mmos.mmos.src.domain.dto.notice;

import com.mmos.mmos.src.domain.entity.Notice;
import com.mmos.mmos.src.domain.entity.Study;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoticeResponseDto {

    private Study study;

    public NoticeResponseDto(Notice notice) {
        this.study = notice.getStudy();
    }
}
