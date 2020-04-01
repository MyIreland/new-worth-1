package cn.worth.sys.service;

import cn.worth.core.domain.MenuVO;
import cn.worth.sys.domain.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-22
 */
public interface IMenuService extends IService<Menu> {

    List<MenuVO> findPermsByRoleIds(List<Long> roleIds);

    List<MenuVO> findMenusByRoleIds(List<Long> roleIds);
}
