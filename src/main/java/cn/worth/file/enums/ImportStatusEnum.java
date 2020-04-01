package cn.worth.file.enums;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-10-10
 * @description
 **/
public enum ImportStatusEnum {
    WAITING(0, "未开始"),
    RUNNING(1, "进行中"),
    SUCCESS(2, "成功"),
    FAIL(3, "失败");


    private final int code;
    private final String desc;

    ImportStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
