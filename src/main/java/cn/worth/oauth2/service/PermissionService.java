package cn.worth.oauth2.service;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-11-05
 * @description
 **/
public interface PermissionService {

    boolean hasPermission(String permission);

    /**
     * 判断当前用户是不是具有某个角色
     * @param roleName
     * @return
     */
    boolean hasRole(String roleName);
}
