package com.happydieting.dev.security.service;

import com.happydieting.dev.data.UserData;
import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.repository.UserRepository;
import com.happydieting.dev.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
    private static final String USERNAME = "username";
    private final UserRepository userRepository;
    private final UserService userService;

    public SessionService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Authentication getSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        SecurityContext ctx = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT);
        return ctx.getAuthentication();
    }

    public boolean isAuthenticated(HttpServletRequest request) {
        return getSession(request) != null;
    }

    public UserModel getSessionUser(HttpServletRequest request) {
        if (!isAuthenticated(request)) return null;

        HttpSession session = request.getSession();
        return userRepository.findByUsername((String) session.getAttribute(USERNAME)).orElse(null);
    }

    public UserData getSessionUserData(HttpServletRequest request) {
        return userService.convertModel2Data(getSessionUser(request));
    }
}
