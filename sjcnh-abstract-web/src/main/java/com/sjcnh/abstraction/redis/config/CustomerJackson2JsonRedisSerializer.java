package com.sjcnh.abstraction.redis.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Redis使用jackson序列化
 *
 * @author W
 */
@SuppressWarnings("unused")
public class CustomerJackson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    private final Logger log = LoggerFactory.getLogger(CustomerJackson2JsonRedisSerializer.class);

    /**
     * 需要序列化的对象
     */
    private final Class<T> clazz;
    /**
     * 转成json格式的对象
     */
    private ObjectMapper objectMapper;


    public CustomerJackson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) {
        if (t == null) {
            return new byte[0];
        }
        try {
            String str =  this.objectMapper.writeValueAsString(t);
            return str.getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            log.debug("序列化对象失败:", e);
            throw new SerializationException("序列化对象失败");
        }
    }

    @Override
    public T deserialize(byte[] bytes)   {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return this.objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            log.debug("反序列化对象失败:", e);
            throw new SerializationException("反序列化对象失败");
        }
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "'objectMapper' must not be null");
        this.objectMapper = objectMapper;
    }

    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}
