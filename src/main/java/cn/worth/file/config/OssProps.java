package cn.worth.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-10-29
 * @description
 **/
@Data
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProps {


    /**
     * accessKeyId
     */
    private String accessKeyId;
    /**
     *
     */
    private String accessKeySecret;

    private String bucketName;

    private String callback;
    private String endpoint;
    /**
     * 内网
     */
    private String endpointInternal;

    private long maxSize;
    /**
     * 代理过期时间 默认分钟
     */
    private long policyExpire;
}
