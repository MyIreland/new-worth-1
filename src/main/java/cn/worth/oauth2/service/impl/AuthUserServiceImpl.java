package cn.worth.oauth2.service.impl;

import cn.worth.common.utils.CollectionUtils;
import cn.worth.common.utils.StringUtils;
import cn.worth.core.domain.LoginUser;
import cn.worth.core.domain.MenuVO;
import cn.worth.core.domain.RoleVO;
import cn.worth.oauth2.domain.AuthUser;
import cn.worth.oauth2.mapper.AuthUserMapper;
import cn.worth.oauth2.service.AuthUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-08-01
 * @description
 **/
@Service
public class AuthUserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements AuthUserService {

    @Override
    public LoginUser findUserByUsername(String username) {
        AuthUser query = new AuthUser();
        query.setUsername(username);
        LoginUser loginUser = baseMapper.loadUserByUsername(query);
        List<RoleVO> roles = loginUser.getRoles();
        Set<String> roleCodes = roles.stream().map(RoleVO::getRoleCode).collect(Collectors.toSet());
        Set<String> allPerms = new HashSet<>();
        roles.forEach(res -> {
            Set<MenuVO> permissions = res.getPermissions();
            if(CollectionUtils.isNotEmpty(permissions)){
                Set<String> perms = permissions.stream().map(MenuVO::getPermission).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
                allPerms.addAll(perms);
            }
        });
        Set<String> perms = new HashSet<>();
        Set<String> menus = new HashSet<>();
        if(CollectionUtils.isNotEmpty(allPerms)){
            perms = allPerms.stream().filter(res -> res.contains(":")).collect(Collectors.toSet());
            menus = allPerms.stream().filter(res -> !res.contains(":")).collect(Collectors.toSet());
        }
        loginUser.setPerms(perms);
        loginUser.setMenus(menus);
        loginUser.setRoleCodes(roleCodes);
        return loginUser;
    }
}
