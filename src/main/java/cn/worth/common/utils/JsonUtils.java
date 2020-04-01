package cn.worth.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang.SerializationException;

import java.io.IOException;

/**
 * @Author: MyIreland
 * @Date: 2019/5/27 16:23
 * @Version 1.0
 */
public class JsonUtils {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final ObjectMapper INDENT_OUTPUT_OBJECT_MAPPER = new ObjectMapper();

    static {
        objectMapper.enable(MapperFeature.USE_GETTERS_AS_SETTERS);
        objectMapper.enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        INDENT_OUTPUT_OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        INDENT_OUTPUT_OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        INDENT_OUTPUT_OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private JsonUtils() {
    }

    /**
     * 将对象转换为字符转
     *
     * @param object
     * @return
     */
    public static String obj2Str(Object object) {
        String str = "";
        try {
            str = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 将字符串转换为对象
     *
     * @param str
     * @param cls
     * @return
     */
    public static <T> T str2Obj(String str, Class<T> cls) {
        T object = null;
        try {
            object = objectMapper.readValue(str, cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 对象转为json字符串，不输出空属性
     * @param object
     */
    public static void writeValueWithoutNullValue(Object object) {
        try {
            if (null != object) {
                INDENT_OUTPUT_OBJECT_MAPPER.writeValueAsString(object);
            }
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }


}