package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.base.bean.DictionaryType;
import com.bjpowernode.crm.base.bean.DictionaryValue;
import com.bjpowernode.crm.settings.mapper.DictionaryTypeMapper;
import com.bjpowernode.crm.settings.mapper.DictionaryValueMapper;
import com.bjpowernode.crm.settings.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.settings.service.impl
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/21 9:28
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {


    @Autowired
    private DictionaryTypeMapper dictionaryTypeMapper;

    @Autowired
    private DictionaryValueMapper dictionaryValueMapper;


    @Override
    public List<DictionaryType> queryDictionary() {
        //先查询字典的类型
        List<DictionaryType> dictionaryTypes = dictionaryTypeMapper.selectAll();

        for (DictionaryType dictionaryType : dictionaryTypes) {
            //取出每个字典类型的主键，查询该类型下的所有字典类型对应的values
            Example example = new Example(DictionaryValue.class);
            //按orderNo进行升序排序
            example.setOrderByClause("orderNo");
            example.createCriteria().andEqualTo("typeCode",dictionaryType.getCode());

            List<DictionaryValue> dictionaryValues = dictionaryValueMapper.selectByExample(example);


            //还要将dictionaryValues设置到每个字典类型中
            dictionaryType.setDictionaryValues(dictionaryValues);
        }

        return dictionaryTypes;
    }
}