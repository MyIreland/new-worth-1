package cn.worth.common.utils;

import cn.worth.common.exception.BusinessException;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-09-11
 * @description
 **/
public class AssertUtils {
    public static void isNull(Object object, String msg) {
        if (object instanceof String) {
            if (StringUtils.isBlank(String.valueOf(object))) {
                throw new BusinessException(msg);
            }
        }
        if (null == object) {
            throw new BusinessException(msg);
        }
    }

    public static void verifyEquals(Object arg1, Object arg2, String msg) {
        if (arg1.equals(arg2)) {
            throw new BusinessException(msg);
        }
    }

    public static void verifyNotEquals(Object arg1, Object arg2, String msg) {
        if (!arg1.equals(arg2)) {
            throw new BusinessException(msg);
        }
    }
}
