package com.happydieting.dev.security.handler;

import com.happydieting.dev.security.service.SessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final SessionService sessionService;
    private final RedirectStrategy happyDietRedirectStrategy;

    @Value("${redirect.forwardUrl}")
    private String forwardUrl;

    public CustomAuthenticationSuccessHandler(SessionService sessionService) {
        this.sessionService = sessionService;
        this.happyDietRedirectStrategy = new DefaultRedirectStrategy();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            sessionService.setSessionUser(authentication.getName(), authentication.getAuthorities());
        }
        happyDietRedirectStrategy.sendRedirect(request, response, forwardUrl);
    }
}
