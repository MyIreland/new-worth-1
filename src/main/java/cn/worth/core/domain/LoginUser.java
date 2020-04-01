package cn.worth.core.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class LoginUser implements Serializable {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 性别 1-男 2-女
     */
    private Integer sex;
    /**
     * 0管理员1普通用户2微信用户
     */
    private Integer type;
    /**
     * 随机盐
     */
    private String salt;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 状态 0-正常 1-锁住 2- 过期
     */
    private Integer status;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 修改时间
     */
    private Date gmtUpdate;
    /**
     * 0-正常，1-删除
     */
    private Integer delFlag;
    /**
     * 角色列表
     */
    private List<RoleVO> roles = new ArrayList<>();
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
}
