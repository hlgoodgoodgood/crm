package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.base.constants.CrmExceptionEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.ActivityQueryVo;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.workbench.service.impl
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/17 9:34
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public List<Map<String, String>> queryAllActivity( ActivityQueryVo queryVo) {
        return activityMapper.queryAllActivity(queryVo);
    }

    @Override
    public void saveActivity(Activity activity) {

        //uuid主键
        activity.setId(UUIDUtil.getUUID());

        //创建时间
        activity.setCreateTime(DateTimeUtil.getSysTime());
        //更新时间
        activity.setEditTime(DateTimeUtil.getSysTime());

        //insertSelective:控制就不参与插入操作
        int count = activityMapper.insertSelective(activity);
        //更新失败
        if(count == 0){
            throw new CrmException(CrmExceptionEnum.ACTIVITY_SAVE);
        }
    }
}