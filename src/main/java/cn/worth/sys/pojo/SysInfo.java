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
public class SysInfo {
    /**
     * 服务器名称
     */
    private String computerName;

    /**
     * 服务器Ip
     */
    private String computerIp;

    /**
     * 项目路径
     */
    private String userDir;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;
}
