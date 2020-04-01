package cn.worth.oauth2.domain;

import cn.worth.common.utils.CollectionUtils;
import cn.worth.core.domain.LoginUser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Getter
@Setter
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String email;
    private Long tenantId;
    private Integer status;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 角色码
     */
    private Set<String> roleCodes;
    /**
     * 按钮权限码
     */
    private Set<String> perms;
    /**
     * 菜单权限码
     */
    private Set<String> menus;

    public UserDetailsImpl(LoginUser authUser) {
        this.id = authUser.getId();
        this.username = authUser.getUsername();
        this.realName = authUser.getRealName();
        this.password = authUser.getPassword();
        this.email = authUser.getEmail();
        this.avatar = authUser.getAvatar();
        this.tenantId = authUser.getTenantId();
        this.status = authUser.getStatus();
        this.roleCodes = authUser.getRoleCodes();
        this.perms = authUser.getPerms();
        this.menus = authUser.getMenus();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new HashSet<>();
        if (CollectionUtils.isNotEmpty(roleCodes)) {
            roleCodes.forEach(role -> collection.add(new SimpleGrantedAuthority(role)));
        }
        return collection;
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
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}