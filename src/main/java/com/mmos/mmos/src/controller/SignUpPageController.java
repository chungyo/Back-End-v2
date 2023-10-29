package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.src.domain.dto.request.SignUpRequestDto;
import com.mmos.mmos.src.domain.entity.College;
import com.mmos.mmos.src.domain.entity.Major;
import com.mmos.mmos.src.domain.entity.University;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpPageController extends BaseController {

    private final UserService userService;
    private final UniversityService universityService;
    private final CollegeService collegeService;
    private final MajorService majorService;
    private final CalendarService calendarService;

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

    @GetMapping("college")
    public ResponseEntity<ResponseApiMessage> getColleges(@RequestParam Long universityIdx) {
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

    @GetMapping("major")
    public ResponseEntity<ResponseApiMessage> getMajors(@RequestParam Long collegeIdx) {
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
            User user = userService.saveUser(requestDto);

            calendarService.saveCalendar(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), user.getUserIndex());
            return sendResponseHttpByJson(SUCCESS, "회원가입 성공", user);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }
}