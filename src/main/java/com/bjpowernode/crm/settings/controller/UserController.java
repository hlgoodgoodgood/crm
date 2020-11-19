package com.bjpowernode.crm.settings.controller;

import com.bjpowernode.crm.base.constants.CrmConstants;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @PostMapping("/settings/user/login")
    public String login(User user, Model model, HttpSession session, HttpServletRequest request){
        //获取用户登录的ip
        String ip = request.getRemoteAddr();
        user.setAllowIps(ip);
        try{
            //登录成功
            user = userService.login(user);
            session.setAttribute(CrmConstants.LOGIN_USER,user);
        }catch (CrmException e){
            //登录失败，各种异常
            String mess = e.getMessage();
            model.addAttribute("mess",mess);
            //转发到登录页面，显示错误信息
            return "../../login";
        }

        return "index";
    }

    //退出系统
    @RequestMapping("/settings/user/loginOut")
    public String loginOut(HttpSession session){
        //从session移除用户
        session.removeAttribute(CrmConstants.LOGIN_USER);
        return "redirect:/login.jsp";
    }

}