package cn.worth.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStateEnum implements BaseEnum<Integer> {
    ACTIVE(0, "激活"),
    LOCKED(1, "锁定"),
    ;
    private Integer code;

    private String msg;
}
