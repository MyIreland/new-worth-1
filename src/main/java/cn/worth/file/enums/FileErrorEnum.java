package cn.worth.file.enums;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-09-11
 * @description
 **/
public enum FileErrorEnum {
    UNKNOW_FILE(500, "上传文件不存在"),
    UPLOAD_FILE_ERROR(500, "文件上传异常"),
    SAVE_FILE_LOG_ERROR(500, "添加上传文件记录失败"),
    UPLOAD_FILE_FAIL(500, "文件上传失败");

    private int code;
    private String desc;

    FileErrorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
