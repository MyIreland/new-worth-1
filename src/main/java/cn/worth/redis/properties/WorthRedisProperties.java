package cn.worth.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "worth.redis")
public class WorthRedisProperties {

    private String keyPrefix = "default:";
}