package cn.worth.sys.service.impl;

import cn.worth.common.constant.CommonConstant;
import cn.worth.common.domain.R;
import cn.worth.common.enums.RCodeEnum;
import cn.worth.common.enums.UserStateEnum;
import cn.worth.common.exception.BusinessException;
import cn.worth.common.utils.StringUtils;
import cn.worth.core.domain.LoginUser;
import cn.worth.sys.domain.User;
import cn.worth.sys.enums.EntityTypeEnum;
import cn.worth.sys.mapper.UserMapper;
import cn.worth.sys.param.BindUserRoleParam;
import cn.worth.sys.pojo.UserPojo;
import cn.worth.sys.service.IUserRoleService;
import cn.worth.sys.service.IUserService;
import cn.worth.sys.utils.VerifyUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-03-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    @Transactional
    public R addOrUpdate(UserPojo userPojo, LoginUser userVO) {

        User user = addOrUpdateUser(userPojo, userVO);

        updateUserRole(userPojo, user);

        return new R(RCodeEnum.SUCCESS);
    }

    @Override
    public R del(Long userId) {
        verifyParams(userId);
        verifyAdmin(userId);
        User userParams = new User();
        userParams.setId(userId);
        userParams.setDelFlag(CommonConstant.STATUS_DEL);
        baseMapper.updateById(userParams);

        return new R(RCodeEnum.SUCCESS);
    }

    private void verifyAdmin(Long userId) {
        User user = getById(userId);
        if(null == user){
            throw new BusinessException("user is null");
        }
        Integer type = user.getType();
        VerifyUtils.verifyAdmin(type);
    }

    @Override
    public R lockUser(Long userId) {

        verifyParams(userId);

        updateUserStatus(userId, UserStateEnum.LOCKED.getCode());

        return new R(RCodeEnum.SUCCESS);
    }

    @Override
    public R unLockUser(Long userId) {
        verifyParams(userId);

        updateUserStatus(userId, UserStateEnum.ACTIVE.getCode());

        return new R(RCodeEnum.SUCCESS);
    }

    @Override
    public R batchDel(List<Long> ids) {
        List<User> users = new ArrayList<>();
        ids.forEach(id -> {
            User user = new User();
            user.setId(id);
            user.setDelFlag(CommonConstant.STATUS_DEL);
            users.add(user);
        });
        return R.success(updateBatchById(users));
    }

    @Override
    public R editSelfInfo(User user) {
        Long id = user.getId();
        String realName = user.getRealName();
        String mobile = user.getMobile();
        String avatar = user.getAvatar();
        if(null == id){
            return R.fail("id is null");
        }
        if(StringUtils.isBlank(realName) || StringUtils.isBlank(mobile) || StringUtils.isBlank(avatar)){
            return R.fail("信息不全");
        }
        updateById(user);
        return R.success("");
    }

    private void verifyParams(Long userId) {
        if(null == userId){
            throw new BusinessException("userId is null");
        }
    }

    private void updateUserStatus(Long userId, int status) {
        User userParams = new User();
        userParams.setId(userId);
        userParams.setStatus(status);
        baseMapper.updateById(userParams);
    }

    private void updateUserRole(UserPojo userPojo, User user) {
        Long userId = user.getId();
        BindUserRoleParam param = new BindUserRoleParam();
        param.setUserId(userId);
        param.setRoleIds(userPojo.getRoleIds());
        userRoleService.bindUserRole(param);
    }

    private User addOrUpdateUser(UserPojo userPojo, LoginUser userVO) {
        User user = new User();
        BeanUtils.copyProperties(userPojo, user);
        user.setStatus(UserStateEnum.ACTIVE.ordinal());
        user.setType(EntityTypeEnum.COMMON.ordinal());
        Long userId = user.getId();
        Date currentDate = new Date();
        if (null == userId) {
            user.setGmtUpdate(currentDate);
            user.setGmtUpdate(currentDate);
            user.setTenantId(userVO.getTenantId());
            baseMapper.insert(user);
        } else {
            user.setGmtUpdate(currentDate);
            baseMapper.updateById(user);
        }
        return user;
    }
}
