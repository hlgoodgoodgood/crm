package com.bjpowernode.crm.base.cache;

import com.bjpowernode.crm.base.bean.DictionaryType;
import com.bjpowernode.crm.settings.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.util.List;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.base.cache
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/20 11:35
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@Component
public class CrmCache {


    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ServletContext servletContext;


    //在Bean对象初始化的时候执行对应的方法
    @PostConstruct
    public void cache(){

        List<DictionaryType> dictionaryTypes = dictionaryService.queryDictionary();

        //将dictionaryTypes放入到ServletContext中
        servletContext.setAttribute("dictionaryTypes",dictionaryTypes);

    }



}