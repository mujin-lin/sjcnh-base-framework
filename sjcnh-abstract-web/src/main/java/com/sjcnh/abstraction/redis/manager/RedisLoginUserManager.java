package com.sjcnh.abstraction.redis.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjcnh.abstraction.redis.constants.RedisConstants;
import com.sjcnh.abstraction.redis.dto.LoginUser;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author Y
 * @description: redis中对user的常规操作，如果其他端的用户需要使用,请以Redis（）LoginUserManager格式创建类
 * @title: RedisLoginUserManager
 * @projectName sjcnh-redis
 * @date 2021/4/16 15:20
 * @company sjcnh-ctu
 */
@SuppressWarnings("unused")
public class RedisLoginUserManager {
    private final Logger log = LoggerFactory.getLogger(RedisLoginUserManager.class);


    private final StringRedisTemplate stringRedisTemplate;

    private final RedisTemplate<String, LoginUser> redisTemplate;

    private final ObjectMapper objectMapper;

    public RedisLoginUserManager(StringRedisTemplate stringRedisTemplate, RedisTemplate<String, LoginUser> redisTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 添加用户到redis中
     *
     * @param loginUser 登录对象
     * @author Y
     * @description 设置当前登录对象
     * @date 2021/4/16 16:17
     **/
    public void setLoginUser(LoginUser loginUser) {
        Assert.notNull(loginUser.getRedisId(), "token为userId，设置Redis用户失败");
        Assert.notNull(loginUser.getToken(), "token为null，设置Redis用户失败");

        // 检查redis中是否存储当前用户，如果有则删除，并重新添加
        LoginUser oldLoginUser = this.getLoginUserByToken(loginUser.getRedisId());
        if (oldLoginUser != null) {
            this.deleteLoginUser(oldLoginUser.getToken());
        }
        log.debug("RedisLoginUserManager createLoginUser login name:{} , login token:{} , login id:{}", loginUser.getName(), loginUser.getToken(), loginUser.getRedisId());

        // 添加token
        stringRedisTemplate.opsForValue().set(loginUser.getToken(), loginUser.getRedisId(),
                RedisConstants.REDIS_LOGIN_USER_TIMEOUT, TimeUnit.SECONDS);
        // 添加登录对象
        redisTemplate.opsForValue().set(loginUser.getRedisId(), loginUser, RedisConstants.REDIS_LOGIN_USER_TIMEOUT,
                TimeUnit.SECONDS);
    }

    /**
     * 获取redis中的LoginUser
     *
     * @param token 登录令牌
     * @return {@link LoginUser }
     * @author Y
     * @date 2021/4/16 16:17
     **/
    public LoginUser getLoginUserByToken(String token) {
        // 判断token是否为空，为空则返回null
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String loginUserId = getLoginUserId(token);
        // 判断登录对象是否为空，为空则返回null
        if (StringUtils.isBlank(loginUserId)) {
            return null;
        }
        try {
            return this.getLoginUser(loginUserId);
        } catch (Exception e) {
            log.debug("get user has error: ", e);
            return null;
        }
    }

    /**
     * 删除redis中的user
     *
     * @param token 登录的令牌
     * @author Y
     * @date 2021/4/16 16:17
     **/
    public void deleteLoginUser(String token) {
        LoginUser loginUser = this.getLoginUserByToken(token);
        if (loginUser != null) {
            redisTemplate.delete(loginUser.getRedisId());
        }
        stringRedisTemplate.delete(token);
    }

    /**
     * 更新redis中的LoginUser
     *
     * @param loginUser 登录对象
     * @author Y
     * @date 2021/4/16 16:18
     **/
    public void refreshLoginTime(LoginUser loginUser) {

        // 过期时间
        Long expire = stringRedisTemplate.getExpire(loginUser.getToken(), TimeUnit.SECONDS);

        // 需要更新expire
        if (expire == null || expire < RedisConstants.REDIS_LOGIN_USER_EXPIRE) {
            log.debug("RedisLoginUserManager delayLoginUserTime login name:{} , login token:{} , login id:{}", loginUser.getName(), loginUser.getToken(), loginUser.getRedisId());
            stringRedisTemplate.expire(loginUser.getToken(), RedisConstants.REDIS_LOGIN_USER_TIMEOUT, TimeUnit.SECONDS);
            redisTemplate.expire(loginUser.getRedisId(), RedisConstants.REDIS_LOGIN_USER_TIMEOUT, TimeUnit.SECONDS);
        }
    }

    /**
     * 通过userId获取redis中的LoginUser
     *
     * @param userId 用户id
     * @return {@link LoginUser }
     * @author Y
     * @date 2021/4/16 16:18
     **/
    public LoginUser getLoginUser(String userId) throws JsonProcessingException {
        if (redisTemplate == null) {
            log.error("RedisLoginUserManager getLoginUserByUserId stringRedisTemplate is null");
            return null;
        } else {
            return redisTemplate.opsForValue().get(userId);
        }
    }

    /**
     * 从redis获取userId
     *
     * @param token 登录令牌
     * @return {@link String }
     * @author Y
     * @date 2021/4/16 14:23
     **/
    public String getLoginUserId(String token) {
        log.debug("RedisLoginUserManager getLoginUserId token" + token);
        if (stringRedisTemplate == null) {
            log.error("RedisLoginUserManager getLoginUserId stringRedisTemplate is null");
        } else {
            Object obj = stringRedisTemplate.opsForValue().get(token);
            if (obj != null) {
                return obj.toString();
            }
        }
        return null;
    }

    /**
     * 通过id批量删除登录对象
     *
     * @param redisId the redisId
     * @author w
     * @date: 2021/8/6
     */
    public void deleteLoginUserById(String... redisId) {
        log.debug("RedisLoginUserManager deleteLoginUserById id:{}", Arrays.toString(redisId));
        if (ObjectUtils.anyNull(redisTemplate)) {
            log.error("RedisLoginUserManager deleteLoginUserById redisTemplate is null");
        } else {
            for (String id : redisId) {
                LoginUser obj = redisTemplate.opsForValue().get(id);
                if (ObjectUtils.allNotNull(obj) && obj != null) {
                    this.deleteLoginUser(obj.getToken());
                }
            }
        }
    }

}
