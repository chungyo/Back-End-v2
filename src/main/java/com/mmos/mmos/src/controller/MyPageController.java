package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.src.domain.dto.request.UpdatePwdRequestDto;
import com.mmos.mmos.src.domain.dto.request.UserDeleteRequestDto;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserBadge;
import com.mmos.mmos.src.service.UserBadgeService;
import com.mmos.mmos.src.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mmos.mmos.config.HttpResponseStatus.*;
import static com.mmos.mmos.utils.SHA256.encrypt;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class MyPageController extends BaseController {

    private final UserService userService;
    private final UserBadgeService userBadgeService;

    // 페이지 로드

    // 프사 변경
    @PatchMapping("/pfp")
    public ResponseEntity<ResponseApiMessage> updatePfp(@RequestParam Long userIdx, @RequestParam Long pfpIdx) {
        try {
            User user = userService.getUser(userIdx);
            // 기존 프사 찾기
            System.out.println("visible pfp: " +userBadgeService.getRepresentBadges(userIdx, "pfp"));
            UserBadge prevPfp = userBadgeService.getRepresentBadges(userIdx, "pfp").get(0);
            // 새 프사 찾기
            UserBadge newPfp = null;
            boolean isExist = false;
            for (UserBadge userUserbadge : user.getUserUserbadges()) {
                if(userUserbadge.getBadge().getBadgeIndex().equals(pfpIdx)) {
                    newPfp = userUserbadge;
                    isExist = true;
                }
            } if(!isExist)
                throw new BaseException(EMPTY_USERBADGE);

            // 이미 대표 프사인 뱃지와 선택한 뱃지가 같다면 변경 성공했다는 문구는 뜨지만 사실 업데이트 되진 않았음
            if(prevPfp.equals(newPfp))
                return sendResponseHttpByJson(SUCCESS, "프로필 사진 변경 성공", null);

            userBadgeService.updatePfp(prevPfp, false);
            userBadgeService.updatePfp(newPfp, true);

            return sendResponseHttpByJson(SUCCESS, "프로필 사진 변경 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 아이디 변경
    @PatchMapping("/id")
    public ResponseEntity<ResponseApiMessage> updateId(@RequestParam Long userIdx, @RequestParam String id) {
        try {
            User user = userService.getUser(userIdx);
            // 기존 아이디를 가져오기
            if(user.getUserId().equals(id))
                // 변경 성공했다는 문구는 뜨지만 사실 업데이트 되진 않았음
                return sendResponseHttpByJson(SUCCESS, "아이디 변경 성공", null);
            // 아이디 변경
            userService.updateId(user, id);

            return sendResponseHttpByJson(SUCCESS, "아이디 변경 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 이름 변경
    @PatchMapping("/name")
    public ResponseEntity<ResponseApiMessage> updateName(@RequestParam Long userIdx, @RequestParam String name) {
        try {
            User user = userService.getUser(userIdx);
            // 기존 이름을 가져오기
            if(user.getUsername().equals(name))
                // 변경 성공했다는 문구는 뜨지만 사실 업데이트 되진 않았음
                return sendResponseHttpByJson(SUCCESS, "이름 변경 성공", null);
            // 이름 변경
            userService.updateName(user, name);

            return sendResponseHttpByJson(SUCCESS, "이름 변경 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 비밀번호 변경
    @PatchMapping("/pwd")
    public ResponseEntity<ResponseApiMessage> updatePwd(@RequestParam Long userIdx, @RequestBody UpdatePwdRequestDto requestDto) {
        try {
            User user = userService.getUser(userIdx);
            String prevPwd = encrypt(requestDto.getPrevPwd());
            String newPwd = encrypt(requestDto.getNewPwd());

            if(requestDto.getPrevPwd().isEmpty()
                    || requestDto.getNewPwd().isEmpty()
                    || requestDto.getCheckPwd().isEmpty())
                throw new BaseException(UPDATE_USER_EMPTY_PWD);
            if(!user.getPassword().equals(prevPwd))
                throw new BaseException(UPDATE_USER_DIFF_PREVPWD);
            if(!requestDto.getNewPwd().equals(requestDto.getCheckPwd()))
                throw new BaseException(UPDATE_USER_DIFF_NEWPWD);

            // 비밀번호 변경
            userService.updatePwd(user, newPwd);

            return sendResponseHttpByJson(SUCCESS, "비밀번호 변경 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 플래너 공개 설정
    @PatchMapping("/planner")
    public ResponseEntity<ResponseApiMessage> updateIsPlannerVisible(@RequestParam Long userIdx) {
        try {
            User user = userService.getUser(userIdx);
            user.updateIsPlannerVisible(!user.getIsPlannerVisible());

            return sendResponseHttpByJson(SUCCESS, "플래너 공개 여부 변경 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 이메일 인증

    // 이메일 변경

    // 회원 탈퇴
    @DeleteMapping("")
    public ResponseEntity<ResponseApiMessage> deleteUser(@RequestParam Long userIdx, @RequestBody UserDeleteRequestDto requestDto) {
        try {
            User user = userService.getUser(userIdx);
            if(user.getUserPassword().equals(requestDto.getPwd()) || user.getUserPassword().equals(requestDto.getCheckPwd()))
                throw new BaseException(UPDATE_USER_DIFF_PREVPWD);

            userService.deleteUser(user);

            return sendResponseHttpByJson(SUCCESS, "회원 탈퇴 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }
}
