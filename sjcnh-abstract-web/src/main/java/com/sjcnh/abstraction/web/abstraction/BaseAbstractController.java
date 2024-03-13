package com.sjcnh.abstraction.web.abstraction;


import com.sjcnh.abstraction.redis.dto.LoginUser;
import com.sjcnh.abstraction.redis.manager.RedisLoginUserManager;
import com.sjcnh.commons.enums.ErrCodeEnum;
import com.sjcnh.commons.exception.BusinessException;
import com.sjcnh.commons.response.ResponseResult;
import com.sjcnh.commons.utils.TokenUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * @author w
 * @description:
 * @title: BaseAbstractController
 * @projectName sjcnh-common
 * @date 2023/11/10
 * @company sjcnh-ctu
 */
@SuppressWarnings("ALL")
public abstract class BaseAbstractController<T> {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * request
     **/
    @Autowired
    private HttpServletRequest request;

    /**
     * 管理用户redis
     **/
    @Autowired
    private RedisLoginUserManager redisManager;

    /**
     * 添加用户到redis中，请在使用时，对loginUser的token与userId进行后缀的添加
     *
     * @param loginUser
     * @author Y
     * @date 2021/4/16 16:09
     **/
    public void setLoginUserToRedis(LoginUser loginUser) {

        this.redisManager.setLoginUser(loginUser);
    }

    /**
     * 获取redis中的LoginUser
     *
     * @return {@link LoginUser }
     * @author Y
     * @description
     * @date 2021/4/16 16:09
     **/
    public LoginUser getLoginUserFromRedis() throws BusinessException {
        // 获取登录对象token
        String token = TokenUtil.getTokenFromServlet(this.request);
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(ErrCodeEnum.AUTH.getCode(), "请先登录");
        }
        // 返回登录对象
        LoginUser loginUser = this.redisManager.getLoginUserByToken(this.getToken());
        if (ObjectUtils.anyNull(loginUser)) {
            throw new BusinessException(ErrCodeEnum.LOGIN_EXPIRED.getCode(), "登录已过期");
        }
        return loginUser;
    }

    /**
     * 通过token去删除redis中的user
     *
     * @param token
     * @author Y
     * @date 2021/4/15 21:36
     **/
    public void deleteLoginUserFromRedis(String token) {
        this.redisManager.deleteLoginUser(token);
    }

    /**
     * 更新redis中的LoginUser
     *
     * @param loginUser
     * @author Y
     * @date 2021/4/16 16:11
     **/
    public void updateLoginUserToRedis(LoginUser loginUser) {
        this.redisManager.refreshLoginTime(loginUser);
    }

    /**
     * 获取当前请求的httpServlet对象
     *
     * @return: HttpServletRequest
     * @author: bin.hu
     * @date: 2021/4/16
     */
    public HttpServletRequest getHttpServletRequest() {
        return this.request;
    }

    /**
     * 获取redisTemplate的对象
     *
     * @return: RedisTemplate
     * @author: bin.hu
     * @date: 2021/4/16
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return this.redisTemplate;
    }

    /**
     * 获取redisLoginUserManager
     *
     * @return RedisLoginUserManager 登录对象操作类
     * @author w
     * @date: 2021/4/16
     */
    public RedisLoginUserManager getRedisManager() {
        return redisManager;
    }

    /**
     * 获取header中的token
     *
     * @return String 请求中的token
     * @author w
     * @date: 2021/4/16
     */
    public String getToken() {
        return TokenUtil.getTokenFromServlet(this.request);
    }

    /**
     * 保存方法
     *
     * @param entity 实体类数据
     * @return ResponseResult<T> 返回保存后的实体
     * @author W
     * @date: 2023/11/10
     */
    public abstract ResponseResult<T> insert(T entity);

    /**
     * 通过id删除实体
     *
     * @param entityId 实体类id
     * @return ResponseResult<String>
     * @author W
     * @date: 2023/11/10
     */
    public abstract <ID extends Serializable> ResponseResult<String> delete(ID entityId);

    /**
     * 通过实体类的id集合删除实体
     *
     * @param ids 实体类的id集合
     * @return ResponseResult<String>
     * @author W
     * @date: 2023/11/10
     */
    public abstract <ID extends Serializable> ResponseResult<String> deleteListId(List<ID> ids);

    /**
     * 更新实体
     *
     * @param entity the entity
     * @return ResponseResult<T>
     * @author W
     * @date: 2023/11/10
     */
    public abstract ResponseResult<T> update(T entity);

}
