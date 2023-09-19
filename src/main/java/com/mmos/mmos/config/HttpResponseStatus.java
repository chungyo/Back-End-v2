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
    POST_USER_EMPTY_USERID(false, BAD_REQUEST.value(), "아이디를 입력해주세요."),
    POST_USER_EMPTY_USERNAME(false, BAD_REQUEST.value(), "이름을 입력해주세요."),
    POST_USER_EMPTY_USERPWD(false, BAD_REQUEST.value(), "비밀번호를 입력해주세요."),
    POST_USER_EMPTY_USERSTUDENTID(false, BAD_REQUEST.value(), "학번을 입력해주세요."),
    POST_USER_EMPTY_NICKNAME(false, BAD_REQUEST.value(), "닉네임을 입력해주세요."),

    UPDATE_USER_SAME_PWD(false, BAD_REQUEST.value(), "기존 비밀번호와 같습니다. 새로운 비밀번호를 입력해주세요."),
    UPDATE_USER_DIFF_NEWPWD(false, BAD_REQUEST.value(), "새로운 비밀번호가 일치하지 않습니다."),


    // Badge
    POST_BADGE_INVALID_GET(false, BAD_REQUEST.value(), "받을 뱃지가 없습니다."),
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
