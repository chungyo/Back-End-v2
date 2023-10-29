package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.src.service.CollegeService;
import com.mmos.mmos.src.service.MajorService;
import com.mmos.mmos.src.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
public class DevPageController extends BaseController {

    private final CollegeService collegeService;
    private final UniversityService universityService;
    private final MajorService majorService;

    @PostMapping("/university")
    public ResponseEntity<ResponseApiMessage> saveUniversity(@RequestParam String name) {
        try {
            return sendResponseHttpByJson(SUCCESS, "대학 저장 성공",
                    universityService.saveUniversity(name));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @PostMapping("/college")
    public ResponseEntity<ResponseApiMessage> saveCollege(@RequestParam Long universityIdx,
                                                          @RequestParam String name) {
        try {
            return sendResponseHttpByJson(SUCCESS, "단과대 저장 성공",
                    collegeService.saveCollege(universityIdx, name));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @PostMapping("/major")
    public ResponseEntity<ResponseApiMessage> saveMajor(@RequestParam Long collegeIdx,
                                                          @RequestParam String name) {
        try {
            return sendResponseHttpByJson(SUCCESS, "전공 저장 성공",
                    majorService.saveMajor(collegeIdx, name));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }
}
