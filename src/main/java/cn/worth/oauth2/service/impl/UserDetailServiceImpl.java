package cn.worth.oauth2.service.impl;

import cn.worth.core.domain.LoginUser;
import cn.worth.oauth2.domain.UserDetailsImpl;
import cn.worth.oauth2.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private AuthUserService userService;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUser userVO = userService.findUserByUsername(username);
        return new UserDetailsImpl(userVO);
    }
}