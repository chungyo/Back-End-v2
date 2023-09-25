package com.mmos.mmos.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public enum HttpResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),


    /**
     * 400 : Request 오류, Response 오류
     */
    // Common
    REQUEST_ERROR(false, BAD_REQUEST.value(), "입력값을 확인해주세요."),
    EMPTY_JWT(false, HttpStatus.UNAUTHORIZED.value(), "JWT를 입력해주세요."),
    INVALID_JWT(false, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,HttpStatus.FORBIDDEN.value(),"권한이 없는 유저의 접근입니다."),
    RESPONSE_ERROR(false, HttpStatus.NOT_FOUND.value(), "값을 불러오는데 실패하였습니다."),




    // User
    // saveUser
    POST_USER_EMPTY_USERID(false, BAD_REQUEST.value(), "아이디를 입력해주세요."),
    POST_USER_EMPTY_USERNAME(false, BAD_REQUEST.value(), "이름을 입력해주세요."),
    POST_USER_EMPTY_USERPWD(false, BAD_REQUEST.value(), "비밀번호를 입력해주세요."),
    POST_USER_EMPTY_USERSTUDENTID(false, BAD_REQUEST.value(), "학번을 입력해주세요."),
    POST_USER_EMPTY_NICKNAME(false, BAD_REQUEST.value(), "닉네임을 입력해주세요."),
    POST_USER_EMPTY_UNIVERSITYID(false, BAD_REQUEST.value(), "대학을 선택해주세요."),
    POST_USER_EMPTY_MAJORID(false, BAD_REQUEST.value(), "학과(전공)를 선택해주세요."),
    POST_USER_DUPLICATE_SAVE(false, BAD_REQUEST.value(), "이미 존재하는 회원입니다."),
    // updatePwd
    UPDATE_USER_EMPTY_PREVPWD(false, BAD_REQUEST.value(), "이전 비밀번호를 입력해주세요."),
    UPDATE_USER_EMPTY_NEWPWD(false, BAD_REQUEST.value(), "새 비밀번호를 입력해주세요."),
    UPDATE_USER_SAME_PWD(false, BAD_REQUEST.value(), "기존 비밀번호와 같습니다. 새로운 비밀번호를 입력해주세요."),
    UPDATE_USER_DIFF_NEWPWD(false, BAD_REQUEST.value(), "새로운 비밀번호가 일치하지 않습니다."),
    UPDATE_USER_DIFF_PREVPWD(false, BAD_REQUEST.value(), "비밀번호를 잘못 입력하셨습니다."),
    // updateNickname
    UPDATE_USER_EMPTY_NICKNAME(false, BAD_REQUEST.value(), "닉네임을 입력해주세요."),
    UPDATE_USER_DUPLICATE_NICKNAME(false, BAD_REQUEST.value(), "이전에 사용하던 닉네임과 같습니다. 새로운 닉네임을 입력해주세요."),


    // Badge
    POST_BADGE_INVALID_REQUEST(false, BAD_REQUEST.value(), "받을 뱃지가 없습니다."),

    // UserBadge
    GET_USERBADGE_EMPTY_LIST(false, BAD_REQUEST.value(), "갖고있는 뱃지가 없습니다."),


    // Calendar
    POST_CALENDAR_INVALID_REQUEST(false, BAD_REQUEST.value(), "한 달이 지나지 않아서 저장할 캘린더가 없습니다."),

    // plan
    POST_PLAN_EMPTY_CONTENTS(false, BAD_REQUEST.value(),"계획 내용이 없습니다."),
    POST_PLAN_INVALID_REQUEST(false, BAD_REQUEST.value(),"스터디 계획이 아닙니다."),

    // UserStudy
    POST_USERSTUDY_DUPLICATE_REQUEST(false, BAD_REQUEST.value(),"이미 초대 요청을 보냈습니다."),
    POST_USERSTUDY_ALREADY_EXIST(false, BAD_REQUEST.value(),"이미 활동 중인 멤버입니다."),
    POST_USERSTUDY_INVALID_REQUEST(false, BAD_REQUEST.value(),"권한이 없습니다."),
    POST_USERSTUDY_COMPLETE_REQUEST(false, BAD_REQUEST.value(),"이미 받은 요청입니다."),

    // Planner
    POST_PLANNER_INVALID_REQUEST(false, BAD_REQUEST.value(), "하루가 지나지 않아서 저장할 플래너가 없습니다."),

    // Study
    UPDATE_STUDY_EMPTY_NAME(false, BAD_REQUEST.value(), "스터디이름을 입력해주세요."),
    UPDATE_STUDY_DUPLICATE_NAME(false, BAD_REQUEST.value(), "이전 이름과 같습니다. 새로운 이름을 입력해주세요."),
    UPDATE_STUDY_ALREADY_COMPLETE(false, BAD_REQUEST.value(), "이미 완수한 스터디입니다."),

    // StudyTime
    POST_STUDYTIME_DUPLICATE_REQUEST(false, BAD_REQUEST.value(), "이미 진행 중인 계획이 있습니다."),
    POST_STUDYTIME_INVALID_REQUEST(false, BAD_REQUEST.value(), "진행 중인 계획이 없습니다."),

    /**
     * 500 : Database, Server 오류
     */
    DATABASE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버와의 연결에 실패하였습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 복호화에 실패하였습니다."),

    // Post
    POST_POST_EMPTY_TITLE(false, BAD_REQUEST.value(), "글 제목을 입력해주세요."),
    POST_POST_EMPTY_CONTENTS(false, BAD_REQUEST.value(), "글 내용을 입력해주세요.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private HttpResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
