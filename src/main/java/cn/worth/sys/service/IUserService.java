package cn.worth.sys.service;

import cn.worth.common.domain.R;
import cn.worth.core.domain.LoginUser;
import cn.worth.sys.domain.User;
import cn.worth.sys.pojo.UserPojo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-09
 */
public interface IUserService extends IService<User> {

    R addOrUpdate(UserPojo userPojo, LoginUser userVO);

    R del(Long userId);

    R lockUser(Long userId);

    R unLockUser(Long userId);

    R batchDel(List<Long> ids);

    /**
     * 更新自己基本信息
     * @param user
     * @return
     */
    R editSelfInfo(User user);
}
