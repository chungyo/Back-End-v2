package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.college.CollegeResponseDto;
import com.mmos.mmos.src.domain.dto.university.UniversitySaveRequestDto;
import com.mmos.mmos.src.service.CollegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("api/v1/colleges")
@RequiredArgsConstructor
public class CollegeController extends BaseController {

    private final CollegeService collegeService;

    /**
     * 관리자 전용
     * College 초기 데이터를 저장하는 API (완료)
     * @param universityIdx (대학 인덱스)
     * @param requestDto
     *          - collegeName (단과대 이름)
     */
    @ResponseBody
    @PostMapping("/{universityIdx}")
    public ResponseEntity<ResponseApiMessage> saveCollege(@PathVariable Long universityIdx, @RequestBody UniversitySaveRequestDto requestDto) {
        CollegeResponseDto responseDto = collegeService.saveCollege(universityIdx, requestDto);

        return sendResponseHttpByJson(SUCCESS, "Saved University", responseDto);
    }
}
