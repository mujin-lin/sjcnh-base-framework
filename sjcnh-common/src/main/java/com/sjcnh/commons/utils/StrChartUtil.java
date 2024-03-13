package com.sjcnh.commons.utils;


import com.sjcnh.commons.constants.IntConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenglin.wu
 * @description:
 * @title: StrChartUtil
 * @projectName sjcnh-common
 * @date 2021/5/7
 * @company  sjcnh-ctu
 */
public final class StrChartUtil {
    /**
     * 默认根据驼峰切割字符串的填充
     * <p>
     * strTran -- str_Tran
     */
    private static final String DEFAULT_SEPARATOR = "_";
    /**
     * 私有化构造器
     */
    private StrChartUtil() {

    }


    /**
     * 将分割出来的字符串全大写
     * 例： param1： "strTran"  param2： "_"   result: "STR_TRAN"
     *
     * @param translationStr the translationStr 需要转换的字符串
     * @param separator      the separator 分割符
     * @return String
     * @author w
     * @date: 2021/8/10
     */
    public static String toUpperCaseTranslationStr(String translationStr,String separator){
        String str = translationStrWithSeparator(translationStr, separator);
        return str.toUpperCase();
    }

    /**
     * 将分割出来的字符串转为全小写,如果separator不传的情况下则默认为 "_"
     * <p>
     * 例： param1： "strTran"  param2： "_"   result: "str_tran"
     *
     * @param translationStr the translationStr 需要转换的字符串
     * @param separator      the separator 分割符
     * @return String
     * @author w
     * @date: 2021/8/10
     */
    public static String toLowerCaseTranslationStr(String translationStr,String separator){
        String str = translationStrWithSeparator(translationStr, separator);
        return str.toLowerCase();
    }


    /**
     * 传入驼峰的字符串，通过驼峰来辨别从哪开始分割
     * <p>
     * 例： param1： "strTran"  param2： "_"   result: "str_Tran"
     * param1： "StrTran" ; param2： "_"   result: "Str_Tran"
     * param1： "str"  param2： "_"   result: "str"
     *
     * @param translationStr 需要转换的字符串
     * @param separator      分割符
     * @return String
     * @author w
     * @date: 2021/4/27
     */
    private static String translationStrWithSeparator(String translationStr,String separator) {
        StringBuilder sb = new StringBuilder();
        char[] chars = translationStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            // 如果当前char字符为大写，则在中间添加切割
            if (aChar >= IntConstants.INT_65 && aChar <= IntConstants.INT_90 && i != 0) {
                sb.append(StringUtils.isBlank(separator) ? DEFAULT_SEPARATOR : separator);
            }
            sb.append(aChar);
        }
        return sb.toString();
    }


    /**
     * 将字符串的首字母小写
     * 例： Str   result: str
     *
     * @param str 传递就来的字符串
     * @return 返回首字母小写后的字符串
     */
    public static String lowerCaseFirst(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        char upperFirst = 'A';
        char upperLast = 'Z';
        // 首字母小写
        char[] ch = str.toCharArray();
        if (ch[0] >= upperFirst && ch[0] <= upperLast) {
            ch[0] = (char) (ch[0] + 32);
        }
        return new String(ch);
    }

    /**
     * 将字符串的首字母大写
     * 例： str   result: Str
     *
     * @param str 传递就来的字符串
     * @return 返回首字母大写后的字符串
     */
    public static String upperCaseFirst(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        char lowerFirst = 'a';
        char lowerLast = 'z';
        // 首字母小写
        char[] ch = str.toCharArray();
        if (ch[0] >= lowerFirst && ch[0] <= lowerLast) {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }
}
