package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.entity.Promotion;
import com.mmos.mmos.src.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;

    //홍보 게시글 조회
    public Promotion findPromotion(Long promotionId) {
        return promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 홍보 게시글이 존재하지 않습니다. PROMOTION_ID=" + promotionId));
    }
    public List<Promotion> findPromotions(){
        return promotionRepository.findAll();
    }


}
