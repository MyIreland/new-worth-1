package cn.worth.approval.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-09-18
 * @description
 **/
@Getter
@AllArgsConstructor
public enum ApprovalErrorEnum {
    TASK_NOT_FOUND(500, "任务未找到"),
    TASK_OPTION_USER_ERROR(500, "无权操作该审批"),
    TASK_STATUS_ERROR(500, "任务状态错误")
    ;

    private final int code;
    private final String desc;
}
