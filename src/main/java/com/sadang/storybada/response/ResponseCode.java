package com.sadang.storybada.response;

import lombok.Getter;

@Getter
public enum ResponseCode {

    // 1000 성공
    SUCCESS(1000, "success"),

    // 2000 번대 - 회원가입 관련 에러
    DUPLICATE_ID(2001, "duplicate id"),
    DUPLICATE_EMAIL(2002, "duplicate email"),
    USER_JOIN_FAIL(2003, "user join fail"),

    // 3000 번대 - 유저 정보 관련 에러
    USER_LOGIN_FAIL(3001, "user login fail"),
    USER_PASSWORD_INCORRECT(3002, "user password incorrect"),

    // 4000 번대 - 게임 관련 에러
    POINT_NOT_ENOUGH(4001, "point is not enough"),
    DICE_REVERSE_BETTING(4002, "you have bet opposite side"),
    LOTTO_ALREADY_BOUGHT(4003, "you have bought a lotto");


    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
