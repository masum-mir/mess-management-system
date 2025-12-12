package com.mms.config;

import com.mms.model.Member;
import jakarta.servlet.http.HttpSession;

public class SessionHelper {

    private static final String USER_SESSION_KEY = "loggedInUser";

    public static void setLoggedInMember(HttpSession session, Member member) {
        session.setAttribute(USER_SESSION_KEY, member);
    }

    public static Member getLoggedInMember(HttpSession session) {
        return (Member) session.getAttribute(USER_SESSION_KEY);
    }

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(USER_SESSION_KEY) != null;
    }

    public static boolean isLeader(HttpSession session) {
        Member member = getLoggedInMember(session);
        return member != null && member.getIsLeader();
    }

    public static void logout(HttpSession session) {
        session.invalidate();
    }
}