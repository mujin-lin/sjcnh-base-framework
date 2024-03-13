package com.sjcnh.abstraction.web.feign;

import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author w
 * @description:
 * @title: ParamBodyInterceptor
 * @projectName sjcnh-base-framework
 * @date 2024/1/4
 * @company sjcnh-ctu
 */
public class ParamBodyInterceptor extends BaseFeignRequestInterceptor{
    private final Logger log = LoggerFactory.getLogger(ParamBodyInterceptor.class);
    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = super.execution(requestTemplate);
        if (null == request){
            return;
        }
        Enumeration<String> bodyNames = request.getParameterNames();
        StringBuilder body = new StringBuilder();
        if (bodyNames != null) {
            while (bodyNames.hasMoreElements()) {
                String name = bodyNames.nextElement();
                String value = request.getParameter(name);
                body.append(name).append("=").append(value).append("&");
            }
        }
        if (body.length() != 0) {
            body.deleteCharAt(body.length() - 1);
            requestTemplate.body(body.toString());
            log.info("openfeign interceptor body:{}", body.toString());
        }
    }
}
