package com.sjcnh.mongo.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sjcnh.abstraction.web.abstraction.BaseAbstractController;
import com.sjcnh.mongo.doc.BaseDocument;
import com.sjcnh.mongo.page.PageMongoDto;
import com.sjcnh.mongo.service.BaseMongoService;
import com.sjcnh.mongo.utils.ReflectionUtils;
import com.sjcnh.commons.response.ResponseResult;
import com.sjcnh.commons.response.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * @author w
 * @description:
 * @title: BaseMongoController
 * @projectName sjcnh-mongo-core
 * @date 2023/10/18
 * @company sjcnh-ctu
 */
@SuppressWarnings({"all"})
public class BaseMongoController<S extends BaseMongoService<T, R>, T extends BaseDocument, R extends MongoRepository<T, Serializable>> extends BaseAbstractController<T> {
    /**
     * 业务处理类
     */
    @Autowired
    protected S baseService;

    /**
     * request
     **/
    @Autowired
    private HttpServletRequest request;

    @JsonIgnore
    @Transient
    private final Class<T> docClass;

    public BaseMongoController() {
        this.docClass = ReflectionUtils.getSuperClassGenericType(getClass(), 1);
    }

    /**
     * 获取当前请求的httpServlet对象
     *
     * @return HttpServletRequest
     * @author W
     * @date: 2023/10/18
     */
    public HttpServletRequest getHttpServletRequest() {
        return this.request;
    }

    /**
     * 获取业务处理层的实例
     *
     * @return S 业务处理层的类
     * @author W
     * @date: 2023/10/18
     */
    public S getBaseService() {
        return baseService;
    }


    /**
     * 插入数据
     *
     * @param entity 实体类数据
     * @return ResponseResult<T>
     * @author W
     * @date: 2023/11/10
     */
    @Override
    @PostMapping("/insert")
    public ResponseResult<T> insert(@RequestBody T entity) {
        T save = this.getBaseService().save(entity);
        return ResponseUtils.success(save);
    }

    /**
     * 通过ID删除数据
     *
     * @return ResponseResult<String>
     * @param: id 实体类的id
     * @author W
     * @date: 2023/10/18
     */
    @Override
    @DeleteMapping("/deleteById/{id}")
    public <ID extends Serializable> ResponseResult<String> delete(ID entityId) {
        this.getBaseService().deleteById(entityId);
        return ResponseUtils.success();
    }

    /**
     * 通过ID删除数据
     *
     * @return ResponseResult<String>
     * @param: id 实体类的id
     * @author W
     * @date: 2023/10/18
     */
    @DeleteMapping("/deleteByIds")
    public <ID extends Serializable> ResponseResult<String> deleteListId(@RequestBody List<ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return ResponseUtils.success();
        }
        this.getBaseService().deleteByIds(ids);
        return ResponseUtils.success();
    }

    /**
     * 更新数据
     *
     * @param entity the entity
     * @return ResponseResult<T>
     * @author W
     * @date: 2023/11/10
     */
    @Override
    @PostMapping("/deleteById")
    public ResponseResult<T> update(@RequestBody T entity) {
        T save = this.getBaseService().save(entity);
        return ResponseUtils.success(save);
    }

    /**
     * 分页查询
     *
     * @param pageMongoDto the pageMongoDto
     * @return Page<T> 分页对象
     * @throws Exception 异常信息
     * @author W
     * @date: 2023/10/19
     */
    @PostMapping("/findPage")
    public Page<T> findPage(@RequestBody PageMongoDto<T> pageMongoDto) throws Exception {
        pageMongoDto.setSearchClass(this.docClass);
        if (!CollectionUtils.isEmpty(pageMongoDto.getPageExtras())) {
            return this.baseService.findPage(pageMongoDto);
        }
        return this.baseService.findPage(pageMongoDto.getSearchExample(), pageMongoDto.getPageRequest());
    }


}
