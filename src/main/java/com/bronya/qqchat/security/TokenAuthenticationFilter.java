package com.bronya.qqchat.security;

import com.bronya.qqchat.domain.bo.LoginUser;
import com.bronya.qqchat.service.TokenService;
import com.bronya.qqchat.service.impl.UserDetailsServiceImpl;
import com.bronya.qqchat.util.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈Token过滤〉
 *
 * @author bronya
 * @create 2024/4/9
 * @since 1.0.0
 */
@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private TokenService tokenService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取登录用户
        LoginUser loginUser = tokenService.getLoginUser(request);
        // 用户存在
        if (loginUser != null) {
            log.info("登录用户: {}", loginUser);
            // 设置登录用户和权限
            List<GrantedAuthority> authorities = userDetailsService.getRoleList(loginUser.getUserRoleId());
            // 传递给Spring Security
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
            SecurityUtils.setAuthentication(authenticationToken);
            // Token续期
            tokenService.refreshToken(loginUser);
        }
        filterChain.doFilter(request, response);
    }
}