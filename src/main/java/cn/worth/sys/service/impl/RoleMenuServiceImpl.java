package cn.worth.sys.service.impl;

import cn.worth.common.domain.R;
import cn.worth.common.enums.RCodeEnum;
import cn.worth.common.utils.CollectionUtils;
import cn.worth.sys.domain.RoleMenu;
import cn.worth.sys.mapper.RoleMenuMapper;
import cn.worth.sys.param.BindRoleMenuParam;
import cn.worth.sys.service.IRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色与菜单对应关系 服务实现类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-22
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Override
    public R<Set<Long>> findMenuIdsByRoleId(Long roleId, Long orgId) {
        Set<Long> menuIdsByRoleId = baseMapper.findMenuIdsByRoleId(roleId, orgId);
        return R.success(menuIdsByRoleId);
    }

    @Override
    @Transactional
    public R bindRoleMenu(BindRoleMenuParam param, Long orgId) {
        Long roleId = param.getRoleId();

        List<RoleMenu> roleMenus = getRoleMenus(param, roleId, orgId);

        deleteByRoleId(roleId);

        if(CollectionUtils.isNotEmpty(roleMenus)){
            saveBatch(roleMenus);
        }
        return R.success("操作成功");
    }

    private List<RoleMenu> getRoleMenus(BindRoleMenuParam param, Long roleId, Long orgId) {
        List<Long> menuIds = param.getMenuIds();
        List<RoleMenu> roleMenus = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenu.setOrgId(orgId);
            roleMenus.add(roleMenu);
        }
        return roleMenus;
    }

    private void deleteByRoleId(Long roleId) {
        QueryWrapper<RoleMenu> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("role_id", roleId);
        baseMapper.delete(QueryWrapper);
    }
}
