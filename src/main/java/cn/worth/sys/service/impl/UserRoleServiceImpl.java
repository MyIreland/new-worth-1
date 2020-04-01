package cn.worth.sys.service.impl;

import cn.worth.common.domain.R;
import cn.worth.common.enums.RCodeEnum;
import cn.worth.common.utils.CollectionUtils;
import cn.worth.sys.domain.UserRole;
import cn.worth.sys.mapper.UserRoleMapper;
import cn.worth.sys.param.BindUserRoleParam;
import cn.worth.sys.service.IUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户与角色对应关系 服务实现类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-22
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Override
    @Transactional
    public R bindUserRole(BindUserRoleParam param) {
        Long userId = param.getUserId();

        List<UserRole> userRoles = getUserRoles(param, userId);

        deleteByUserId(userId);

        if(CollectionUtils.isNotEmpty(userRoles)){
            saveBatch(userRoles);
        }

        return R.success("操作成功");
    }

    private void deleteByUserId(Long userId) {
        QueryWrapper<UserRole> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("user_id", userId);
        baseMapper.delete(QueryWrapper);
    }

    private List<UserRole> getUserRoles(BindUserRoleParam param, Long userId) {
        List<Long> roleIds = param.getRoleIds();
        List<UserRole> userRoles = new ArrayList<>();

        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        }
        return userRoles;
    }

    @Override
    public R getRoleIdsByUserId(Long userId) {

        Set<Long> roleIds = baseMapper.getRoleIdsByUserId(userId);
        return R.success(roleIds);
    }
}
