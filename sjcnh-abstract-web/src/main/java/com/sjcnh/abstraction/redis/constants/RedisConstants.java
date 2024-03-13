package com.sjcnh.abstraction.redis.constants;

/**
 * @author w
 * @description:
 * @title: RedisConstants
 * @projectName sjcnh-redis
 * @date 2023/6/12
 * @company sjcnh-ctu
 */
public final class RedisConstants {
    private RedisConstants() {

    }

    /**
     * 默认登录在线时长 (秒)
     */
    public static final Integer REDIS_LOGIN_USER_TIMEOUT = 30 * 60;

    /**
     * 登录时长小于此时间时会进行刷新redis (秒)
     */
    public static final Integer REDIS_LOGIN_USER_EXPIRE = 60 * 25;
    /**
     * 过期秒数 验证码 过期秒数
     */
    public final static long REDIS_VALIDATE_CODE_TIMEOUT = 60 * 5;

    /**
     * @author w
     * @description: redis cache manager的时间空间
     * @title: RedisCacheConstants
     * @projectName sjcnh-redis
     * @date 2023/6/12
     * @company sjcnh-ctu
     */
    public static class RedisCacheConstants {
        /**
         * 缓存时间1分钟
         */
        public static final String CACHE_ONE_MINUTE = "1m";
        /**
         * 缓存时间2分钟
         */
        public static final String CACHE_TWO_MINUTE = "2m";
        /**
         * 缓存时间3分钟
         */
        public static final String CACHE_THREE_MINUTE = "3m";
        /**
         * 缓存时间4分钟
         */
        public static final String CACHE_FOUR_MINUTE = "4m";
        /**
         * 缓存时间5分钟
         */
        public static final String CACHE_FIVE_MINUTE = "5m";
        /**
         * 缓存时间6分钟
         */
        public static final String CACHE_SIX_MINUTE = "6m";
        /**
         * 缓存时间7分钟
         */
        public static final String CACHE_SEVEN_MINUTE = "7m";
        /**
         * 缓存时间8分钟
         */
        public static final String CACHE_EIGHT_MINUTE = "8m";
        /**
         * 缓存时间9分钟
         */
        public static final String CACHE_NINE_MINUTE = "9m";
        /**
         * 缓存时间10分钟
         */
        public static final String CACHE_TEN_MINUTE = "10m";
        /**
         * 缓存时间15分钟
         */
        public static final String CACHE_FIFTEEN_MINUTE = "15m";
        /**
         * 缓存时间20分钟
         */
        public static final String CACHE_TWENTY_MINUTE = "20m";
        /**
         * 缓存时间30分钟
         */
        public static final String CACHE_THIRTY_MINUTE = "30m";
        /**
         * 缓存时间1小时
         */
        public static final String CACHE_ONE_HOUR = "1h";
        /**
         * 一个半小时
         */
        public static final String CACHE_ONE_AND_HALF_HOURS = "1.5h";
        /**
         * 两个小时
         */
        public static final String CACHE_TWO_HOUR = "2h";
        /**
         * 3个小时
         */
        public static final String CACHE_THREE_HOUR = "3h";
        /**
         * 4个小时
         */
        public static final String CACHE_FOUR_HOUR = "4h";
        /**
         * 24个小时
         */
        public static final String CACHE_TWENTY_FOUR_HOUR = "24h";
        /**
         * 1天
         */
        public static final String CACHE_THE_WHOLE_DAY = "1d";
        /**
         * 2天
         */
        public static final String CACHE_TWO_DAY = "2d";
        /**
         * 3天
         */
        public static final String CACHE_THREE_DAY = "3d";
        /**
         * 4天
         */
        public static final String CACHE_FOUR_DAY = "4d";
        /**
         * 30天
         */
        public static final String CACHE_THIRTY_DAY = "30d";
    }
}
