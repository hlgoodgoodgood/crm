package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.base.bean.PaginationVo;
import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.constants.CrmConstants;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.ActivityQueryVo;
import com.bjpowernode.crm.workbench.bean.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.workbench.controller
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/17 9:34
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@Controller
public class ActivityController {


    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;


    @RequestMapping("/workbench/activity/queryAllActivity")
    public @ResponseBody PaginationVo queryAllActivity(
            @RequestParam(defaultValue = "1",required = false) int page,
            @RequestParam(defaultValue = "2",required = false) int pageSize,
            ActivityQueryVo queryVo){

        //开启分页功能 limit a,b
        PageHelper.startPage(page, pageSize);

        List<Map<String,String>> activities = activityService.queryAllActivity(queryVo);


        PageInfo<Map<String,String>> pageInfo = new PageInfo<>(activities);

        //把前台分页插件需要的数据都封装到paginationVo
        PaginationVo paginationVo = new PaginationVo(pageInfo);

        return paginationVo;
    }

    //异步查询所有用户
    @RequestMapping("/workbench/activity/queryAllUsers")
    @ResponseBody
    public List<User> queryAllUsers(){
        return userService.queryAllUsers();
    }

    //保存市场活动
    @RequestMapping("/workbench/activity/saveActivity")
    @ResponseBody
    public ResultVo saveActivity(Activity activity, HttpSession session){
        //获取登录用户
        User user = (User) session.getAttribute(CrmConstants.LOGIN_USER);
        //创建者
        activity.setCreateBy(user.getName());
        //编辑者
        activity.setEditBy(user.getName());

        ResultVo resultVo = new ResultVo();
        try {
            activityService.saveActivity(activity);
            resultVo.setSuccess(true);
            resultVo.setMess("添加成功");
        }catch (CrmException e){
            resultVo.setSuccess(false);
            //将异常信息添加到resultVo中
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }

    //根据主键异步查询市场活动
    @RequestMapping("/workbench/activity/queryActivityById")
    @ResponseBody
    public Activity queryActivityById(String id){
       Activity activity = activityService.queryActivityById(id);
       return activity;
    }

    //异步更新市场活动
    @RequestMapping("/workbench/activity/updateActivity")
    @ResponseBody
    public ResultVo updateActivity(Activity activity,HttpSession session){
        //获取当前登录用户
        User user = (User) session.getAttribute(CrmConstants.LOGIN_USER);
        activity.setEditBy(user.getName());

        ResultVo resultVo = new ResultVo();
        try {
            activityService.updateActivity(activity);
            resultVo.setSuccess(true);
            resultVo.setMess("更新成功");
        }catch (CrmException e){
            resultVo.setSuccess(false);
            //将异常信息添加到resultVo中
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }

    //根据主键删除市场活动
    @RequestMapping("/workbench/activity/deleteActivity")
    @ResponseBody
    public ResultVo deleteActivity(String id){

        ResultVo resultVo = new ResultVo();
        try {
            activityService.deleteActivity(id);
            resultVo.setSuccess(true);
            resultVo.setMess("删除成功");
        }catch (CrmException e){
            resultVo.setSuccess(false);
            //将异常信息添加到resultVo中
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }

    //根据主键删除市场活动
    @RequestMapping("/workbench/activity/deleteActivityByDetail")
    public String deleteActivityByDetail(String id){
        try{
            //删除成功
            activityService.deleteActivity(id);
        }catch (CrmException e){
            e.printStackTrace();
        }
       return "redirect:/toView/activity/index";
    }

    //根据主键查询市场活动
    @RequestMapping("/workbench/activity/queryActivityDetailById")
    public String queryActivityDetailById(String id, Model model){
        Activity activity = activityService.queryActivityDetailById(id);
        model.addAttribute("activity",activity);
        return "forward:/toView/activity/detail";
    }

    //异步修改备注内容
    @RequestMapping("/workbench/activity/updateActivityRemark")
    @ResponseBody
    public ResultVo updateActivityRemark(ActivityRemark activityRemark){

        ResultVo resultVo = new ResultVo();
        try {
            activityService.updateActivityRemark(activityRemark);
            resultVo.setSuccess(true);
            resultVo.setMess("修改市场活动备注成功");
        }catch (CrmException e){
            resultVo.setSuccess(false);
            //将异常信息添加到resultVo中
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }

    //异步删除备注内容
    @RequestMapping("/workbench/activity/deleteActivityRemark")
    @ResponseBody
    public ResultVo deleteActivityRemark(String id){

        ResultVo resultVo = new ResultVo();
        try {
            activityService.deleteActivityRemark(id);
            resultVo.setSuccess(true);
            resultVo.setMess("删除市场活动备注成功");
        }catch (CrmException e){
            resultVo.setSuccess(false);
            //将异常信息添加到resultVo中
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }

    //添加市场活动备注
    @RequestMapping("/workbench/activity/saveActivityRemark")
    @ResponseBody
    public ResultVo saveActivityRemark(ActivityRemark activityRemark,HttpSession session){
        ResultVo resultVo = new ResultVo();
        try {
            //获取创建人
            User user = (User) session.getAttribute(CrmConstants.LOGIN_USER);
            activityRemark.setCreateBy(user.getName());
            activityService.saveActivityRemark(activityRemark);
            resultVo.setSuccess(true);
            resultVo.setMess("添加市场活动备注成功");
        }catch (CrmException e){
            resultVo.setSuccess(false);
            //将异常信息添加到resultVo中
            resultVo.setMess(e.getMessage());
        }
        return resultVo;
    }



}