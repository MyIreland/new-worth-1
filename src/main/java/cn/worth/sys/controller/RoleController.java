package cn.worth.sys.controller;

import cn.worth.common.annotation.CurrentUser;
import cn.worth.common.constant.CommonConstant;
import cn.worth.common.domain.R;
import cn.worth.common.exception.BusinessException;
import cn.worth.common.utils.StringUtils;
import cn.worth.core.domain.LoginUser;
import cn.worth.core.controller.BaseController;
import cn.worth.sys.domain.Role;
import cn.worth.sys.service.IRoleService;
import cn.worth.sys.utils.VerifyUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chenxiaoqing
 * @since 2019-08-07
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<IRoleService, Role> {

    @Autowired
    private IRoleService roleService;


    @PostMapping("page")
    public R page(Page<Role> page, Role role, @CurrentUser LoginUser user){
        QueryWrapper<Role> wrapper = getRoleWrapper(role, user);
        Page<Role> rolePage = roleService.page(page, wrapper);
        return R.success(rolePage);
    }

    @PostMapping("list")
    public R list(Role role, @CurrentUser LoginUser user){
        QueryWrapper<Role> roleWrapper = getRoleWrapper(role, user);

        List<Role> roles = roleService.list(roleWrapper);
        return R.success(roles);
    }

    private QueryWrapper<Role> getRoleWrapper(Role role, LoginUser user) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        wrapper.eq("del_flag", CommonConstant.STATUS_NORMAL);
        wrapper.eq("tenant_id", user.getTenantId());
        String roleCode = role.getRoleCode();
        String roleName = role.getRoleName();
        Integer roleType = role.getRoleType();
        if(StringUtils.isNotBlank(roleCode)){
            wrapper.eq("role_code", roleCode);
        }
        if(StringUtils.isNotBlank(roleName)){
            wrapper.eq("role_name", roleName);
        }
        if(null != roleType){
            wrapper.eq("role_type", roleType);
        }
        return wrapper;
    }

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return Role
     */
    @GetMapping("{id}")
    public R<Role> get(@PathVariable Long id) {
        return R.success(roleService.getById(id));
    }

    /**
     * 添加
     *
     * @param role 实体
     * @return success/false
     */
    @PostMapping()
    public R<Boolean> add(@RequestBody Role role, @CurrentUser LoginUser loginUser) {
        Date gmtCreate = new Date();
        role.setGmtCreate(gmtCreate);
        role.setTenantId(loginUser.getTenantId());
        role.setGmtUpdate(gmtCreate);
        role.setDelFlag(CommonConstant.STATUS_NORMAL);
        return R.success(roleService.save(role));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {

        verifyParam(id);

        Role roleQuery = new Role();
        roleQuery.setId(id);
        roleQuery.setGmtUpdate(new Date());
        roleQuery.setDelFlag(CommonConstant.STATUS_DEL);
        return R.success(roleService.updateById(roleQuery));
    }

    private void verifyParam(@PathVariable Long id) {
        Role role = roleService.getById(id);
        if(null == role){
            throw new BusinessException("error id");
        }
        VerifyUtils.verifyAdmin(role.getRoleType());
    }

    /**
     * 编辑
     *
     * @param role 实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> update(@RequestBody Role role) {
        role.setGmtUpdate(new Date());
        return R.success(roleService.updateById(role));
    }
}
