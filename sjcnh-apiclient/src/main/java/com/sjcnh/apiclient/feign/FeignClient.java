package com.sjcnh.apiclient.feign;

import com.sjcnh.apiclient.annotation.ApiClient;
import com.sjcnh.apiclient.test.ResponseTest;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author chenglin.wu
 * @description:
 * @title: FeignClient
 * @projectName demo
 * @date 2021/8/12
 * @company WHY
 */
@ApiClient
public interface FeignClient {
    /**
     * jhn;l
     * @return ResponseTest
     * @author W
     * @date: 2022/3/9
     */
    @GetMapping("http://192.168.1.25:8009/doc.html#/home")
    ResponseTest requestBaidu();


}
