package cn.worth.sys.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-08-06
 * @description
 **/
@Getter
@Setter
public class UserPojo {
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
     * 0-普通 1-管理员 2-微信用户
     */
    private Integer type;
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
     * 机构ID
     */
    private Long tenantId;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 角色集合
     */
    private List<Long> roleIds;
}
