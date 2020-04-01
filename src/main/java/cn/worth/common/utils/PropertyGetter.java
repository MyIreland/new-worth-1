package cn.worth.common.utils;

public interface PropertyGetter<T, K> {

    K getProperty(T obj);
}