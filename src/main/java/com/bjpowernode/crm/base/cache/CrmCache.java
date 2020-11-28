package com.bjpowernode.crm.base.cache;

import com.bjpowernode.crm.base.bean.DictionaryType;
import com.bjpowernode.crm.settings.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.util.*;

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


   /* @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ServletContext servletContext;


    //在Bean对象初始化的时候执行对应的方法
    @PostConstruct
    public void cache(){

        //数据字典数据缓冲
        List<DictionaryType> dictionaryTypes = dictionaryService.queryDictionary();

        //将dictionaryTypes放入到ServletContext中
        servletContext.setAttribute("dictionaryTypes",dictionaryTypes);

        //读取Stage2Possibility.properties    包名.属性文件(扩展名不能要)
        ResourceBundle resourceBundle =
                ResourceBundle.getBundle("mybatis.Stage2Possibility");
        //获取所有key
        Enumeration<String> keys = resourceBundle.getKeys();
        //把所有阶段和可能性的数据放在map中
        Map<String,String> map = new TreeMap<>();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();//取出本次循环的数据
            String value = resourceBundle.getString(key);
            map.put(key,value);
        }
        //将map放在servletContext中
        servletContext.setAttribute("stage2PossibilityMap",map);
    }
*/


}