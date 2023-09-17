package com.mmos.mmos.src.domain.dto.promotion;

import com.mmos.mmos.src.domain.entity.Promotion;
import com.mmos.mmos.src.domain.entity.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PromotionResponseDto {
    private Study study;
    public PromotionResponseDto(Promotion entity) {
        this.study = entity.getStudy();
    }

}

