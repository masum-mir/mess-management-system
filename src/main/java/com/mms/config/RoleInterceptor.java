package com.mms.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // URLs that require leader/manager role
        String[] leaderOnlyUrls = {
                "/members","/members/add", "/members/edit", "/members/delete",
                "/expenses","/expenses/add", "/expenses/edit", "/expenses/delete",
                "/collections","/collections/add", "/collections/edit", "/collections/delete",
                "/meals","/meals/add", "/meals/edit", "/meals/delete", "/meals/attendance"
        };

        // Check if current URL requires leader role
        for (String leaderUrl : leaderOnlyUrls) {
            if (uri.startsWith(leaderUrl)) {
                if (!SessionHelper.isLeader(request.getSession())) {
                    response.sendRedirect("/?error=unauthorized");
                    return false;
                }
                break;
            }
        }

        return true;
    }
}