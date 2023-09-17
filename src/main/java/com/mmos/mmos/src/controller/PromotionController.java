package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.promotion.PromotionResponseDto;
import com.mmos.mmos.src.domain.entity.Promotion;
import com.mmos.mmos.src.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController extends BaseController{

    private final PromotionService promotionService;

    @ResponseBody
    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getPromotion(@RequestParam(required = false) Long promotionIdx) {
        System.out.println("promotionIdx = " + promotionIdx);
        if(promotionIdx == null){
            List<Promotion> promotionList = promotionService.findPromotions();
            System.out.println("promotionList = " + promotionList);
            return sendResponseHttpByJson(SUCCESS, "Load promotion list.", promotionList);
        }
        PromotionResponseDto responseDto = new PromotionResponseDto(promotionService.findPromotion(promotionIdx));

        return sendResponseHttpByJson(SUCCESS, "Load promotion. PROMOTION_INDEX=" + promotionIdx, responseDto);
    }


}
