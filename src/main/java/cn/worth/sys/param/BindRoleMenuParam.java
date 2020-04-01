package cn.worth.sys.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-08-07
 * @description
 **/
@Getter
@Setter
public class BindRoleMenuParam {
    @NotNull(message = "roleId is null")
    private Long roleId;

    private List<Long> menuIds;
}
