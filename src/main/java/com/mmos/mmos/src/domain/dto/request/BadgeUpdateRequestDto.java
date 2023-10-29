package com.mmos.mmos.src.domain.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BadgeUpdateRequestDto {
    List<Long> userBadgeIdxList;
}
