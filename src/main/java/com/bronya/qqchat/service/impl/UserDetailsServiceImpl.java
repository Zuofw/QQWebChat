package com.bronya.qqchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bronya.qqchat.constant.RoleConstants;
import com.bronya.qqchat.domain.bo.LoginUser;
import com.bronya.qqchat.domain.entity.User;
import com.bronya.qqchat.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈UserDetails〉
 *
 * @author bronya
 * @create 2024/4/9
 * @since 1.0.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        Integer roleId = RoleConstants.USER;
        return new LoginUser(
                user.getUserName(),
                user.getPassword(),
                roleId,
                null,
                getRoleList(roleId)
        );
    }
    public List<GrantedAuthority> getRoleList(int roleId) {
        Map<Integer, String> roleMap = RoleConstants.roleMap;
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleMap.get(roleId)));
        return authorities;
    }
}