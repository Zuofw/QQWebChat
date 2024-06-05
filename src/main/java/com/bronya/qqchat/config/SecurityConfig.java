package com.bronya.qqchat.config;

import com.bronya.qqchat.security.AccessDeniedHandlerImpl;
import com.bronya.qqchat.security.CORSFilter;
import com.bronya.qqchat.security.TokenAuthenticationFilter;
import com.bronya.qqchat.security.UnAuthorizedRequestHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 〈SecuriyConfig〉<br>
 * 〈〉
 *
 * @author bronya
 * @create 2024/4/9
 * @since 1.0.0
 */
@Configuration
//@EnableWebSecurity
public class SecurityConfig {
    @Resource
    private TokenAuthenticationFilter tokenAuthenticationFilter;
    @Resource
    private UnAuthorizedRequestHandler unAuthorizedRequestHandler;
    @Resource
    private CORSFilter corsFilter;
    @Resource
    private AccessDeniedHandlerImpl accessDeniedHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        httpSecurity.authorizeHttpRequests(
                authorizeRequests -> authorizeRequests
                        .requestMatchers("/user/login").permitAll() // 允许登录和注册
                        .requestMatchers("/user/register").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
//                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/chat").permitAll()
                        .requestMatchers("user/getUserByName").permitAll()
                        .anyRequest().authenticated()
        );
        httpSecurity.exceptionHandling(
                exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(unAuthorizedRequestHandler)
                        .accessDeniedHandler(accessDeniedHandler)
        );
        httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);
        // 添加自定义的Token验证过滤器
        httpSecurity.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }
}