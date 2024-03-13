package com.sjcnh.apiclient.test;

import java.io.Serializable;

/**
 * @author chenglin.wu
 * @description:
 * @title: ResponseTest
 * @projectName demo
 * @date 2021/8/12
 * @company WHY
 */
public class ResponseTest implements Serializable {

    /**
     * resCode : 0
     * resMsg : OK
     * detailMsg : null
     * resData : {"transToken":"af34512c68074ba39fa44dd11b69dfb7","transEncryptKey":"emzZgeNftUSPlyVp"}
     */

    private int resCode;
    private String resMsg;
    private Object detailMsg;
    private ResDataBean resData;

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public Object getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(Object detailMsg) {
        this.detailMsg = detailMsg;
    }

    public ResDataBean getResData() {
        return resData;
    }

    public void setResData(ResDataBean resData) {
        this.resData = resData;
    }

    @Override
    public String toString() {
        return "ResponseTest{" +
                "resCode=" + resCode +
                ", resMsg='" + resMsg + '\'' +
                ", detailMsg=" + detailMsg +
                ", resData=" + resData +
                '}';
    }

    public static class ResDataBean implements Serializable {
        /**
         * transToken : af34512c68074ba39fa44dd11b69dfb7
         * transEncryptKey : emzZgeNftUSPlyVp
         */

        private String transToken;
        private String transEncryptKey;

        public String getTransToken() {
            return transToken;
        }

        public void setTransToken(String transToken) {
            this.transToken = transToken;
        }

        public String getTransEncryptKey() {
            return transEncryptKey;
        }

        public void setTransEncryptKey(String transEncryptKey) {
            this.transEncryptKey = transEncryptKey;
        }

        @Override
        public String toString() {
            return "ResDataBean{" +
                    "transToken='" + transToken + '\'' +
                    ", transEncryptKey='" + transEncryptKey + '\'' +
                    '}';
        }
    }
}
