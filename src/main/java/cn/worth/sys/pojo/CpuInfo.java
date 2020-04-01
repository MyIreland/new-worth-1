package cn.worth.sys.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-10-08
 * @description
 **/
@Getter
@Setter
public class CpuInfo {
    /**
     * 核心数
     */
    private int cpuNum;

    /**
     * CPU总的使用率
     */
    private double total;

    /**
     * CPU系统使用率
     */
    private double sys;

    /**
     * CPU用户使用率
     */
    private double used;

    /**
     * CPU当前等待率
     */
    private double wait;

    /**
     * CPU当前空闲率
     */
    private double free;
}
