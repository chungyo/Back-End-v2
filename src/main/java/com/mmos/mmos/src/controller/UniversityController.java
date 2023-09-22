package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.university.UniversityResponseDto;
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

    @ResponseBody
    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getUniversity(@RequestParam Long universityIdx) {
        UniversityResponseDto responseDto = universityService.getUniversity(universityIdx);

        return sendResponseHttpByJson(SUCCESS, "GET UNIVERSITY. UNIVERSITY_INDEX = " + universityIdx, responseDto);
    }

    @ResponseBody
    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getUniversities() {
        List<UniversityResponseDto> responseDtoList = universityService.getUniversities();

        return sendResponseHttpByJson(SUCCESS, "GET UNIVERSITIES.", responseDtoList);
    }
}
