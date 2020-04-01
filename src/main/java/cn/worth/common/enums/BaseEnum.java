package cn.worth.common.enums;
import java.io.Serializable;

public interface BaseEnum<T extends Serializable> {

    T getCode();

    String getMsg();
}
