package org.example.servicelearningreporting.models;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jspecify.annotations.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
//Jaren Franklin - Very happy with the Login interceptor and Web Config
    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            return true; // user is logged in → allow request
        }

        // Not logged in → redirect
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}
