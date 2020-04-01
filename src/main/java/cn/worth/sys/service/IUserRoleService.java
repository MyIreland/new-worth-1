package cn.worth.sys.service;

import cn.worth.common.domain.R;
import cn.worth.sys.domain.UserRole;
import cn.worth.sys.param.BindUserRoleParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户与角色对应关系 服务类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-22
 */
public interface IUserRoleService extends IService<UserRole> {

    R bindUserRole(BindUserRoleParam param);

    R getRoleIdsByUserId(Long userId);
}
