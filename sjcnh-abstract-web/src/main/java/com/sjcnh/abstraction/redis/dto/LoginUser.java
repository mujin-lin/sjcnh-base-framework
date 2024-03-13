package com.sjcnh.abstraction.redis.dto;


import com.sjcnh.commons.constants.IntConstants;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Y
 * @description: 登录用户表
 * @title: LoginUser
 * @projectName sjcnh-redis
 * @date 2021/4/16 15:20
 * @company sjcnh-ctu
 **/
@SuppressWarnings("unused")
public class LoginUser implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 8896484968461011677L;

    /**
     * id
     */
    private String id;
    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 账号
     */
    private String account;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 姓名
     */
    private String name;


    /**
     * 微信号
     */
    private String wxOpenId;

    /**
     * 角色
     */
    private String roles;

    /**
     * 登录认证
     */
    private String token;

    /**
     * 头像的url
     */
    private String headImgUrl;
    /**
     * 请求来源
     */
    private String requestSource;
    /**
     * 认证与否
     */
    private Boolean qualificationFlag;
    /**
     * 扩展属性
     */
    private Object extraInfo;
    /**
     * redis 中存的id
     */
    private String redisId;


    /**
     * 获取ID
     *
     * @return String the id
     * @author W
     * @date 2021-05-29
     */
    public String getRedisId() {
        return this.id + (char) IntConstants.INT_45 + this.requestSource;
    }

    public void setRedisId(String redisId) {
        this.redisId = redisId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

    public Boolean getQualificationFlag() {
        return qualificationFlag;
    }

    public void setQualificationFlag(Boolean qualificationFlag) {
        this.qualificationFlag = qualificationFlag;
    }

    public Object getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Object extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginUser loginUser = (LoginUser) o;
        return Objects.equals(id, loginUser.id) && Objects.equals(idCard, loginUser.idCard) && Objects.equals(account, loginUser.account) && Objects.equals(phone, loginUser.phone) && Objects.equals(name, loginUser.name) && Objects.equals(wxOpenId, loginUser.wxOpenId) && Objects.equals(roles, loginUser.roles) && Objects.equals(token, loginUser.token) && Objects.equals(headImgUrl, loginUser.headImgUrl) && Objects.equals(requestSource, loginUser.requestSource) && Objects.equals(qualificationFlag, loginUser.qualificationFlag) && Objects.equals(extraInfo, loginUser.extraInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idCard, account, phone, name, wxOpenId, roles, token, headImgUrl, requestSource, qualificationFlag, extraInfo);
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "id='" + id + '\'' +
                ", idCard='" + idCard + '\'' +
                ", account='" + account + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", wxOpenId='" + wxOpenId + '\'' +
                ", roles='" + roles + '\'' +
                ", token='" + token + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", requestSource='" + requestSource + '\'' +
                ", qualificationFlag=" + qualificationFlag +
                ", extraInfo=" + extraInfo +
                '}';
    }
}


