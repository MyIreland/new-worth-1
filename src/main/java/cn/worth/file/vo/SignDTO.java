package cn.worth.file.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignDTO implements Serializable {
    private static final long serialVersionUID=1L;

    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     *
     */
    private String policy;

    /**
     * 签名
     */
    private String signature;

    /**
     *
     */
    private String dir;

    /**
     * 回调地址
     */
    private String callback;

    /**
     * 返回过期时间
     */
    private Date expire;
    /**
     *
     */
    private String action;

}