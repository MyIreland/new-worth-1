package cn.worth.sys.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-08-06
 * @description
 **/
@Getter
@AllArgsConstructor
public enum EntityTypeEnum {

    COMMON(0, "普通类型"),
    ADMIN(1, "管理员类型"),
    WX(2, "微信类型")
    ;

    private int code;
    private String desc;
}
