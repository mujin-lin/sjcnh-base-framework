package com.sjcnh.commons.constants;


import javax.servlet.http.HttpServletRequest;

/**
 * @author w
 * @description:
 * @title: ReflectMethodPrefix
 * @projectName base_framework
 * @date 2021/4/16 13:18
 * @company sjcnh-ctu
 */
public final class ReflectConstants {

    /**
     * 私有化常量类的构造器
     */
    private ReflectConstants() {
    }

    /**
     * set方法的前缀
     */
    public static final String SET_PREFIX = "set";

    /**
     * get方法的前缀
     */
    public static final String GET_PREFIX = "get";

    /**
     * boolean方法获取值的前缀
     */
    public static final String IS_PREFIX = "is";
    // ---------------------------------------------------
    //    基本数据类型和其包装类的字节码对象可能拿到的simpleName
    // ---------------------------------------------------
    // 基本数据类型
    /**
     * int
     */
    public static final String INT = "int";
    /**
     * char
     */
    public static final String CHAR = "char";
    /**
     * long
     */
    public static final String LONG = "long";
    /**
     * double
     */
    public static final String DOUBLE = "double";
    /**
     * float
     */
    public static final String FLOAT = "float";
    /**
     * boolean
     */
    public static final String BOOLEAN = "boolean";
    /**
     * byte
     */
    public static final String BYTE = "byte";
    /**
     * short
     */
    public static final String SHORT = "short";
    // 包装类
    /**
     * Integer
     */
    public static final String PACKING_INT = "Integer";
    /**
     * Character
     */
    public static final String PACKING_CHAR = "Character";
    /**
     * Long
     */
    public static final String PACKING_LONG = "Long";
    /**
     * Double
     */
    public static final String PACKING_DOUBLE = "Double";
    /**
     * Float
     */
    public static final String PACKING_FLOAT = "Float";
    /**
     * Byte
     */
    public static final String PACKING_BYTE = "Byte";
    /**
     * Boolean
     */
    public static final String PACKING_BOOLEAN = "Boolean";
    /**
     * Short
     */
    public static final String PACKING_SHORT = "Short";
    /**
     * String
     */
    public static final String STRING_SIMPLE_NAME = "String";
    /**
     * BaseEntity的方法 getParams名称
     */
    public static final String GET_PARAMS = "getParams";
    // ---------------------------------------------------
    //               web 请求方式的参数和全类名
    // ---------------------------------------------------

    /**
     * tomcat 中HttpServletRequest中获取header的方法,<p/>
     * 详情{@link HttpServletRequest#getHeader(String)}
     */
    public static final String HTTP_SERVLET_REQUEST_GET_HEADER = "getHeader";
    /**
     * tomcat 中HttpServletRequest中获取header的方法,<p/>
     * 详情{@link HttpServletRequest#getHeaderNames()}
     */
    public static final String HTTP_SERVLET_REQUEST_GET_HEADER_NAMES = "getHeaderNames";
    /**
     * tomcat 中ServerHttpRequest中获取header的方法,<p/>
     * 详情{@link ServerHttpRequest#getHeaders()}
     */
    public static final String SERVER_REQUEST_GET_HEADERS = "getHeaders";

    /**
     * HttpSercletRequest 类的包名
     */
    public static final String HTTP_SERVLET_REQUEST_PACKAGE = "javax.servlet.http";
    /**
     * ServerHttpRequest 包名
     */
    public static final String REACTIVE_REQUEST_PACKAGE = "org.springframework.http.server";

    // ---------------------------------------------------
    //    扩展字段分页查询的区间判断常量
    // ---------------------------------------------------
    /**
     * 全闭区间
     */
    public static final String CLOSE_ALL = "closeAll";
    /**
     * 左开右闭
     */
    public static final String OPEN_LEFT_AND_CLOSE_RIGHT = "openLeftAndCloseRight";
    /**
     * 左闭右开
     */
    public static final String CLOSE_LEFT_AND_OPEN_RIGHT = "closeLeftAndOpenRight";
    /**
     * 全开
     */
    public static final String OPEN_ALL = "openAll";
}
