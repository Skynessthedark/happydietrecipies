    package com.happydieting.dev.model;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.Getter;
    import lombok.Setter;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.Collection;
    import java.util.List;

    @Entity
    @Table(name = "USER")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
    @Getter
    @Setter
    public class UserModel extends ItemModel implements UserDetails {

        private String username;

        @NotBlank(message = "The full name field cannot be left blank.")
        private String fullName;

        @Email
        @NotBlank(message = "The e-mail field cannot be left blank.")
        private String email;

        @NotBlank
        private String password;

        @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private MediaModel image;

        private String bio;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of();
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isEnabled() {
            return UserDetails.super.isEnabled();
        }

        @Override public boolean isAccountNonExpired() { return true; }
        @Override public boolean isAccountNonLocked() { return true; }
        @Override public boolean isCredentialsNonExpired() { return true; }
    }
