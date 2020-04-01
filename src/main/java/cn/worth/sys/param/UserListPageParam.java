package cn.worth.sys.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-08-09
 * @description
 **/
@Getter
@Setter
public class UserListPageParam {
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 性别 1-男 2-女
     */
    private Integer sex;
    /**
     * 0管理员1普通用户2微信用户
     */
    private Integer type;
    /**
     * 状态 0-正常 1-锁住 2- 过期
     */
    private Integer status;
    /**
     * 邮箱
     */
    private String email;
}
