package com.sjcnh.web.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.sjcnh.data.entity.BaseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Y
 * @description: 基础Service
 * @title: BaseService
 * @projectName sjcnh-abstract-web
 * @date 2021/4/16 15:20
 **/
@SuppressWarnings("all")
public class BaseService<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> {


    /**
     * 数据的insert操作
     *
     * @param entity 实体类的对象
     * @return boolean
     * @author w
     * @date: 2021/4/20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(T entity) {
        return SqlHelper.retBool(getBaseMapper().insert(entity));
    }

    /**
     * 通过ID删除一条数据
     *
     * @param id id
     * @return boolean
     * @author w
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        return SqlHelper.retBool(this.getBaseMapper().deleteById(id));
    }

    /**
     * 通过map直接删除数据
     *
     * @param columnMap map
     * @return boolean
     * @author w
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByMap(Map<String, Object> columnMap) {
        Assert.notEmpty(columnMap, "error: columnMap must not be empty");
        return SqlHelper.retBool(getBaseMapper().deleteByMap(columnMap));
    }

    /**
     * 通过wrapper删除数据
     *
     * @param queryWrapper wrapper
     * @return boolean
     * @author w
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(Wrapper<T> queryWrapper) {
        return SqlHelper.retBool(getBaseMapper().delete(queryWrapper));
    }


    /**
     * 通过实体类更新
     *
     * @param entity 实体类
     * @return boolean
     * @author w
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(T entity) {
        return SqlHelper.retBool(getBaseMapper().updateById(entity));
    }

    /**
     * 通过wrapper直接更新
     *
     * @param updateWrapper wrapper
     * @return boolean
     * @author w
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Wrapper<T> updateWrapper) {
        return update(null, updateWrapper);
    }

    /**
     * 通过entity和wrapper进行更新操作
     *
     * @param entity        实体类
     * @param updateWrapper 更新的Wrapper
     * @return boolean
     * @author w
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        return SqlHelper.retBool(getBaseMapper().update(entity, updateWrapper));
    }
}
