package cn.worth.sys.utils;

import cn.worth.common.exception.BusinessException;
import cn.worth.sys.enums.EntityTypeEnum;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-09-01
 * @description
 **/
public class VerifyUtils {
    public static void verifyAdmin(Integer type){
        if(EntityTypeEnum.ADMIN.getCode() == type){
            throw new BusinessException("无权操作！");
        }
    }
}
