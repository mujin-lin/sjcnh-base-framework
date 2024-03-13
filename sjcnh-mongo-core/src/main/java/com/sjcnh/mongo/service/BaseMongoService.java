package com.sjcnh.mongo.service;

import com.sjcnh.mongo.doc.BaseDocument;
import com.sjcnh.mongo.page.PageMongoDto;
import com.sjcnh.mongo.utils.QueryUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author w
 * @description:
 * @title: BaseMongoService
 * @projectName sjcnh-mongo-core
 * @date 2023/10/18
 * @company sjcnh-ctu
 */
@SuppressWarnings({"all"})
public class BaseMongoService<T extends BaseDocument, R extends MongoRepository<T, Serializable>> {

    @Autowired
    private R baseRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    /**
     * 通过实体id删除当前文档数据
     *
     * @param id the id
     * @author W
     * @date: 2023/10/18
     */
    @Transactional(rollbackFor = {Exception.class})
    public void deleteById(Serializable id) {
        this.baseRepository.deleteById(id);
    }

    /**
     * 通过id集合删除当前id所对应的数据
     *
     * @param ids the ids
     * @author W
     * @date: 2023/10/18
     */
    @Transactional(rollbackFor = {Exception.class})
    public void deleteByIds(Serializable... ids) {
        List<Serializable> idsList = new ArrayList<>(ids.length);
        Collections.addAll(idsList, ids);
        this.baseRepository.deleteAllById(idsList);
    }

    /**
     * 通过id集合删除当前id所对应的数据
     *
     * @param ids the ids
     * @author W
     * @date: 2023/10/18
     */
    @Transactional(rollbackFor = {Exception.class})
    public <ID extends Serializable> void deleteByIds(Iterable<ID> ids) {
        this.baseRepository.deleteAllById(ids);
    }

    /**
     * 通过实体模糊匹配查询数据
     *
     * @param example the example
     * @return List<T>
     * @author W
     * @date: 2023/10/18
     */
    public List<T> find(T example) {
        return this.baseRepository.findAll(Example.of(example));
    }

    /**
     * 查找所有当前的数据
     *
     * @return List<T>
     * @author W
     * @date: 2023/10/18
     */
    public List<T> findAll() {
        return this.baseRepository.findAll();
    }

    /**
     * 分页查询数据
     *
     * @param example  the example
     * @param pageable the pageable
     * @return Page<T>
     * @author W
     * @date: 2023/10/18
     */
    public Page<T> findPage(T example, Pageable pageable) {
        return this.baseRepository.findAll(Example.of(example), pageable);
    }

    /**
     * 分页查询数据
     *
     * @param example  查询模板
     * @param matcher  matcher表达式
     * @param pageable 分页对象
     * @return Page<T>
     * @author W
     * @date: 2023/10/18
     */
    public Page<T> findPage(T example, ExampleMatcher matcher, Pageable pageable) {
        return this.baseRepository.findAll(Example.of(example, matcher), pageable);
    }

    /**
     * 通过mongoTemplate进行分页查询
     *
     * @param pageSearch the pageSearch
     * @return Page<T> 分页查询数据
     * @throws NoSuchMethodException     没找到方法异常
     * @throws IllegalAccessException    参数异常
     * @throws InvocationTargetException 反射调用异常
     * @author W
     * @date: 2023/10/19
     */
    public Page<T> findPage(PageMongoDto<T> pageSearch) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        PageRequest pageDto = pageSearch.getPageRequest();
        // 创建查询的基本语句
        Query query = QueryUtils.createLikeQuery(pageSearch.getSearchExample(), pageDto.getSort().isSorted(), pageDto.getSort());
        // 创建扩展语句，主要用于时间的扩展
        QueryUtils.queryWithExtra(query, pageSearch.getPageExtras());

        // count总条数
        long count = mongoTemplate.count(query, pageSearch.getSearchClass());
        long totalPage = count / pageDto.getPageSize();
        long mod = count % pageDto.getPageSize();
        if (mod != 0) {
            totalPage += 1;
        }

        // 构造skip和limit实现真正的分页
        QueryUtils.pagingQuery(pageSearch, query);
        // 查询数据并返回
        List<T> content = mongoTemplate.find(query, pageSearch.getSearchClass());
        return new PageImpl<>(content, pageDto, totalPage);
    }


    /**
     * 通过id查询数据
     *
     * @param id the id
     * @return T
     * @author W
     * @date: 2023/10/18
     */
    public T findById(Serializable id) {
        return this.baseRepository.findById(id).orElse(null);
    }

    /**
     * 保存或更新
     *
     * @param entity the entity
     * @return T
     * @author W
     * @date: 2023/10/18
     */
    @Transactional(rollbackFor = {Exception.class})
    public T save(T entity) {
        initSaveData(entity);
        return this.baseRepository.save(entity);
    }


    /**
     * 保存集合的所有数据
     *
     * @param entityList the entityList
     * @return List<T>
     * @author W
     * @date: 2023/10/18
     */
    @Transactional(rollbackFor = {Exception.class})
    public List<T> saveAll(List<T> entityList) {
        entityList.forEach(entity -> {
            this.initSaveData(entity);
        });
        return this.baseRepository.saveAll(entityList);
    }

    /**
     * 初始化实体数据
     *
     * @param entity the entity
     * @return void
     * @author W
     * @date: 2023/10/24
     */
    private <T extends BaseDocument> void initSaveData(T entity) {
        Serializable id = entity.getId();
        entity.initDelFlag();
        if (ObjectUtils.anyNull(id)) {
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
        } else {
            entity.setUpdateTime(LocalDateTime.now());
        }
    }

    /**
     * 获取基础查询对象
     *
     * @return R 查询对象
     * @author W
     * @date: 2023/10/18
     */
    protected R getBaseRepository() {
        return this.baseRepository;
    }

}
