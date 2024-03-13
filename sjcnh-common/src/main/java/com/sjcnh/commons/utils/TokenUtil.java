package com.sjcnh.commons.utils;


import com.sjcnh.commons.RequestHeaderFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author Y
 * @description: token工具类
 * @title: TokenUtil
 * @projectName sjcnh-abstract-web
 * @date 2021/4/16 15:20
 **/
public final class TokenUtil {
    private static final Logger log = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * token关键字
     */
    public static final String TOKEN_HEADER = "authorization";
    /**
     * 请求来源关键字
     */
    public static final String REQUEST_SOURCE = "RequestSource";



    private TokenUtil() {
    }

    public static String createToken() {
        //token算法

        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 通过请求对象获取token 兼容exchange
     *
     * @param request 请求对象
     * @return String
     * @author w
     * @date: 2021/4/20
     */
    public static <T> String getTokenFromServlet(T request) {
        String token = "";
        token = RequestHeaderFactory.getSpecifyHeader(request, TOKEN_HEADER);
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return token.replace(TOKEN_PREFIX, "");
    }

    /**
     * 通过请求对象获取请求来源
     *
     * @param request 请求对象
     * @return String
     * @author w
     * @date: 2021/4/20
     */
    public static <T> String getRequestSource(T request) {
        String requestSource = "";
        requestSource = RequestHeaderFactory.getSpecifyHeader(request, REQUEST_SOURCE);
        if (StringUtils.isBlank(requestSource)) {
            return null;
        }
        return requestSource;
    }

}
