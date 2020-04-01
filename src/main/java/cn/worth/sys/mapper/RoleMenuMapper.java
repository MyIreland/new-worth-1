package cn.worth.sys.mapper;

import cn.worth.sys.domain.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 角色与菜单对应关系 Mapper 接口
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-22
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    Set<Long> findMenuIdsByRoleId(@Param("roleId") Long roleId, @Param("orgId") Long orgId);
}
