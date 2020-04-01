package cn.worth.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: MyIreland on.
 * @Description:
 * @Date: 2019/3/22
 */
@Getter
@AllArgsConstructor
public enum RCodeEnum implements BaseEnum<Integer>{
    SUCCESS(200, "操作成功"),

    BIZ_EXCEPTION(501, "业务异常"),

    UNLOGIN(201, "未登录"),

    FAIL(100, "失败");

    private Integer code;

    private String msg;
}
