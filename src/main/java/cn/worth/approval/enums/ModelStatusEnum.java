package cn.worth.approval.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-09-17
 * @description
 **/
@Getter
@AllArgsConstructor
public enum ModelStatusEnum {

    UNUSED(0, "未使用"), INUSE(1, "使用中");

    private int code;
    private String desc;

}
