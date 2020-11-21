package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.base.bean.ResultVo;
import com.bjpowernode.crm.base.constants.CrmConstants;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.Clue;
import com.bjpowernode.crm.workbench.bean.ClueRemark;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.workbench.controller
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/21 14:24
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@Controller
public class ClueController {

    @Autowired
    private ClueService clueService;


    @RequestMapping("/workbench/clue/saveClue")
    @ResponseBody
    public ResultVo saveClue(Clue clue, HttpSession session){

        ResultVo resultVo = new ResultVo();
        try{
            User user = (User) session.getAttribute(CrmConstants.LOGIN_USER);
            clue.setCreateBy(user.getName());
            clueService.saveClue(clue);
            resultVo.setSuccess(true);
            resultVo.setMess("添加线索成功");
        }catch (CrmException e){
            resultVo.setSuccess(false);
            resultVo.setMess(e.getMessage());
        }

        return resultVo;
    }

    //根据线索id查询线索信息及其线索备注
    @RequestMapping("/workbench/clue/queryClueDetailById")
    public String queryClueDetailById(String id, Model model){

        Clue clue = clueService.queryClueDetailById(id);
        model.addAttribute("clue",clue);
        return "/clue/detail";
    }

    //更新线索备注
    @RequestMapping("/workbench/clue/updateClueRemark")
    @ResponseBody
    public ResultVo updateClueRemark(ClueRemark clueRemark,HttpSession session){
        ResultVo resultVo = new ResultVo();
        try{
            User user = (User) session.getAttribute(CrmConstants.LOGIN_USER);
            clueRemark.setEditBy(user.getName());
            clueService.updateClueRemark(clueRemark);
            resultVo.setSuccess(true);
            resultVo.setMess("更新线索备注成功");
        }catch (CrmException e){
            resultVo.setSuccess(false);
            resultVo.setMess(e.getMessage());
        }

        return resultVo;
    }

    //添加线索备注

    @RequestMapping("/workbench/clue/saveClueRemark")
    @ResponseBody
    public ResultVo saveClueRemark(ClueRemark clueRemark,HttpSession session){
        ResultVo resultVo = new ResultVo();
        try{
            User user = (User) session.getAttribute(CrmConstants.LOGIN_USER);
            clueRemark.setCreateBy(user.getName());
            clueService.saveClueRemark(clueRemark);
            resultVo.setSuccess(true);
            resultVo.setMess("添加线索备注成功");
        }catch (CrmException e){
            resultVo.setSuccess(false);
            resultVo.setMess(e.getMessage());
        }

        return resultVo;
    }
}