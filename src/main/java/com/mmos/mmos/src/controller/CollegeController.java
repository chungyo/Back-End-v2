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

    @ResponseBody
    @PostMapping("/{universityIdx}")
    public ResponseEntity<ResponseApiMessage> saveCollege(@PathVariable Long universityIdx, @RequestBody UniversitySaveRequestDto requestDto) {
        CollegeResponseDto responseDto = collegeService.saveCollege(universityIdx, requestDto);

        return sendResponseHttpByJson(SUCCESS, "Saved University", responseDto);

    }
}
