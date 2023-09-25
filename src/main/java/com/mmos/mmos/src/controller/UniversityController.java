package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.university.UniversityResponseDto;
import com.mmos.mmos.src.domain.dto.university.UniversitySaveRequestDto;
import com.mmos.mmos.src.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/api/v1/universities")
@RequiredArgsConstructor
public class UniversityController extends BaseController {
    private final UniversityService universityService;

    /**
     * 관리자 전용
     * University 객체 생성하는 API (완료)
     * @param requestDto
     *          - universityName
     */
    @ResponseBody
    @PostMapping("")
    public ResponseEntity<ResponseApiMessage> saveUniversity(@RequestBody UniversitySaveRequestDto requestDto) {
        UniversityResponseDto responseDto = universityService.saveUniversity(requestDto);

        return sendResponseHttpByJson(SUCCESS, "GET UNIVERSITY.", responseDto);
    }

    /**
     * University 인덱스로 단일 조회하는 API (완료)
     * @param universityIdx (대학 인덱스)
     */
    @ResponseBody
    @GetMapping("/{universityIdx}")
    public ResponseEntity<ResponseApiMessage> getUniversity(@PathVariable Long universityIdx) {
        UniversityResponseDto responseDto = universityService.getUniversity(universityIdx);

        return sendResponseHttpByJson(SUCCESS, "GET UNIVERSITY. UNIVERSITY_INDEX = " + universityIdx, responseDto);
    }

    /**
     * 모든 University를 조건 없이 조회하는 API (완료)
     */
    @ResponseBody
    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getUniversities() {
        List<UniversityResponseDto> responseDtoList = universityService.getUniversities();

        return sendResponseHttpByJson(SUCCESS, "GET UNIVERSITIES.", responseDtoList);
    }
}
