package com.happydieting.dev.security.service;

import com.happydieting.dev.constant.SessionConstant;
import com.happydieting.dev.model.UserModel;
import com.happydieting.dev.repository.UserRepository;
import com.happydieting.dev.security.data.SessionUserInfo;
import com.happydieting.dev.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.Optional;

@Service
public class SessionService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public SessionService(UserRepository userRepository, ModelMapper modelMapper, UserService userService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return (attrs != null) ? attrs.getRequest() : null;
    }


    private HttpSession getSession(boolean create) {
        HttpServletRequest request = getRequest();
        return (request != null) ? request.getSession(create) : null;
    }

    public void setAttribute(String key, Object value) {
        HttpSession session = getSession(true);
        if (session != null) {
            session.setAttribute(key, value);
        }
    }

    public Object getAttribute(String key) {
        HttpSession session = getSession(false);
        return (session != null) ? session.getAttribute(key) : null;
    }

    public void removeAttribute(String key) {
        HttpSession session = getSession(false);
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    public void invalidateSession() {
        HttpSession session = getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public boolean isAuthenticated() {
        return getSession(true) != null;
    }

    public void setSessionUser(String username, Collection<? extends GrantedAuthority> authorities) {
        //TODO: after role management, set authorities

        HttpSession session = getSession(true);
        if (session != null) {
            Optional<UserModel> userModelOpt = userRepository.findByUsername(username);
            if(userModelOpt.isPresent() && userModelOpt.get().isEnabled()) {
                session.setAttribute(SessionConstant.USERNAME, username);

                UserModel userModel = userModelOpt.get();
                session.setAttribute(SessionConstant.USER_INFO, modelMapper.map(userModel, SessionUserInfo.class));
            }
        }
    }

    public UserModel getSessionUser() {
        HttpSession session = getSession(true);
        if (session != null) {
            String username = (String) session.getAttribute(SessionConstant.USERNAME);
            return userRepository.findByUsername(username).orElse(null);
        }
        return null;
    }
}
