package com.sjcnh.commons.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author W
 * @description:
 * @title: RegexUtils
 * @projectName sjcnh-common
 * @date 2021年06月03日
 * @company sjcnh-ctu
 */
public final class RegexUtils {
    /**
     * 私有化构造方法
     */
    private RegexUtils() {
    }

    /**
     * 身份证的正则
     */
    private static final String ID_CARD_REG = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";

    /**
     * 手机号的正则
     */
    private static final String PHONE = "^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$";
    /**
     * 邮箱的正则
     */
    private static final String E_MAIL = "^([a-zA-Z0-9]+[_|_|\\-|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";

    /**
     * 判断身份证号是否有误
     *
     * @return boolean
     * @param: idCard 身份证号
     * @author W
     * @date 2021-06-03
     */
    public static boolean isIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        return Pattern.matches(ID_CARD_REG, idCard);
    }

    /**
     * 判断手机号是否有误
     *
     * @return boolean
     * @param: phone 手机号
     * @author W
     * @date 2021-06-03
     */
    public static boolean isPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return Pattern.matches(PHONE, phone);
    }

    /**
     * 判断邮箱是否有误
     *
     * @return boolean
     * @param: eMail 邮箱号
     * @author W
     * @date 2021-06-03
     */
    public static boolean isEMail(String eMail) {
        if (StringUtils.isBlank(eMail)) {
            return false;
        }
        return Pattern.matches(E_MAIL, eMail);
    }

}
