package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.BusinessLogicException;
import com.mmos.mmos.config.exception.NotAuthorizedAccessException;
import com.mmos.mmos.src.domain.dto.request.CheckEmailDto;
import com.mmos.mmos.src.domain.dto.request.SendEmailDto;
import com.mmos.mmos.src.domain.dto.request.SignUpRequestDto;
import com.mmos.mmos.src.domain.entity.*;
import com.mmos.mmos.src.service.*;
import com.univcert.api.UnivCert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/signup")
@RequiredArgsConstructor
public class SignUpPageController extends BaseController {

    private final UserService userService;
    private final UniversityService universityService;
    private final CollegeService collegeService;
    private final MajorService majorService;
    private final CalendarService calendarService;
    private final UserBadgeService userBadgeService;
    private final FriendService friendService;

    @GetMapping("university")
    public ResponseEntity<ResponseApiMessage> getUniversities() {
        try {
            List<University> universities =  universityService.getUniversities();
            Map<Long, String> universityMap = new HashMap<>();
            for (University university : universities) {
                universityMap.put(university.getUniversityIndex(), university.getUniversityName());
            }

            return sendResponseHttpByJson(SUCCESS, "대학 목록 조회 성공", universityMap);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 이메일 인증

    @GetMapping("college/{universityIdx}")
    public ResponseEntity<ResponseApiMessage> getColleges(@PathVariable Long universityIdx) {
        try {
            List<College> colleges = collegeService.getColleges(universityIdx);
            Map<Long, String> collegeMap = new HashMap<>();
            for (College college : colleges) {
                collegeMap.put(college.getCollegeIndex(), college.getCollegeName());
            }

            return sendResponseHttpByJson(SUCCESS, "단과대 목록 조회 성공", collegeMap);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @GetMapping("major/{collegeIdx}")
    public ResponseEntity<ResponseApiMessage> getMajors(@PathVariable Long collegeIdx) {
        try {
            List<Major> majors = majorService.getMajors(collegeIdx);
            Map<Long, String> majorMap = new HashMap<>();
            for (Major major : majors) {
                majorMap.put(major.getMajorIndex(), major.getMajorName());
            }
            return sendResponseHttpByJson(SUCCESS, "전공 목록 조회 성공", majorMap);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @PostMapping("")
    public ResponseEntity<ResponseApiMessage> signUp(@RequestBody SignUpRequestDto requestDto) {
        try {
            if(!requestDto.getIsCertified())
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            User user = userService.saveUser(requestDto);
            userBadgeService.saveUserBadge(user.getUserIndex());
            calendarService.saveCalendar(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), user.getUserIndex());
            friendService.friendWithMe(user.getUserIndex());

            return sendResponseHttpByJson(SUCCESS, "회원가입 성공", user);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @PostMapping("/email")
    public ResponseEntity<ResponseApiMessage> sendCertificationNumber(@RequestBody SendEmailDto dto) {
        try {
            Map<String, Object> result = UnivCert.certify("ee5c770b-a868-49c3-9b98-1aaf42383c94", dto.getEmail(), "가천대학교", true);
            return sendResponseHttpByJson(SUCCESS, "이메일 전송 성공", result.get("code"));
        } catch (Exception e) {
            e.printStackTrace();
            return sendResponseHttpByJson(BUSINESS_LOGIC_ERROR, "서버 오류", null);
        }
    }

    @PostMapping("/email/certified")
    public ResponseEntity<ResponseApiMessage> checkCertificationNumber(@RequestBody CheckEmailDto dto) {
        try {
            Map<String, Object> result = UnivCert.certifyCode("ee5c770b-a868-49c3-9b98-1aaf42383c94", dto.getEmail(), "가천대학교", dto.getCode());
            return sendResponseHttpByJson(SUCCESS, result.get("success").toString(), result.get("code"));
        } catch (Exception e) {
            e.printStackTrace();
            return sendResponseHttpByJson(BUSINESS_LOGIC_ERROR, "서버 오류", null);
        }
    }
}
