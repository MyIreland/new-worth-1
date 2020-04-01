package cn.worth.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: chenxiaoqing9
 * @Date: Created in 2018/10/30
 * @Description:
 * @Modified by:
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImportParam {
    String value();
}
