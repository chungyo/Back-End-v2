package com.mmos.mmos.src.service;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.dto.badge.BadgeResponseDto;
import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.INVALID_BADGE;
import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    public Badge findBadgeByIdx(Long badgeId) {
        return badgeRepository.findById(badgeId)
                .orElse(null);
    }

    public List<Badge> findBadgesByPurpose(String purpose) {
        return badgeRepository.findBadgesByBadgePurpose(purpose)
                .orElse(null);
    }

    // 휘장, 뱃지, 프사 모두 각각의 인덱스가 하나의 엔티티에서 다른 인덱스를 갖고 있으므로
    // 따로 나눌 필요 없이 index 값으로만 하면 됨
    @Transactional
    public BadgeResponseDto getBadge(Long badgeIdx) {
        Badge badge = findBadgeByIdx(badgeIdx);
        // 뱃지가 존재 하지 않을 때
        if(badge == null){
            return new BadgeResponseDto(HttpResponseStatus.INVALID_BADGE);
        }
        return new BadgeResponseDto(badge, SUCCESS);
    }



    // 휘장 전체 조회 = badge
    // 티어 전체 조회 = tier
    // 프사 전체 조회 = pfp
    @Transactional
    public List<BadgeResponseDto> getBadgesByPurpose(String purpose) {
        List<Badge> badgeList = findBadgesByPurpose(purpose);
        List<BadgeResponseDto> responseDtoList = new ArrayList<>();

        // 목적에 맞는 뱃지가 하나도 존재 하지 않들 때
        if( badgeList == null)
            // List에 NOT_FOUND dto 하나 추가해서 리턴
            responseDtoList.add(new BadgeResponseDto(INVALID_BADGE));
        else{
            for (Badge badge : badgeList) {
                responseDtoList.add(new BadgeResponseDto(badge, SUCCESS));
            }
        }

        return responseDtoList;
    }
}
