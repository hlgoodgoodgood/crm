package com.bjpowernode.crm.base.bean;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.base.bean
 * @Description: 专门给客户反馈消息实体类
 * @Author: Andy
 * @CreateDate: 2020/11/17 15:07
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
public class ResultVo {

    private boolean success;

    private String mess;

    @Override
    public String toString() {
        return "ResultVo{" +
                "success=" + success +
                ", mess='" + mess + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }
}