package com.sjcnh.abstraction.redis.manager;


import com.sjcnh.abstraction.redis.constants.RedisConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;
/**
 * @author Y
 * @description: redis验证码
 * @title: RedisLoginUserManager
 * @projectName sjcnh-redis
 * @date 2021/4/16 15:20
 * @company sjcnh-ctu
 */
@SuppressWarnings({"all"})
public class RedisValidateCodeManager {


    private static final Logger logger = LoggerFactory.getLogger(RedisValidateCodeManager.class);

    private final StringRedisTemplate stringRedisTemplate;

    public RedisValidateCodeManager(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * @param key 键
     * @param value 值
     * @param time  消息过期时间(s)，若不设置，默认5分钟
     * @return boolean
     * @title isEmpty
     * @description 判断该键值对是否存在，若不存在，则新增，返回true，若存在，则返回false
     * @author H
     * @date: 2020年4月23日
     */
    public boolean isEmpty(String key, String value, Long time) {
        if (time != null) {
            return this.stringRedisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
        }
        return this.stringRedisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 设置当前当前key对应的code
     *
     * @param key  the key
     * @param code the code
     * @author W
     * @date: 2023/6/12
     */
    public void setValidateCode(String key, String code) {
        this.stringRedisTemplate.opsForValue().set(key, code, RedisConstants.REDIS_VALIDATE_CODE_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 刷新当前key对应的code
     *
     * @param key  the key
     * @param code the code
     * @author W
     * @date: 2023/6/12
     */
    public void refreshValidateCode(String key, String code) {

        logger.info("refreshValidateCode " + key + " " + code);

        this.stringRedisTemplate.opsForValue().set(key, code, RedisConstants.REDIS_VALIDATE_CODE_TIMEOUT, TimeUnit.SECONDS);

        logger.info("refreshValidateCode " + key + " " + code);

    }

    /**
     * 获取当前key对应的code
     *
     * @param key the token
     * @return String
     * @author W
     * @date: 2023/6/12
     */
    public String getValidateCode(String key) {
        return this.stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除当前key对应的code
     *
     * @param key the token
     * @author W
     * @date: 2023/6/12
     */
    public void delete(String key) {
        this.stringRedisTemplate.delete(key);
    }
}
