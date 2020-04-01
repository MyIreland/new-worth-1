package cn.worth.common.enums;

import cn.worth.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-08-05
 * @description 0菜单 1按钮 2链接
 **/
@Getter
@AllArgsConstructor
public enum MenuTypeEnum implements BaseEnum<Integer> {
    MENU(0, "菜单"),
    BUTTON(1, "按钮"),
    LINK(2, "链接");

    private Integer code;
    private String msg;

    public static MenuTypeEnum getMenuTypeByCode(Integer type) {
        if (type != null) {
            MenuTypeEnum[] values = MenuTypeEnum.values();
            for (MenuTypeEnum value : values) {
                if (value.ordinal() == type) {
                    return value;
                }
            }
        }
        throw new BusinessException("菜单资源存在异常菜单类型：" + type);
    }
}
