package cn.worth.sys.mapper;

import cn.worth.core.domain.MenuVO;
import cn.worth.sys.domain.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-22
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<MenuVO> findPermsByRoleIds(@Param("roleIds") List<Long> roleIds);

    List<MenuVO> findMenusByRoleIds(@Param("roleIds") List<Long> roleIds);

}
