package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.badge.BadgeResponseDto;
import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    public Badge findBadgeByIdx(Long badgeId) {
        return badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 뱃지가 존재하지 않습니다. BADGE_ID=" + badgeId));
    }

    public List<Badge> findBadgesByPurpose(String purpose) {
        return badgeRepository.findBadgesByBadgePurpose(purpose)
                .orElseThrow(() -> new IllegalArgumentException("휘장 == 0, 티어 == 1, 프사 == 2 다시 검색해주세요. 현재 입력=" + purpose));
    }

    // 휘장, 뱃지, 프사 모두 각각의 인덱스가 하나의 엔티티에서 다른 인덱스를 갖고 있으므로
    // 따로 나눌 필요 없이 index 값으로만 하면 됨
    @Transactional
    public BadgeResponseDto getBadge(Long badgeIdx) {
        Badge badge = findBadgeByIdx(badgeIdx);

        return new BadgeResponseDto(badge);
    }



    // 휘장 전체 조회 = badge
    // 티어 전체 조회 = tier
    // 프사 전체 조회 = pfp
    @Transactional
    public List<BadgeResponseDto> getBadgesByPurpose(String purpose) {
        List<Badge> badgeList = findBadgesByPurpose(purpose);
        List<BadgeResponseDto> responseDtoList = new ArrayList<>();

        for (Badge badge : badgeList) {
            responseDtoList.add(new BadgeResponseDto(badge));
        }

        return responseDtoList;
    }
}
