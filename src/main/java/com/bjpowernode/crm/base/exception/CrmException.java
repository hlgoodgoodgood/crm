package com.bjpowernode.crm.base.exception;

import com.bjpowernode.crm.base.constants.CrmExceptionEnum;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.base.exception
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/16 14:27
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
public class CrmException extends RuntimeException {


    private CrmExceptionEnum exceptionEnum;

    public CrmException(CrmExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMess());//将异常信息放在堆栈信息中
        this.exceptionEnum = exceptionEnum;
    }

    public CrmExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public void setExceptionEnum(CrmExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}