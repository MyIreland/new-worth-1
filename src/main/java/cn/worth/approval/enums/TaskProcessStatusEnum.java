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
public enum TaskProcessStatusEnum {
    WAIT(0, "待审核"),AGREE(1, "通过"),REFUSE(2, "驳回");

    private final int code;
    private final String desc;
}
