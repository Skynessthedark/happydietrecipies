package com.happydieting.dev.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Component
public class SessionEventHandler {

    private static final String USERNAME = "username";

    @EventListener
    public void audit(InteractiveAuthenticationSuccessEvent e) {
        getSession().ifPresent(s -> s.setAttribute(USERNAME, e.getAuthentication().getName()));
    }

    @EventListener
    public void audit(SessionDestroyedEvent e) {
        getSession().ifPresent(s -> s.removeAttribute(USERNAME));
    }

    private static Optional<HttpSession> getSession() {
        return getCurrentRequest().map(HttpServletRequest::getSession);
    }

    private static Optional<HttpServletRequest> getCurrentRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }
}
