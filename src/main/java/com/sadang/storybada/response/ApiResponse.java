package com.sadang.storybada.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiResponse<T> {

    // 성공 실패 여부, 코드, 메세지
    // 성공시 추가 데이터
    private String result;
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>("success", ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    public static <T> ApiResponse<T> fail(ResponseCode code) {
        return new ApiResponse<>("fail", code.getCode(), code.getMessage(), null);
    }

}
