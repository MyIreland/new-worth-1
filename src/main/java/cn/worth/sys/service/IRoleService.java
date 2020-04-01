package cn.worth.sys.service;

import cn.worth.sys.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-22
 */
public interface IRoleService extends IService<Role> {

    List<Long> getRoleIdsByUserId(Long userId);
}
