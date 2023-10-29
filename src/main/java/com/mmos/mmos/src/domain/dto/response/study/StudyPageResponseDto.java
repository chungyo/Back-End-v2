package com.mmos.mmos.src.domain.dto.response.study;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class StudyPageResponseDto {

    Page<MyStudySectionDto> myStudySection;
    Page<PromotionSectionDto> promotionSection;

    public StudyPageResponseDto(Page<MyStudySectionDto> myStudySection,
                                Page<PromotionSectionDto> promotionSection) {
        this.myStudySection = myStudySection;
        this.promotionSection = promotionSection;
    }
}
