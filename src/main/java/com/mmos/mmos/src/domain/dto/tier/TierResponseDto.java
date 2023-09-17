package com.mmos.mmos.src.domain.dto.tier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TierResponseDto {

    private Long tierIdx;
    private String tierName;
    private String tierImage;
    private Long tierTopRatio;

    public TierResponseDto(Long tierIdx, String tierName, String tierImage, Long tierTopRatio) {
        this.tierIdx = tierIdx;
        this.tierName = tierName;
        this.tierImage = tierImage;
        this.tierTopRatio = tierTopRatio;
    }
}
