package com.sjcnh.commons.exception;


import com.sjcnh.commons.enums.ErrCodeEnum;

/**
 * @author w
 * @description:
 * @title: BusinessExceprion
 * @projectName sjcnh-common
 * @date 2021/4/16
 * @company  sjcnh-ctu
 */
@SuppressWarnings("unused")
public class BusinessException extends Exception{

    /**
     * 类型：long
     */
    private static final long serialVersionUID = -3804995326646218863L;

    /**
     * 错误代码
     */
    private int errCode;

    /**
     * 错误信息
     */
    private String errMsg;

    /**
     * @return the errCode
     */
    public int getErrCode() {
        return errCode;
    }

    /**
     * @param errCode the errCode to set
     */
    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    /**
     * @return the errMsg
     */
    public String getErrMsg() {
        return errMsg;
    }

    /**
     * @param errMsg the errMsg to set
     */
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public BusinessException(int errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BusinessException(String errMsg) {
        super(errMsg);
        this.errCode = ErrCodeEnum.BUSINESS.getCode();
        this.errMsg = errMsg;
    }


}
