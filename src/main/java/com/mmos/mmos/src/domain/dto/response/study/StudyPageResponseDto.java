package com.mmos.mmos.src.domain.dto.response.study;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class StudyPageResponseDto {

    Page<MyStudySectionDto> myStudySection;
    Page<PromotionSectionDto> promotionSection;
    List<RankingSectionDto> rankingSection;

    public StudyPageResponseDto(Page<MyStudySectionDto> myStudySection,
                                Page<PromotionSectionDto> promotionSection,
                                List<RankingSectionDto> rankingSection) {
        this.myStudySection = myStudySection;
        this.promotionSection = promotionSection;
        this.rankingSection = rankingSection;
    }
}
