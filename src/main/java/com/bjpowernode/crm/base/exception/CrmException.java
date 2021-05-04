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
 * 自定义异常:
 *  1、可以给用户返回信息
 *  2、项目上线出现Bug
 */
public class CrmException extends RuntimeException {

//*封装异常信息的枚举对象 包含业务码和错误提示信息
    private CrmExceptionEnum exceptionEnum;

    public CrmException(CrmExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMess());//将异常信息放在堆栈信息中
        this.exceptionEnum = exceptionEnum;//异常类属性的实例化
    }

    public CrmExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public void setExceptionEnum(CrmExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}