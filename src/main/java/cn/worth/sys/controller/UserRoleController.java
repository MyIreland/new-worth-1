package cn.worth.sys.controller;

import cn.worth.common.domain.R;
import cn.worth.sys.param.BindUserRoleParam;
import cn.worth.sys.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-08-07
 * @description
 **/
@RestController
@RequestMapping("userRole")
public class UserRoleController {

    @Autowired
    private IUserRoleService userRoleService;

    @PostMapping("bindUserRole")
    public R bindUserRole(@RequestBody BindUserRoleParam param){
        return userRoleService.bindUserRole(param);
    }

    @GetMapping("{userId}")
    public R getRoleIdsByUserId(@PathVariable Long userId){
        return userRoleService.getRoleIdsByUserId(userId);
    }


}
