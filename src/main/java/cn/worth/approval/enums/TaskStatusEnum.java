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
public enum TaskStatusEnum {
    RUNNING(0, "进行中"),PASS(1, "通过"),REFUSE(2, "拒绝"),CANCEL(3, "取消"),RECALL(4, "撤回");

    private int code;
    private String desc;

}
