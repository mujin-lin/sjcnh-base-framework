package com.sjcnh.commons.response;


import com.sjcnh.commons.constants.IntConstants;

/**
 * @author w
 * @description:
 * @title: ResponseUtils
 * @projectName sjcnh-common
 * @date 2021/4/16
 * @company sjcnh-ctu
 */
@SuppressWarnings("unused")
public final class ResponseUtils {
    private static final String OK = "OK";

    private ResponseUtils() {
    }

    /**
     * 返回成功有数据
     *
     * @param data the data
     * @return ResponseResult<T>
     * @author W
     * @date: 2023/5/29
     */
    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = new ResponseResult<T>();
        result.setResCode(IntConstants.INT_0);
        result.setResMsg(OK);
        result.setResData(data);
        return result;
    }

    /**
     * 返回失败，有数据，有message
     *
     * @param data the data
     * @param msg  the msg
     * @return ResponseResult<T>
     * @author W
     * @date: 2023/5/29
     */
    public static <T> ResponseResult<T> success(T data, String msg) {
        ResponseResult<T> result = new ResponseResult<T>();
        result.setResCode(IntConstants.INT_0);
        result.setResMsg(msg);
        result.setResData(data);
        return result;
    }

    /**
     * 返回成功，有消息提示，无数据返回
     *
     * @param msg the msg
     * @return ResponseResult<T>
     * @author W
     * @date: 2023/5/29
     */
    public static <T> ResponseResult<T> successMsg(String msg) {
        ResponseResult<T> result = new ResponseResult<T>();
        result.setResCode(IntConstants.INT_0);
        result.setResMsg(msg);
        return result;
    }

    /**
     * 返回成功，默认消息提示，伍数据
     *
     * @return ResponseResult<T>
     * @author W
     * @date: 2023/5/29
     */
    public static <T> ResponseResult<T> success() {
        ResponseResult<T> result = new ResponseResult<T>();
        result.setResCode(IntConstants.INT_0);
        result.setResMsg(OK);
        return result;
    }

    /**
     * 返回失败，填充错误码和错误消息提示
     *
     * @param errCode the errCode
     * @param errMsg  the errMsg
     * @return ResponseResult<T>
     * @author W
     * @date: 2023/5/29
     */
    public static <T> ResponseResult<T> fail(int errCode, String errMsg) {
        ResponseResult<T> result = new ResponseResult<T>();
        result.setResCode(errCode);
        result.setResMsg(errMsg);
        return result;
    }

    /**
     * 返回失败，填充错误码和错误消息提示 详细信息
     *
     * @param errCode the errCode
     * @param errMsg  the errMsg
     * @param detailMsg  the detailMsg
     * @return ResponseResult<T>
     * @author W
     * @date: 2023/5/29
     */
    public static <T> ResponseResult<T> fail(int errCode, String errMsg,String detailMsg) {
        ResponseResult<T> result = new ResponseResult<T>();
        result.setResCode(errCode);
        result.setResMsg(errMsg);
        result.setDetailMsg(detailMsg);
        return result;
    }

}
