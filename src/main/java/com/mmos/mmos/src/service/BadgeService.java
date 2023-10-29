package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.entity.Badge;
import com.mmos.mmos.src.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.DATABASE_ERROR;
import static com.mmos.mmos.config.HttpResponseStatus.EMPTY_BADGE;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    public Badge findBadgeByIdx(Long badgeId) throws BaseException {
        return badgeRepository.findById(badgeId)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_BADGE));
    }

    public List<Badge> findBadgesByPurpose(String purpose) throws BaseException  {
        return badgeRepository.findBadgesByBadgePurpose(purpose)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_BADGE));
    }

    // 휘장, 뱃지, 프사 모두 각각의 인덱스가 하나의 엔티티에서 다른 인덱스를 갖고 있으므로
    // 따로 나눌 필요 없이 index 값으로만 하면 됨
    @Transactional
    public Badge getBadge(Long badgeIdx) throws BaseException {
        try {
            return findBadgeByIdx(badgeIdx);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 휘장 전체 조회 = badge
    // 티어 전체 조회 = tier
    // 프사 전체 조회 = pfp
    @Transactional
    public List<Badge> getBadgesByPurpose(String purpose) throws BaseException {
        try {
            return findBadgesByPurpose(purpose);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
