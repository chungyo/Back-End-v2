package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.major.MajorResponseDto;
import com.mmos.mmos.src.domain.dto.major.MajorSaveRequestDto;
import com.mmos.mmos.src.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/majors")
@RequiredArgsConstructor
public class MajorController extends BaseController {

    private final MajorService majorService;

    // 학과 전체 조회
    @ResponseBody
    @PostMapping("/{collegeIdx}")
    public ResponseEntity<ResponseApiMessage> saveCollege(@PathVariable Long collegeIdx, @RequestBody MajorSaveRequestDto requestDto) {
        MajorResponseDto responseDto = majorService.saveMajor(collegeIdx, requestDto);

        return sendResponseHttpByJson(SUCCESS, "Saved University", responseDto);

    }
}
