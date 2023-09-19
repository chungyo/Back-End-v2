package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.entity.Promotion;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.repository.PromotionRepository;
import com.mmos.mmos.src.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final StudyRepository studyRepository;

    //홍보 게시글 조회
    public Promotion findPromotion(Long promotionId) {
        return promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 홍보 게시글이 존재하지 않습니다. PROMOTION_ID=" + promotionId));
    }

    public List<Promotion> findPromotions(){
        return promotionRepository.findAll();
    }

    public Study findStudy(Long studyIdx) {
        return studyRepository.findById(studyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. STUDY_INDEX=" + studyIdx));
    }


    // 공지 생성
    @Transactional
    public Promotion savePromotion(Long studyIdx) {
        Study study = findStudy(studyIdx);
        Promotion promotion = new Promotion(study);

        return promotionRepository.save(promotion);
    }
}
