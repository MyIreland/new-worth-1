package cn.worth.sys.mapper;

import cn.worth.sys.domain.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 用户与角色对应关系 Mapper 接口
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-22
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    Set<Long> getRoleIdsByUserId(@Param("userId") Long userId);
}
