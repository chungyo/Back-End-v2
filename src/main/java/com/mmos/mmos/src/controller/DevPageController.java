package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.src.service.CollegeService;
import com.mmos.mmos.src.service.MajorService;
import com.mmos.mmos.src.service.UniversityService;
import com.univcert.api.UnivCert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.mmos.mmos.config.HttpResponseStatus.BUSINESS_LOGIC_ERROR;
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

    @PostMapping("/email/reset")
    public ResponseEntity<ResponseApiMessage> resetCertificationEmailList() {
        try {
            UnivCert.clear("ee5c770b-a868-49c3-9b98-1aaf42383c94");
            return sendResponseHttpByJson(SUCCESS, "초기화 완료", null);
        } catch (Exception e) {
            return sendResponseHttpByJson(BUSINESS_LOGIC_ERROR, "서버 오류", null);
        }
    }

    @GetMapping("/email")
    public ResponseEntity<ResponseApiMessage> getCertificationEmailList() {
        try {
            Map<String, Object> result = UnivCert.list("ee5c770b-a868-49c3-9b98-1aaf42383c94");
            return sendResponseHttpByJson(SUCCESS, "인증된 이메일 가져오기", result.get("data"));
        } catch (Exception e) {
            return sendResponseHttpByJson(BUSINESS_LOGIC_ERROR, "서버 오류", null);
        }
    }
}
