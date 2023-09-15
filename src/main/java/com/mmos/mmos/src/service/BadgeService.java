package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.badge.BadgeResponseDto;
import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    public Badge findBadge(Long badgeId) {
        return badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 뱃지가 존재하지 않습니다. BADGE_ID=" + badgeId));
    }

    // 휘장 ID로 조회
    @Transactional
    public BadgeResponseDto findById(Long badgeId) {
        Badge badge = findBadge(badgeId);

        return new BadgeResponseDto(badge);
    }
}
