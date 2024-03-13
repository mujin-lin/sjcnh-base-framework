package com.sjcnh.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjcnh.abstraction.web.abstraction.BaseAbstractController;
import com.sjcnh.data.dto.PageDto;
import com.sjcnh.data.entity.BaseEntity;
import com.sjcnh.data.utils.ReflectionUtils;
import com.sjcnh.commons.enums.ErrCodeEnum;
import com.sjcnh.commons.response.ResponseResult;
import com.sjcnh.commons.response.ResponseUtils;
import com.sjcnh.web.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Y
 * @description: 基础controller
 * @title: BaseController
 * @projectName sjcnh-abstract-web
 * @date 2021/4/16 15:20
 **/
@Slf4j
@SuppressWarnings("ALL")
public class BaseController<S extends BaseService<M, T>, M extends BaseMapper<T>, T extends BaseEntity> extends BaseAbstractController<T> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * request
     **/
    @Autowired
    private HttpServletRequest request;




    /**
     * 业务处理类
     */
    @Autowired
    protected S baseService;



    /**
     * 获取业务处理层的实例
     *
     * @return S 业务处理层的类
     * @author w
     * @date: 2021/4/16
     */
    public S getBaseService() {
        return baseService;
    }



    /**
     * 新增实体数据
     *
     * @return ResponseResult<T>
     * @param: entity
     * @author W
     * @date 2021-05-25
     */
    @Override
    public ResponseResult<T> insert(T entity) {
        try {
            Class<? extends BaseEntity> aClass = entity.getClass();
            Method setId = aClass.getDeclaredMethod("setId");
            setId.invoke(entity, (Object) null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.info("set id null error");
        }
        this.getBaseService().save(entity);
        return ResponseUtils.success(entity);
    }

    /**
     * 通过ID删除数据
     *
     * @return ResponseResult<String>
     * @param: id 实体类的id
     * @author W
     * @date 2021-05-25
     */
    @Override
    public <ID extends Serializable> ResponseResult<String> delete(ID id) {
        this.getBaseService().removeById(id);
        return ResponseUtils.success();
    }

    /**
     * 通过ID删除数据
     *
     * @return ResponseResult<String>
     * @param: id 实体类的id
     * @author W
     * @date 2021-05-25
     */
    @Override
    public <ID extends Serializable> ResponseResult<String> deleteListId(List<ID> id) {
        this.getBaseService().removeByIds(id);
        return ResponseUtils.success();
    }

    /**
     * 更新一条数据
     *
     * @return ResponseResult<T>
     * @param: entity 实体类数据
     * @author W
     * @date 2021-05-25
     */
    @Override
    public ResponseResult<T> update(T entity) {
        boolean flag = this.getBaseService().saveOrUpdate(entity);
        if (flag) {
            return ResponseUtils.success();
        }
        return ResponseUtils.fail(ErrCodeEnum.DATA_CHECK.getCode(), "更新失败");
    }

    /**
     * 分页查询的数据
     *
     * @return ResponseResult<Page < T>>
     * @param: pageDto 分页的dto
     * @author W
     * @date 2021-05-24
     */
    public ResponseResult<Page<T>> findPage(PageDto<T> pageDto) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        QueryWrapper<T> queryWrapper = this.getPageQueryWrapper(pageDto);
        // 如果没有传排序字段和排序方式则使用create_time默认排序
//        Page<T> inputPage = this.getOrder(pageDto);
        Page<T> page = this.getBaseService().page(pageDto.getPage(), queryWrapper);
        return ResponseUtils.success(page);
    }

    /**
     * 获取分页的查询wrapper
     *
     * @return QueryWrapper<T>
     * @param: pageDto
     * @author W
     * @date 2021-05-29
     */
    private QueryWrapper<T> getPageQueryWrapper(PageDto<T> pageDto) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        QueryWrapper<T> queryWrapper = null;
        if (ObjectUtils.allNotNull(pageDto.getSearchExample())) {
            queryWrapper = ReflectionUtils.createPageWrapper2Field(pageDto.getSearchExample(), pageDto.getExtras());
        }
        return queryWrapper;
    }

    /**
     * 分页查询，排除自己 适用于管理人员的用户查询，排除自己
     *
     * @return ResponseResult<Page < T>>
     * @param: pageDto 分页的dto
     * @param: myselfId 当前登录用户的id
     * @author W
     * @date 2021-05-29
     */
    public ResponseResult<Page<T>> findPage(PageDto<T> pageDto, String myselfId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        QueryWrapper<T> queryWrapper = this.getPageQueryWrapper(pageDto);
//        Page<T> inputPage = this.getOrder(pageDto);
        queryWrapper.ne("id", myselfId);

        Page<T> page = this.getBaseService().page(pageDto.getPage(), queryWrapper);
        return ResponseUtils.success(page);
    }

    /**
     * 获取分页排序的数据
     *
     * @return Page<T>
     * @param: pageDto 分页的dto
     * @author W
     * @date 2021-05-29
     */
    private Page<T> getOrder(PageDto<T> pageDto) {
        // 如果没有传排序字段和排序方式则使用create_time默认排序
        Page<T> inputPage = pageDto.getPage();
        List<OrderItem> orders = inputPage.getOrders();
        return inputPage;
    }


}
