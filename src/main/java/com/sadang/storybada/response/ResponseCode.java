package com.sadang.storybada.response;

import lombok.Getter;

@Getter
public enum ResponseCode {

    // 1000 성공
    SUCCESS(1000, "success"),

    // 2000 번대 - 회원가입 관련 에러
    DUPLICATE_ID(2001, "duplicate id"),
    USER_JOIN_FAIL(2002, "user join fail"),

    // 3000 번대 - 로그인 관련 에러
    USER_LOGIN_FAIL(3001, "user login fail");

    private int code;
    private String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
