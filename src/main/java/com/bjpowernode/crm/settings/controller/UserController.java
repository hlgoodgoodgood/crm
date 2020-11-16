package com.bjpowernode.crm.settings.controller;

import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.settings.controller
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/16 10:36
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/queryUsers")
    @ResponseBody
    public List<User> queryUsers(){
        return userService.queryUsers();
    }

}