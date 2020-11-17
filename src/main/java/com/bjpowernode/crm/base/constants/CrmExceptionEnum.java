package com.bjpowernode.crm.base.constants;

/*
* 定义异常的类型
*
* 001:用户登录业务/异常
* 002:
* */

public enum CrmExceptionEnum {

    LOGIN_ACCOUNT_ERROR("001","用户名或密码错误"),
    LOGIN_ACCOUNT_EXPIRE("001","账户已失效"),
    LOGIN_ACCOUNT_FORBID("001","账户被禁用"),
    LOGIN_ACCOUNT_IP("001","不允许的IP"),

    ACTIVITY_SAVE("002","添加市场活动失败");

    private String code;//业务状态码 001:用户登录 002:交易模块

    private String mess;

    CrmExceptionEnum(String code, String mess) {
        this.code = code;
        this.mess = mess;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }
}
