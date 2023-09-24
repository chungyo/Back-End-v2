package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.src.domain.dto.calendar.CalendarResponseDto;
import com.mmos.mmos.src.domain.dto.planner.PlannerResponseDto;
import com.mmos.mmos.src.domain.dto.study.StudyResponseDto;
import com.mmos.mmos.src.domain.dto.user.UserNicknameUpdateDto;
import com.mmos.mmos.src.domain.dto.user.UserPwdUpdateDto;
import com.mmos.mmos.src.domain.dto.user.UserResponseDto;
import com.mmos.mmos.src.domain.dto.user.UserSaveRequestDto;
import com.mmos.mmos.src.service.CalendarService;
import com.mmos.mmos.src.service.PlannerService;
import com.mmos.mmos.src.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;
    private final CalendarService calendarService;
    private final PlannerService plannerService;

    // 회원가입
    @ResponseBody
    @PostMapping("")
    public ResponseEntity<ResponseApiMessage> saveUser(@RequestBody UserSaveRequestDto requestDto) {
        // null 검사
        if(requestDto.getId() == null) {
            return sendResponseHttpByJson(POST_USER_EMPTY_USERID, "EMPTY USER_ID.", requestDto);
        }
        if(requestDto.getName() == null) {
            return sendResponseHttpByJson(POST_USER_EMPTY_USERNAME, "EMPTY USER_NAME.", requestDto);
        }
        if(requestDto.getPwd() == null) {
            return sendResponseHttpByJson(POST_USER_EMPTY_USERPWD, "EMPTY USER_PWD.", requestDto);
        }
        if(requestDto.getStudentId() == null) {
            return sendResponseHttpByJson(POST_USER_EMPTY_USERSTUDENTID, "EMPTY USER_STUDENT_ID.", requestDto);
        }
        if(requestDto.getNickname() == null) {
            return sendResponseHttpByJson(POST_USER_EMPTY_NICKNAME, "EMPTY USER_NICKNAME.", requestDto);
        }
        if(requestDto.getMajorIdx() == null) {
            return sendResponseHttpByJson(POST_USER_EMPTY_NICKNAME, "EMPTY MAJOR_INDEX.", requestDto);
        }

        // Regex 검사(학번, 이름, 이메일, 닉네임)


        // 비밀번호 암호화


        // User 생성
        UserResponseDto userResponseDto = userService.saveUser(requestDto);
        if(userResponseDto == null)
            return sendResponseHttpByJson(SUCCESS, "SAVE USER FAIL. USER_INDEX=", null);

        // Calendar 생성
        CalendarResponseDto calendarResponseDto = calendarService.saveCalendar(LocalDate.now().getMonthValue(), userResponseDto.getIdx());

        // Planner 생성
        PlannerResponseDto plannerResponseDto = plannerService.savePlanner(LocalDate.now(), calendarResponseDto.getIdx());

        return sendResponseHttpByJson(SUCCESS, "SAVE USER. USER_INDEX=" + userResponseDto.getIdx(), userResponseDto);
    }

    @ResponseBody
    @GetMapping("/{userIdx}/studies")
    public ResponseEntity<ResponseApiMessage> getStudies(@PathVariable Long userIdx) {
        List<StudyResponseDto> studyResponseDtoList = userService.getStudies(userIdx);

        return sendResponseHttpByJson(SUCCESS, "GET STUDIES. USER_INDEX=" + userIdx, studyResponseDtoList);
    }

        // 비밀번호 변경
    @ResponseBody
    @PatchMapping("/{userIdx}/password")
    public ResponseEntity<ResponseApiMessage> updatePwd(@RequestBody UserPwdUpdateDto userPwdUpdateDto, @PathVariable Long userIdx) {
        // null 검사
        if(userPwdUpdateDto.getPrevPwd() == null)
            return sendResponseHttpByJson(UPDATE_USER_EMPTY_PREVPWD, "PASSWORD UPDATE FAIL. PREV_PWD == NULL", null);
        if(userPwdUpdateDto.getNewPwd() == null)
            return sendResponseHttpByJson(UPDATE_USER_EMPTY_NEWPWD, "PASSWORD UPDATE FAIL. NEW_PWD == NULL", null);
        if (userPwdUpdateDto.getNewPwdByCheck() == null)
            return sendResponseHttpByJson(UPDATE_USER_DIFF_NEWPWD, "PASSWORD UPDATE FAIL. NEW_PWD_CHECK == NULL", null);

        // 기존 비밀번호와 새로운 비밀번호가 다른지 확인
        if(userPwdUpdateDto.getPrevPwd().equals(userPwdUpdateDto.getNewPwd()))
            return sendResponseHttpByJson(UPDATE_USER_SAME_PWD, "PASSWORD UPDATE FAIL. (PREV_PWD == NEW PWD)", null);
        // 새로운 비밀번호와 확인 비밀번호가 같은지 확인
        if(!userPwdUpdateDto.getNewPwd().equals(userPwdUpdateDto.getNewPwdByCheck()))
            return sendResponseHttpByJson(UPDATE_USER_DIFF_NEWPWD, "PASSWORD UPDATE FAIL. (NEW_PWD != CHECK NEW PWD)", null);

        UserResponseDto userResponseDto = userService.updatePwd(userPwdUpdateDto, userIdx);

        if(userResponseDto == null)
            return sendResponseHttpByJson(UPDATE_USER_DIFF_PREVPWD, "UPDATE PASSWORD FAIL. DB PWD != PREV_PWD", null);
        return sendResponseHttpByJson(SUCCESS, "UPDATE PASSWORD. USER_IDX=" + userIdx, userResponseDto);
    }

    // 닉네임 변경
    @ResponseBody
    @PatchMapping("/{userIdx}/nickname")
    public ResponseEntity<ResponseApiMessage> updateNickname(@RequestBody UserNicknameUpdateDto userNicknameUpdateDto, @PathVariable Long userIdx) {
        // null 검사
        if(userNicknameUpdateDto.getNewNickname() == null)
            return sendResponseHttpByJson(UPDATE_USER_EMPTY_NICKNAME, "UPDATE NICKNAME FAIL. NICKNAME == NULL" + userIdx, null);

        UserResponseDto responseDto = userService.updateNickname(userNicknameUpdateDto, userIdx);

        if(responseDto == null)
            return sendResponseHttpByJson(UPDATE_USER_DUPLICATE_NICKNAME, "UPDATE NICKNAME FAIL. DUPLICATED NICKNAME" + userIdx, null);
        return sendResponseHttpByJson(SUCCESS, "UPDATE NICKNAME. USER_IDX=" + userIdx, responseDto);
    }
}
