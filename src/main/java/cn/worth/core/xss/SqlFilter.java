package cn.worth.core.xss;


import cn.worth.common.exception.BusinessException;
import cn.worth.common.utils.StringUtils;

/**
 * SQL过滤
 * @Author: MyIreland on.
 * @Description: https://gitee.com/renrenio/renren-fast/blob/master/src/main/java/io/renren/common/xss/SQLFilter.java
 * @Date: 2019/3/9
 */
public class SqlFilter {

    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public static String sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if(str.contains(keyword)){
                throw new BusinessException("包含非法字符");
            }
        }

        return str;
    }
}