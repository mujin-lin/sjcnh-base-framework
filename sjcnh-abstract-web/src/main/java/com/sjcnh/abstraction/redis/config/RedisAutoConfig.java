package com.sjcnh.abstraction.redis.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.sjcnh.abstraction.redis.constants.RedisConstants;
import com.sjcnh.abstraction.redis.dto.LoginUser;
import com.sjcnh.abstraction.redis.manager.RedisLoginUserManager;
import com.sjcnh.abstraction.redis.manager.RedisTransEncryptManager;
import com.sjcnh.abstraction.redis.manager.RedisValidateCodeManager;
import com.sjcnh.commons.constants.IntConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author w
 * @description:
 * @title: RedisAutoConfig
 * @projectName base_framework
 * @date 2021/4/16 10:33
 * @company sjcnh-ctu
 */
@SuppressWarnings("all")
@Configuration
@ConfigurationProperties(value = "sjcnh.web.global.config.redis")
public class RedisAutoConfig {
    /**
     * 设置序列化器时是否要加上类全限定名
     */
    private boolean serializerFullyQualifiedName;

    /**
     * 配置简单的redisTemplate序列化器
     *
     * @param redisConnectionFactory redis的连接工厂对象
     * @return RedisTemplate<String, Object> redisTemplate
     * @author w
     * @date: 2021/4/16
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> strKeyRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        return getRedisTemplate(redisConnectionFactory, template);
    }

    /**
     * 配置以Object对象为key的redisTemplate序列化器
     *
     * @param connectionFactory redis的连接工厂对象
     * @return RedisTemplate<Object, Object>
     * @author w
     * @date: 2021/4/25
     */
    @Bean("objTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        return getRedisTemplate(connectionFactory, template);
    }

    /**
     * redis序列化器的mapper
     *
     * @return ObjectMapper
     * @author W
     * @date: 2023/7/12
     */
    public ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        if (this.serializerFullyQualifiedName) {
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }
        return mapper;
    }

    /**
     * 设置redisTemplate的序列化器
     *
     * @param connectionFactory redis连接工厂
     * @param template          redisTemplate对象
     * @return void
     * @author w
     * @date: 2021/4/25
     */
    private <T> RedisTemplate<T, Object> getRedisTemplate(RedisConnectionFactory connectionFactory, RedisTemplate<T, Object> template) {
        template.setConnectionFactory(connectionFactory);
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//
//        serializer.setObjectMapper(redisObjectMapper());
//        template.setValueSerializer(serializer);
//        template.setHashValueSerializer(serializer);
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(StringRedisSerializer.UTF_8);
        template.setHashKeySerializer(StringRedisSerializer.UTF_8);
        return template;
    }


    /**
     * 普通的redisTemplate的缓存设置
     *
     * @param redisTemplate redisTemplate对象
     * @return CacheManager 缓存管理器
     * @author w
     * @date: 2021/4/16
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        config = config.entryTtl(Duration.ZERO)
                // 设置 key为string序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getStringSerializer()))
                // 设置value为json序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
                // 不缓存空值
                .disableCachingNullValues();


        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_ONE_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_1)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_TWO_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_2)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_THREE_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_3)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_FOUR_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_4)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_FIVE_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_5)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_SIX_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_6)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_SEVEN_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_7)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_EIGHT_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_8)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_NINE_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_9)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_TEN_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_10)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_FIFTEEN_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_15)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_TWENTY_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_20)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_THIRTY_MINUTE, config.entryTtl(Duration.ofMinutes(IntConstants.INT_30)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_ONE_HOUR, config.entryTtl(Duration.ofHours(IntConstants.INT_1)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_ONE_AND_HALF_HOURS, config.entryTtl(Duration.ofHours(IntConstants.INT_1).plusMinutes(IntConstants.INT_30)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_TWO_HOUR, config.entryTtl(Duration.ofHours(IntConstants.INT_2)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_THREE_HOUR, config.entryTtl(Duration.ofHours(IntConstants.INT_3)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_FOUR_HOUR, config.entryTtl(Duration.ofHours(IntConstants.INT_4)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_TWENTY_FOUR_HOUR, config.entryTtl(Duration.ofHours(IntConstants.INT_24)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_THE_WHOLE_DAY, config.entryTtl(Duration.ofDays(IntConstants.INT_1)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_TWO_DAY, config.entryTtl(Duration.ofDays(IntConstants.INT_2)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_THREE_DAY, config.entryTtl(Duration.ofDays(IntConstants.INT_3)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_FOUR_DAY, config.entryTtl(Duration.ofDays(IntConstants.INT_4)));
        configMap.put(RedisConstants.RedisCacheConstants.CACHE_THIRTY_DAY, config.entryTtl(Duration.ofDays(IntConstants.INT_30)));

        // 使用自定义的缓存配置初始化一个cacheManager
        return RedisCacheManager.builder(Objects.requireNonNull(redisTemplate.getConnectionFactory()))
                .cacheDefaults(config)
                // 一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .initialCacheNames(configMap.keySet())
                .withInitialCacheConfigurations(configMap)
                .build();
    }

    /**
     * 登录用户管理类
     *
     * @param stringRedisTemplate 注入ioc的StringRedisTemplate
     * @return RedisLoginUserManager
     * @author W
     * @date: 2023/12/4
     */
    @Bean("redisLoginUserManager")
    public RedisLoginUserManager createRedisUserManager(StringRedisTemplate stringRedisTemplate) {
        RedisConnectionFactory connectionFactory = stringRedisTemplate.getConnectionFactory();

        RedisTemplate<String, LoginUser> loginUserTemplate = new RedisTemplate<>();
        Assert.state(connectionFactory != null, "RedisConnectionFactory is required");
        loginUserTemplate.setConnectionFactory(connectionFactory);

        ObjectMapper jacksonMapper = redisObjectMapper();
        // 设置序列化器
        CustomerJackson2JsonRedisSerializer<LoginUser> serializer = new CustomerJackson2JsonRedisSerializer<>(LoginUser.class);
        serializer.setObjectMapper(jacksonMapper);
        loginUserTemplate.setValueSerializer(serializer);
        loginUserTemplate.setHashValueSerializer(serializer);
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        loginUserTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        loginUserTemplate.setHashKeySerializer(StringRedisSerializer.UTF_8);
        loginUserTemplate.afterPropertiesSet();

        return new RedisLoginUserManager(stringRedisTemplate, loginUserTemplate, jacksonMapper);
    }

    /**
     * redis随机加密密文管理类
     *
     * @param stringRedisTemplate StringRedisTemplate的序列化器
     * @author W
     * @date: 2023/12/4
     */
    @Bean("redisTransEncryptManager")
    @DependsOn("stringRedisTemplate")
    public RedisTransEncryptManager createRedisTranEncryptManager(StringRedisTemplate stringRedisTemplate) {
        return new RedisTransEncryptManager(stringRedisTemplate);
    }

    /**
     * redis校验码管理器
     *
     * @param stringRedisTemplate the stringRedisTemplate
     * @return RedisValidateCodeManager
     * @author W
     * @date: 2023/12/4
     */
    @Bean("redisValidateCodeManager")
    @DependsOn("stringRedisTemplate")
    public RedisValidateCodeManager createRedisValidateCodeManager(StringRedisTemplate stringRedisTemplate) {
        return new RedisValidateCodeManager(stringRedisTemplate);
    }

    /**
     * 创建密码encoder类
     *
     * @return PasswordEncoder
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
