package com.happydieting.dev.config;

import com.happydieting.dev.security.filter.JwtTokenFilter;
import com.happydieting.dev.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String API_PREFIX = "/api/";
    private static final String API_PROCESSING_URL = API_PREFIX + "**";
    private static final String[] WEB_WHITELIST = {"/css/**", "/images/**", "/js/**", "/signup", "/signup/**", "/login"};
    private static final String[] API_WHITELIST = {"/api/auth/**", "/api/account/register"};
    public static final String REGISTER = "/register";

    public static final String SIGNUP = "/signup";
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtTokenFilter jwtTokenFilter) {
        this.customUserDetailsService = userDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher(request -> !request.getRequestURI().startsWith(API_PREFIX))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WEB_WHITELIST).permitAll()
                        .requestMatchers(LOGIN_PROCESSING_URL, REGISTER).permitAll()
                        .requestMatchers(API_PROCESSING_URL).denyAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(LOGIN_PROCESSING_URL)
                        .defaultSuccessUrl("/", true)
                        .failureUrl(LOGIN_PROCESSING_URL + "?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl(LOGIN_PROCESSING_URL + "?logout=true")
                        .permitAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher(API_PROCESSING_URL)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(API_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
