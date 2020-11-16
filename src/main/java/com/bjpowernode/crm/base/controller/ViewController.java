package com.bjpowernode.crm.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.base.controller
 * @Description: 跳转页面
 * @Author: Andy
 * @CreateDate: 2020/11/16 15:58
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@Controller
public class ViewController {

    //跳转所有视图    required:默认是true,每次必须给该变量传值，false可以传值，也可以不传值
    @RequestMapping({"/toView/{modalView}/{view}","/toView/{view}"})
    public String toView(
            @PathVariable(value = "modalView",required = false) String modalView,
            @PathVariable("view") String view){

        if(modalView != null){

            return modalView + File.separator + view;
        }else{
            return view;
        }
    }
}