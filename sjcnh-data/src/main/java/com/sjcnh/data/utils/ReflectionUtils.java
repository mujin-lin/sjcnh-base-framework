package com.sjcnh.data.utils;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sjcnh.commons.constants.IntConstants;
import com.sjcnh.commons.constants.ReflectConstants;
import com.sjcnh.data.dto.PageExtra;
import com.sjcnh.data.entity.BaseEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author chenglin.wu
 * @description:
 * @projectName why-data
 * @title: ReflectionUtils
 * @date 2021/4/16 10:57
 * @company sjcnh-ctu
 */
@SuppressWarnings("unused")
public final class ReflectionUtils {
    private static final Logger log = LoggerFactory.getLogger(ReflectionUtils.class);
    /**
     * 初始化基本数据类型和包装类型的simpleName
     */
    private static final List<String> DATA_TYPE_LIST = new ArrayList<>(IntConstants.INT_17);

    static {
        DATA_TYPE_LIST.add(ReflectConstants.BYTE);
        DATA_TYPE_LIST.add(ReflectConstants.INT);
        DATA_TYPE_LIST.add(ReflectConstants.CHAR);
        DATA_TYPE_LIST.add(ReflectConstants.LONG);
        DATA_TYPE_LIST.add(ReflectConstants.DOUBLE);
        DATA_TYPE_LIST.add(ReflectConstants.BOOLEAN);
        DATA_TYPE_LIST.add(ReflectConstants.FLOAT);
        DATA_TYPE_LIST.add(ReflectConstants.SHORT);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_BYTE);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_INT);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_CHAR);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_LONG);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_DOUBLE);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_FLOAT);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_SHORT);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_BOOLEAN);
        DATA_TYPE_LIST.add(ReflectConstants.STRING_SIMPLE_NAME);
    }

    /**
     * 私有化
     */
    private ReflectionUtils() {
    }

    /**
     * 获取泛型的实际类型
     *
     * @param clazz 泛型的class对象
     * @return Class 返回实际的Class对象
     * @author chenglin.wu
     * @date: 2021/4/16
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenericType(Class clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * 获取set方法映射表
     *
     * @param targetMethods 反射拿到的所有的方法
     * @return HashMap<String, Method> 返回set方法的映射表
     * @author chenglin.wu
     * @date: 2021/4/16
     */
    public static HashMap<String, Method> getMethodMap4Set(Method[] targetMethods) {
        return ReflectionUtils.getSetMethodMapping(targetMethods);
    }


    /**
     * 通过当前实体类的数据获取查询的queryWrapper
     *
     * @param entity 实体类
     * @return QueryWrapper<T>
     * @throws NoSuchMethodException,InvocationTargetException,IllegalAccessException 反射中调用方法等的异常
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    public static <T extends BaseEntity> QueryWrapper<T> createPageWrapper2Field(T entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (ObjectUtils.anyNull(entity)) {
            return new QueryWrapper<>();
        }
        DeclaredAndSuperClass declaredAndSuperClass = getDeclaredAndSuperClass(entity);
        List<Field> allFields = declaredAndSuperClass.getAllFields();
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        // 遍历属性
        for (Field field : allFields) {
            // 属性类型
            String fieldType = field.getType().getSimpleName();
            boolean notIgnore = judgementIgnoreField(field);
            if (DATA_TYPE_LIST.contains(fieldType) && notIgnore) {
                String getMethodName = ReflectConstants.GET_PREFIX + upperCaseFirst(field.getName());
                try {
                    Method method = declaredAndSuperClass.getDeclaredClass().getMethod(getMethodName);
                    getWrapper(entity, field, method, wrapper);
                } catch (NoSuchMethodException e) {
                    Method method = declaredAndSuperClass.getSuperClass().getMethod(getMethodName);
                    getWrapper(entity, field, method, wrapper);
                    log.error("child class no this method name :{}", getMethodName);
                } catch (IllegalAccessException e) {
                    log.error("method : {} not access permission ", getMethodName);
                } catch (InvocationTargetException e) {
                    log.error("method ：{} internal exception", getMethodName);
                }
            }
        }
        return wrapper;
    }

    /**
     * 通过当前实体类的数据获取查询的queryWrapper
     *
     * @param entity 守实体类
     * @return QueryWrapper<T>
     * @throws NoSuchMethodException,InvocationTargetException,IllegalAccessException 反射中调用方法等的异常
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    public static <T extends BaseEntity> QueryWrapper<T> createPageWrapper2Field(T entity, List<PageExtra> extras) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        QueryWrapper<T> wrapper = createPageWrapper2Field(entity);
        if (!CollectionUtils.isEmpty(extras)) {
            for (PageExtra extra : extras) {
                String section = extra.judgmentSection();
                String columnName = extra.getColumnName();
                String dbField = getDbField(columnName);
                switch (section) {
                    case ReflectConstants.OPEN_ALL:
                        wrapper.between(dbField, extra.getBeginValue(), extra.getEndValue());
                        break;
                    case ReflectConstants.CLOSE_ALL:
                        wrapper.gt(columnName, extra.getBeginValue()).lt(columnName, extra.getEndValue());
                        break;
                    case ReflectConstants.CLOSE_LEFT_AND_OPEN_RIGHT:
                        wrapper.ge(columnName, extra.getBeginValue()).lt(columnName, extra.getEndValue());
                        break;
                    case ReflectConstants.OPEN_LEFT_AND_CLOSE_RIGHT:
                        wrapper.gt(columnName, extra.getBeginValue()).le(columnName, extra.getEndValue());
                        break;
                    default:
                        break;
                }

            }
        }
        return wrapper;
    }


    /**
     * 获取属性对应的数据库列名
     *
     * @param field     列名
     * @param fieldType 属性
     * @return String
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    private static String getTableFieldAnnotationValue(String field, Field fieldType) {
        TableField annotation = fieldType.getAnnotation(TableField.class);
        if (ObjectUtils.allNotNull(annotation) && StringUtils.isNotBlank(annotation.value())) {
            field = annotation.value();
        } else {
            field = getDbField(field);
        }
        return field;
    }

    /**
     * 获取属性对应的数据库列名
     *
     * @param field 属性
     * @return String
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    private static String getTableFieldAnnotationValue(Field field) {
        TableField annotation = field.getAnnotation(TableField.class);
        if (ObjectUtils.allNotNull(annotation) && StringUtils.isNotBlank(annotation.value())) {
            return annotation.value();
        } else {
            return getDbField(field.getName());
        }
    }

    /**
     * 判断此属性是否为忽略属性
     *
     * @param field 属性
     * @return boolean false 为忽略字段 true 则不忽略
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    private static boolean judgementIgnoreField(Field field) {
        // 获取属性中所有的注解
        TableField tableField = field.getAnnotation(TableField.class);
        if (ObjectUtils.anyNull(tableField)) {
            int modifiers = field.getModifiers();
            // 如果为staic 或者final 修饰的字段则直接忽略
            return !(Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers));
        }
        return tableField.exist();
    }

    /**
     * 将字符串的首字母小写
     *
     * @param str 传递就来的字符串
     * @return 返回首字母小写后的字符串
     */
    public static String lowerCaseFirst(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        // 首字母小写
        char[] ch = str.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] = (char) (ch[0] + 32);
        }
        return new String(ch);
    }

    /**
     * 将字符串的首字母大写
     *
     * @param str 传递就来的字符串
     * @return 返回首字母大写后的字符串
     */
    public static String upperCaseFirst(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        // 首字母小写
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 构造wrapper
     *
     * @param wrapper   new 出来的wrapper
     * @param fieldName 字段名
     * @param invoke    调用get方法获取出来的值
     * @param field     数据库字段名
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    private static <T extends BaseEntity> void getWrapper(QueryWrapper<T> wrapper, String fieldName, Object invoke, String field) {
        // 构造查询条件
        if (ReflectConstants.STRING_SIMPLE_NAME.equals(fieldName) && ObjectUtils.allNotNull(invoke)) {
            wrapper.like(field, invoke);
        } else if (ObjectUtils.isNotEmpty(invoke)) {
            wrapper.eq(field, invoke);
        }
    }

    /**
     * 构造wrapper
     *
     * @param entity  实体类
     * @param field   属性
     * @param method  get 方法
     * @param wrapper new 出来的wrapper
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    private static <T extends BaseEntity> void getWrapper(T entity, Field field, Method method, QueryWrapper<T> wrapper) throws IllegalAccessException, InvocationTargetException {
        Object invoke = method.invoke(entity);
        if (ObjectUtils.anyNull(invoke)) {
            return;
        }
        String columnName = getTableFieldAnnotationValue(field);
        // 构造查询条件
        if (ReflectConstants.STRING_SIMPLE_NAME.equals(field.getType().getSimpleName()) && ObjectUtils.isNotEmpty(invoke)) {
            wrapper.like(columnName, invoke);
        } else if (ObjectUtils.isNotEmpty(invoke)) {
            wrapper.eq(columnName, invoke);
        }
    }

    /**
     * @param targetMethods 所有的方法数组
     * @return HashMap<String, Method>
     * @author chenglin.wu
     * @date: 2021/4/16
     */
    private static HashMap<String, Method> getSetMethodMapping(Method[] targetMethods) {
        HashMap<String, Method> myMethods = new HashMap<>();
        if (ArrayUtils.isEmpty(targetMethods)) {
            return myMethods;
        }
        // 将所有set方法加入到映射表中
        for (Method method : targetMethods) {
            if (method.getName().startsWith(ReflectConstants.SET_PREFIX)) {
                myMethods.put(method.getName(), method);
            }
        }
        return myMethods;
    }

    /**
     * 获取泛型的实际类型
     *
     * @param clazz 泛型的class对象
     * @param index 下标
     * @return Class
     * @author chenglin.wu
     * @date: 2021/4/16
     */
    @SuppressWarnings({"all"})
    private static Class getSuperClassGenericType(Class clazz, int index) {
        // 获取当前class字节码对象的直接父类包括当前类的泛型
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        // 获取泛型的class字节码类型
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        // 如果下标超过了,就直接返回Object的字节码对象
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        // 如果获取出来的不是字节码对象,也是返回Object的字节码对象
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        // 将获取到的字节码对象强转到指定类型
        return (Class) params[index];
    }

    /**
     * 通过实体获取实体的父类Class对象和自己的Class文件对象
     *
     * @param entity 实体类
     * @return DeclaredAndSuperClass
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    private static <T> DeclaredAndSuperClass getDeclaredAndSuperClass(T entity) {
        if (ObjectUtils.isEmpty(entity) || ObjectUtils.isEmpty(entity)) {
            throw new IllegalArgumentException("null");
        }
        return new DeclaredAndSuperClass(entity.getClass());
    }


    /**
     * 获取数据库的字段名 转换成全大写
     *
     * @param fieldStr 当前的数据库字段名
     * @return String
     * @author chenglin.wu
     * @date: 2021/4/27
     */
    private static String getDbField(String fieldStr) {
        StringBuilder sb = new StringBuilder();
        char[] chars = fieldStr.toCharArray();
        for (char aChar : chars) {
            if (aChar >= IntConstants.INT_65 && aChar <= IntConstants.INT_90) {
                sb.append('_');
            }
            sb.append(aChar);
        }
        String tableField = sb.toString();
        return tableField.toUpperCase();
    }

    private static class DeclaredAndSuperClass {

        private final Class<?> declaredClass;

        private final Class<?> superClass;

        public DeclaredAndSuperClass(Class<?> declaredClass) {
            this.declaredClass = declaredClass;
            this.superClass = declaredClass.getSuperclass();
        }

        public Class<?> getDeclaredClass() {
            return declaredClass;
        }

        public Class<?> getSuperClass() {
            return superClass;
        }

        /**
         * 获取本类和父类所有的属性
         *
         * @return List<Field>
         * @author chenglin.wu
         * @date: 2021/5/17
         */
        private List<Field> getAllFields() {
            List<Field> fields = new ArrayList<>();
            Field[] declaredFields = declaredClass.getDeclaredFields();
            Field[] superFields = superClass.getDeclaredFields();
            fields.addAll(Arrays.asList(superFields));
            fields.addAll(Arrays.asList(declaredFields));
            return fields;
        }

        /**
         * 获取本类的所有属性
         *
         * @return List<Field>
         * @author chenglin.wu
         * @date: 2021/5/17
         */
        private List<Field> getDeclaredFields() {
            Field[] declaredFields = declaredClass.getDeclaredFields();
            return new ArrayList<>(Arrays.asList(declaredFields));
        }

        /**
         * 获取父类的所有属性
         *
         * @return List<Field>
         * @author chenglin.wu
         * @date: 2021/5/17
         */
        private List<Field> getSuperFields() {
            Field[] declaredFields = superClass.getDeclaredFields();
            return new ArrayList<>(Arrays.asList(declaredFields));
        }
    }

}
