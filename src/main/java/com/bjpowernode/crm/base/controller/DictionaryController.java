package com.bjpowernode.crm.base.controller;

import com.bjpowernode.crm.base.bean.DictionaryType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.base.controller
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/21 10:25
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@RestController
public class DictionaryController {

    @RequestMapping("/dictionaryCache")
    public List<DictionaryType> dictionaryCache(HttpSession session){
        ServletContext servletContext = session.getServletContext();


        List<DictionaryType> dictionaryTypes = (List<DictionaryType>) servletContext.getAttribute("dictionaryTypes");
        return dictionaryTypes;
    }
}