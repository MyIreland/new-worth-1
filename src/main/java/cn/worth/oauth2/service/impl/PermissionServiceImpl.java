package cn.worth.oauth2.service.impl;

import cn.worth.oauth2.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-11-06
 * @description
 **/

@Slf4j
@Component("pms")
public class PermissionServiceImpl implements PermissionService {
    public boolean hasPermission(String permission) {
        return true;
//		if (StrUtil.isBlank(permission)) {
//			return true;
//		}
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication == null) {
//			return false;
//		}
//		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//		return authorities.stream()
//			.map(GrantedAuthority::getAuthority)
//			.filter(StringUtils::hasText)
//			.anyMatch(x -> PatternMatchUtils.simpleMatch(permission, x));
    }


    /**
     * 判断当前用户是不是具有某个角色
     * @param roleName
     * @return
     */
    public boolean hasRole(String roleName) {
        return true;
    }
}
