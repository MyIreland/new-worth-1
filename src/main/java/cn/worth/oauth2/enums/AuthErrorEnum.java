
package cn.worth.oauth2.enums;
import org.springframework.http.HttpStatus;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-08-02
 * @description 授权错误码
 **/
public enum AuthErrorEnum {
    INVALID_CLIENT(HttpStatus.UNAUTHORIZED.value(), "验证权限出错，请重新登录"),
    INVALID_USER(HttpStatus.UNAUTHORIZED.value(), "无效用户"),
    CLIRNENT_NO_AUTH(HttpStatus.UNAUTHORIZED.value(), "授权客户端失败"),
    USER_NO_AUTH(HttpStatus.FORBIDDEN.value(), "当前用户无权限访问"),
    ERROR_ACCOUNT_OR_PASSWORD(HttpStatus.UNAUTHORIZED.value(), "账户或者密码错误");

    private int code;
    private String desc;

    AuthErrorEnum(int code, String desc) {
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
