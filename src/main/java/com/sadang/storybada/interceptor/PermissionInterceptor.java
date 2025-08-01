package com.sadang.storybada.interceptor;

import com.sadang.storybada.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        String uri = request.getRequestURI();

        if(uri.startsWith("/game")) {
            if(userDTO == null) {
                response.sendRedirect("/user/register");
                return false;
            }
        }

        if(uri.startsWith("/user/register")) {
            if(userDTO != null) {
                response.sendRedirect("/frontpage");
                return false;
            }
        }

        // 이하 미사용
//        if(userDTO == null) {
//            if(uri.startsWith("/game")) {
//                response.sendRedirect("/user/register");
//                return false;
//            }
//        } else {
//            if(uri.startsWith("/user/register")) {
//                response.sendRedirect("/frontpage");
//                return false;
//            }
//        }

        return true;
    }
}
