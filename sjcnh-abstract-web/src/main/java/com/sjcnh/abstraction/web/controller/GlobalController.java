package com.sjcnh.abstraction.web.controller;


import com.sjcnh.abstraction.redis.manager.RedisTransEncryptManager;
import com.sjcnh.commons.constants.IntConstants;
import com.sjcnh.commons.response.ResponseResult;
import com.sjcnh.commons.response.ResponseUtils;
import com.sjcnh.commons.utils.TokenUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author w
 * @description:
 * @title: GlobalController
 * @projectName sjcnh-abstract-web
 * @date 2021年05月16日
 */
@RestController
@RequestMapping("/global")
public class GlobalController {
    private final RedisTransEncryptManager redisTransEncryptManager;

    public GlobalController(RedisTransEncryptManager redisTransEncryptManager) {
        this.redisTransEncryptManager = redisTransEncryptManager;
    }

    /**
     * 登录之前获取加密秘钥对密码进行加密处理
     *
     * @return ResponseResult<Map < String, String>>
     * @author W
     * @date 2021/5/16
     */
    @GetMapping("/encrypt")
    public ResponseResult<Map<String, String>> passwordEncrypt() {
        String transToken = TokenUtil.createToken();
        // 获取16位的随机字符串
        String transEncryptKey = RandomStringUtils.randomAlphabetic(IntConstants.INT_16);
        // 将随机字符串存到redis中
        this.redisTransEncryptManager.setTransEncryptKey(transToken, transEncryptKey);

        Map<String, String> map = new HashMap<>(6);
        map.put("transToken", transToken);
        map.put("transEncryptKey", transEncryptKey);
        return ResponseUtils.success(map);
    }



}
