package cn.worth.sys.mapper;

import cn.worth.sys.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-22
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Long> getRoleIdsByUserId(@Param("userId") Long userId);
}
