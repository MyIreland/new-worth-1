package cn.worth.approval.vo;

import cn.worth.approval.domain.ApprovalTask;
import cn.worth.approval.domain.ApprovalTaskProcess;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-09-17
 * @description
 **/
@Getter
@Setter
public class ApprovalTaskVO extends ApprovalTask {
    private List<ApprovalTaskProcess> processes;
}
