package cn.worth.quartz.enums;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-09-10
 * @description
 **/
public enum JobStatusEnum {
    NOT_RUNNING("0", "未运行"),
    RUNNING("1", "运行中");

    private String code;

    private String desc;

    JobStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
