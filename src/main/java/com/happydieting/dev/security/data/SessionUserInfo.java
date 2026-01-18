package com.happydieting.dev.security.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
public class SessionUserInfo implements Serializable {
    private String username;
    private String fullName;
    private String email;
    Collection<? extends GrantedAuthority> authorities;
}
