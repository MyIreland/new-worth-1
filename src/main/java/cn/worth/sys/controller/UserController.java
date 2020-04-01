package cn.worth.sys.controller;

import cn.worth.common.annotation.CurrentUser;
import cn.worth.common.constant.CommonConstant;
import cn.worth.common.domain.R;
import cn.worth.common.utils.StringUtils;
import cn.worth.core.controller.BaseController;
import cn.worth.core.domain.LoginUser;
import cn.worth.sys.domain.User;
import cn.worth.sys.pojo.UserPojo;
import cn.worth.sys.service.IRoleService;
import cn.worth.sys.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: chenxiaoqing9
 * @Date: Created in 2019/3/19
 * @Description:
 * @Modified by:
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<IUserService, User> {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @PostMapping("/page")
    public R page(Page<User> userPage, User user, @CurrentUser LoginUser loginUser) {
        QueryWrapper<User> QueryWrapper = getUserQueryWrapper(user, loginUser);
        Page<User> page = userService.page(userPage, QueryWrapper);
        return R.success(page);
    }

    @PostMapping("/editSelfInfo")
    public R editSelfInfo(@RequestBody User user, @CurrentUser LoginUser loginUser) {
        return userService.editSelfInfo(user);
    }

    private QueryWrapper<User> getUserQueryWrapper(User user, @CurrentUser LoginUser loginUser) {

        QueryWrapper<User> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.orderByAsc("username");
        QueryWrapper.eq("tenant_id", loginUser.getTenantId());
        QueryWrapper.eq("del_flag", CommonConstant.STATUS_NORMAL);
        String realName = user.getRealName();
        String username = user.getUsername();
        String mobile = user.getMobile();
        String email = user.getEmail();
        if (StringUtils.isNotBlank(realName)){
            QueryWrapper.eq("real_name", realName);
        }
        if (StringUtils.isNotBlank(mobile)){
            QueryWrapper.eq("mobile", mobile);
        }
        if (StringUtils.isNotBlank(username)){
            QueryWrapper.eq("username", username);
        }
        if (StringUtils.isNotBlank(email)){
            QueryWrapper.eq("email", email);
        }
        return QueryWrapper;
    }

    @GetMapping("/{userId}")
    public R get(@PathVariable Long userId) {
        User data = userService.getById(userId);
        if(null != data){
            UserPojo pojo = new UserPojo();
            BeanUtils.copyProperties(data, pojo);
            List<Long> roleIds = roleService.getRoleIdsByUserId(userId);
            pojo.setRoleIds(roleIds);
            return R.success(pojo);
        }

        return R.fail("用户不存在");
    }

    @PostMapping
    public R add(@RequestBody UserPojo userPojo, @CurrentUser LoginUser userVO) {
        return userService.addOrUpdate(userPojo, userVO);
    }

    @PutMapping
    public R update(@RequestBody UserPojo userPojo, @CurrentUser LoginUser userVO) {
        return userService.addOrUpdate(userPojo, userVO);
    }

    @DeleteMapping("/{userId}")
    public R del(@PathVariable Long userId) {
        return userService.del(userId);
    }

    @PostMapping("/batchDel")
    public R batchDel(@RequestBody List<Long> ids) {
        return userService.batchDel(ids);
    }

    @PostMapping("/lockUser")
    public R lockUser(@PathVariable Long userId) {
        return userService.lockUser(userId);
    }

    @PostMapping("/unLockUser")
    public R unLockUser(@PathVariable Long userId) {
        return userService.unLockUser(userId);
    }
}
