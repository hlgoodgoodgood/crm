package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.workbench.controller
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/24 15:42
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@Controller
public class TransactionController {


    @Autowired
    private TransactionService transactionService;

    //只能给前端返回字符串{1,2,3,4}
    @RequestMapping("/workbench/transaction/queryCustomerName")
    @ResponseBody
    public List<String> queryCustomerName(String customerName){
        return transactionService.queryCustomerName(customerName);
    }

    //选中阶段，向后台发送异步请求查询阶段对应的可能性
    @RequestMapping("/workbench/transaction/queryPossibilityByStage")
    @ResponseBody
    public String queryPossibilityByStage(String stage, HttpSession session){
        Map<String,String> stage2PossibilityMap =
                (Map<String, String>) session.getServletContext().getAttribute("stage2PossibilityMap");
        return stage2PossibilityMap.get(stage);
    }
}