package org.hyizhou.titaniumstation.common.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 提供一个简单的Json转换工具
 *
 * @author hyizhou
 * @date 2024/1/14
 */
public class SimpleJsonTool {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toString(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T toObject(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) throws JsonProcessingException {
        return objectMapper.readValue(json, typeReference);
    }

}
