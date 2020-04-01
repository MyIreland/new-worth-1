package cn.worth.common.domain;

import cn.worth.common.enums.BaseEnum;
import cn.worth.common.enums.RCodeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 统一响应响应信息主体
 * @param <T>
 */
@Getter
@Setter
@NoArgsConstructor
public class R<T> implements Serializable {

    /**
     * 附带信息
     */
    private String message = "";

    /**
     * 状态码
     */
    private int code = RCodeEnum.SUCCESS.getCode();

    /**
     * 时间戳
     */
    private long timestamp = new Date().getTime();

    /**
     * 请求地址
     */
    private String path;

    private Boolean cbkOriginData;

    public Boolean getCbkOriginData() {
        return cbkOriginData;
    }

    public void setCbkOriginData(Boolean cbkOriginData) {
        this.cbkOriginData = cbkOriginData;
    }

    private T data;

    public R(T data) {
        super();
        this.data = data;
    }

    public R(BaseEnum<Integer> baseEnum) {
        this.code = baseEnum.getCode();
        this.message = baseEnum.getMsg();
    }

    public R(T data, String message) {
        super();
        this.data = data;
        this.message = message;
    }

    public R(Throwable e) {
        super();
        this.message = e.getMessage();
        this.code = RCodeEnum.BIZ_EXCEPTION.getCode();
    }

    public static R success(String message) {
        R r = new R();
        r.setMessage(message);

        return r;
    }

    public static<T> R success(String message, T data) {
        R<T> r = new R<>();
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static<T> R success(T data) {
        R<T> r = new R<>();
        r.setData(data);
        return r;
    }

    public static R fail(String message) {
        R r = new R();
        r.setCode(RCodeEnum.BIZ_EXCEPTION.getCode());
        r.setMessage(message);

        return r;
    }

    public static R fail(RCodeEnum codeEnum) {
        R r = new R();
        r.setCode(codeEnum.getCode());
        r.setMessage(codeEnum.getMsg());
        return r;
    }

    public static R fail(int code, String message) {
        R r = new R();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static R fail(Throwable e) {
        R r = new R();
        r.setMessage(e.getMessage());
        r.setCode(RCodeEnum.BIZ_EXCEPTION.getCode());
        return r;
    }

}
