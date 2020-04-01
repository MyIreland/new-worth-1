package cn.worth.sys.service;

import cn.worth.common.domain.R;
import cn.worth.sys.domain.RoleMenu;
import cn.worth.sys.param.BindRoleMenuParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色与菜单对应关系 服务类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-22
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    R findMenuIdsByRoleId(Long roleId, Long orgId);

    R bindRoleMenu(BindRoleMenuParam param, Long orgId);
}
