package cn.worth.common.utils;

import com.xiaoleilu.hutool.collection.CollectionUtil;

import java.util.*;

/**
 * @Author: chenxiaoqing9
 * @Date: Created in 2019/3/19
 * @Description: 工具类
 * @Modified by:
 */
public class CollectionUtils extends CollectionUtil {

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 集合转成 HashMap<J, List<K>> 格式
     *
     * @param list
     * @param propertyGetter 接口（通过J类型获取K类型的集合）
     * @param <J>            Map中key的类型
     * @param <K>            Map中，value 集合存储的类型
     * @return HashMap<J, List < K>>
     */
    public static <J, K> HashMap<J, List<K>> list2MapList(Collection<K> list, PropertyGetter<K, J> propertyGetter) {
        LinkedHashMap<J, List<K>> hm = new LinkedHashMap<>();
        for (K each : list) {
            J key = propertyGetter.getProperty(each);
            List<K> exists = hm.get(key);
            if (exists == null) {
                exists = new ArrayList<>();
                hm.put(key, exists);
            }
            exists.add(each);
        }
        return hm;
    }

    public static <J, K> HashMap<J, K> list2MapObject(Collection<K> list, PropertyGetter<K, J> propertyGetter) {
        LinkedHashMap<J, K> hm = new LinkedHashMap<>();
        for (K each : list) {
            J key = propertyGetter.getProperty(each);
            K exists = hm.get(key);
            if (null == exists) {
                hm.put(key, each);
            }
        }
        return hm;
    }

}

