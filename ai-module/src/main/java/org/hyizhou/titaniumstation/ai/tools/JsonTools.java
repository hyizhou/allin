package org.hyizhou.titaniumstation.ai.tools;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @date 2024/6/5
 */
public class JsonTools {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 设置不包含null属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 将对象转换为JSON字符串，忽略null值的属性
     *
     * @param object 待转换的对象
     * @return JSON字符串
     */
    public static String toJsonString(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

