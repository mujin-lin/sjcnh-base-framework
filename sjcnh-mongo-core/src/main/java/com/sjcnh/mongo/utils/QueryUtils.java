package com.sjcnh.mongo.utils;

import com.sjcnh.mongo.annotations.LogicDelete;
import com.sjcnh.mongo.doc.BaseDocument;
import com.sjcnh.mongo.page.PageExtra;
import com.sjcnh.mongo.page.PageMongoDto;
import com.sjcnh.mongo.page.PageSort;
import com.sjcnh.commons.constants.ReflectConstants;
import com.sjcnh.commons.utils.StrChartUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author w
 * @description:
 * @title: QueryUtils
 * @projectName sjcnh-mongo-core
 * @date 2023/10/18
 * @company sjcnh-ctu
 */
@SuppressWarnings({"all"})
public final class QueryUtils {
    private static final Logger log = LoggerFactory.getLogger(ReflectionUtils.class);
    /**
     * 实体类中的id字段名
     */
    private static final String ID = "id";

    private QueryUtils() {
    }


    /**
     * 通过当前实体类的数据获取查询的 query对象，所有的String类型都为模糊匹配
     * 所有的基本数据类型包括其包装类都为精准匹配;
     * 并且当前所有属性的关系都为and关系
     *
     * @param entity 实体类
     * @param isSort 是否需要排序，如果
     * @return QueryWrapper<T>
     * @throws NoSuchMethodException     没找到方法异常
     * @throws IllegalAccessException    参数异常
     * @throws InvocationTargetException 反射调用异常
     * @author w
     * @date: 2021/5/17
     */
    public static <T> Query createLikeQuery(T entity, boolean isSort, Sort sort) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 判断入参
        if (isSort && ObjectUtils.anyNull(sort)) {
            throw new IllegalArgumentException("isSort equal ture but sort param is empty, please check sort param");
        } else if (ObjectUtils.anyNull(entity)) {
            return new Query();
        }
        // 创建查询的query
        Query query = new Query();
        DeclaredAndSuperClass declaredAndSuperClass = ReflectionUtils.getDeclaredAndSuperClass(entity);
        List<Field> allFields = declaredAndSuperClass.getAllFields();
        // 遍历属性
        for (Field field : allFields) {
            LogicDelete logicAnnotation = field.getAnnotation(LogicDelete.class);
            if (ObjectUtils.allNotNull(logicAnnotation)) {
                int value = logicAnnotation.value();
                String columnName = ReflectionUtils.getTranslationWithSeparatorFieldName(field);
                query.addCriteria(Criteria.where(columnName).is(value));
                continue;
            }
            // 属性类型
            String fieldType = field.getType().getSimpleName();
            boolean ignoreField = ReflectionUtils.judgementIgnoreField(field);
            if (ReflectionUtils.DATA_TYPE_LIST.contains(fieldType) && !ignoreField) {
                String getMethodName = ReflectConstants.GET_PREFIX + StrChartUtil.upperCaseFirst(field.getName());
                try {
                    Method method = declaredAndSuperClass.getDeclaredClass().getMethod(getMethodName);
                    getCriteria(entity, field, method, query);
                } catch (NoSuchMethodException e) {
                    Method method = declaredAndSuperClass.getSuperClass().getMethod(getMethodName);
                    getCriteria(entity, field, method, query);
                    log.error("child class no this method name :{}", getMethodName);
                } catch (IllegalAccessException e) {
                    log.error("method : {} not access permission ", getMethodName);
                } catch (InvocationTargetException e) {
                    log.error("method ：{} internal exception", getMethodName);
                }
            }
        }
        return isSort ? query.with(sort) : query.with(QueryConstants.DEFAULT_SORT);
    }

    /**
     * 添加分页的查询扩展时间字段
     *
     * @param query      query
     * @param pageExtras pageExtras
     * @author w
     * @date: 2021/8/11
     */
    public static void queryWithExtra(Query query, List<PageExtra> pageExtras) {
        if (ObjectUtils.isEmpty(pageExtras)) {
            return;
        }

        // 获取扩展字段，主要针对时间的between
        for (PageExtra extra : pageExtras) {
            if (StringUtils.isBlank(extra.getColumnName())) {
                continue;
            }
            if (ObjectUtils.anyNull(extra.getBeginTime())) {
                query.addCriteria(Criteria.where(extra.getColumnName()).lte(extra.getEndTime()));
            } else if (ObjectUtils.anyNull(extra.getEndTime())) {
                query.addCriteria(Criteria.where(extra.getColumnName()).gte(extra.getBeginTime()));
            } else if (ObjectUtils.allNotNull(extra.getEndTime(), extra.getBeginTime())) {
                Criteria between = Criteria.where(extra.getColumnName()).lte(extra.getEndTime()).gte(extra.getBeginTime());
                query.addCriteria(between);
            }
        }
    }

    /**
     * 获取分页查询的query，添加分页的最后两个条件skip和limit
     *
     * @param pageSearch the pageSearch
     * @author w
     * @date: 2021/8/11
     */
    public static <T extends BaseDocument> void pagingQuery(PageMongoDto<T> pageSearch, Query query) {
        PageRequest page = pageSearch.getPageRequest();
        int skipNumber = (page.getPageNumber() - 1) * page.getPageSize();
        query.skip(skipNumber).limit(page.getPageSize());
    }

    /**
     * 创建排序sort类
     *
     * @param pageSorts the pageSorts
     * @return Sort
     * @author W
     * @date: 2023/11/17
     */
    public static Sort createSort(List<PageSort> pageSorts) {
        // 传入为空则返回不排序
        if (CollectionUtils.isEmpty(pageSorts)) {
            return Sort.unsorted();
        }
        List<Sort.Order> sortOrders = new ArrayList<>();
        for (PageSort pageSort : pageSorts) {
            if (StringUtils.isBlank(pageSort.getSortBy()) || QueryConstants.ASC.equalsIgnoreCase(pageSort.getSortBy())) {
                sortOrders.add(Sort.Order.asc(pageSort.getColumnName()));
            } else {
                sortOrders.add(Sort.Order.desc(pageSort.getColumnName()));
            }
        }
        return Sort.by(sortOrders);

    }

    /**
     * 构造Criteria
     *
     * @param entity 实体类
     * @param field  属性
     * @param method get 方法
     * @param query  查询mongo的query类
     * @author w
     * @date: 2021/5/17
     */
    private static <T> void getCriteria(T entity, Field field, Method method, Query query) throws IllegalAccessException, InvocationTargetException {
        Object invoke = method.invoke(entity);
        if (ObjectUtils.anyNull(invoke)) {
            return;
        }
        Transient annotation = field.getAnnotation(Transient.class);
        if (ObjectUtils.anyNotNull(annotation)) {
            return;
        }
        String columnName = ReflectionUtils.getTranslationWithSeparatorFieldName(field);
        Criteria criteria = null;
        // 构造查询条件
        if (ReflectConstants.STRING_SIMPLE_NAME.equals(field.getType().getSimpleName()) && ObjectUtils.isNotEmpty(invoke)) {
            if (ID.equals(columnName)) {
                criteria = Criteria.where(columnName).is(invoke);
            } else {
                criteria = Criteria.where(columnName).regex(".*" + invoke + ".*");
            }
        } else if (ObjectUtils.isNotEmpty(invoke)) {
            criteria = Criteria.where(columnName).is(invoke);
        }
        if (null != criteria) {
            query.addCriteria(criteria);
        }
    }


    /**
     * @author w
     * @description: 设计表中的字段信息
     * @title: WrapperConstants
     * @projectName sjcnh-mongo-core
     * @date 2021/4/29
     * @company sjcnh-ctu
     */
    @SuppressWarnings("unused")
    public static final class QueryConstants {

        /**
         * 数据库字段--创建
         */
        public static final String CREATE_TIME = "create_time";
        /**
         * 大文件桶上传时间
         */
        public static final String GRID_FILE_UPLOAD_DATE = "uploadDate";


        /**
         * 前端传送字段---开始时间
         */
        public static final String BEGIN_TIME = "beginTime";

        /**
         * 前端传送字段---结束时间
         */
        public static final String END_TIME = "endTime";

        /**
         * 默认排序order
         */
        public static final Sort.Order DEFAULT_ORDER = Sort.Order.desc(CREATE_TIME);

        /**
         * 默认的排序Sort
         */
        public static final Sort DEFAULT_SORT = Sort.by(DEFAULT_ORDER);
        /**
         * 默认的大文件排序规则
         */
        public static final Sort DEFAULT_GRID_SORT = Sort.by(Sort.Order.desc(GRID_FILE_UPLOAD_DATE));

        /**
         * 倒序排列的字符串
         */
        public static final String DESC = "DESC";

        /**
         * 正序排列的字符串
         */
        public static final String ASC = "ASC";
    }

}
