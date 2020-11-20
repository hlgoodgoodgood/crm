package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.base.constants.CrmExceptionEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.ActivityQueryVo;
import com.bjpowernode.crm.workbench.bean.ActivityRemark;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.mapper.ActivityRemarkMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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

    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;

    @Autowired
    private UserMapper userMapper;

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

    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateActivity(Activity activity) {
        activity.setEditTime(DateTimeUtil.getSysTime());
        int count = activityMapper.updateByPrimaryKeySelective(activity);
        if(count == 0){
            throw new CrmException(CrmExceptionEnum.ACTIVITY_UPDATE);
        }
    }

    @Override
    public void deleteActivity(String id) {
        //删除市场活动
        int count =  activityMapper.deleteByPrimaryKey(id);
        //删除市场活动下的对应的市场活动备注
       /* 精准删除
       ActivityRemark activityRemark = new ActivityRemark();
        //只能精准删除 delete from tbl_activity_remark where activityId=?
        //如果需要模糊匹配的话，只能用Example
        activityRemark.setActivityId(id);
        activityRemarkMapper.delete(activityRemark);*/

       /*
       Example删除
        */
       Example example = new Example(ActivityRemark.class);
       example.createCriteria().andEqualTo("activityId",id);
       activityRemarkMapper.deleteByExample(example);
        if(count == 0){
            throw new CrmException(CrmExceptionEnum.ACTIVITY_DELETE);
        }
    }

    @Override
    public Activity queryActivityDetailById(String id) {
        Activity activity = activityMapper.selectByPrimaryKey(id);

        //根据activity中的owner查询对用的用户
        User user = userMapper.selectByPrimaryKey(activity.getOwner());

        //将用户的姓名设置到owner
        activity.setOwner(user.getName());

        //根据市场活动的主键查询对应的市场活动备注
        /**
         * 当我们使用tkMapper条件查询的时候,把查询条件拼接到Example中
         */
        Example example = new Example(ActivityRemark.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activityId",activity.getId());
        List<ActivityRemark> activityRemarks = activityRemarkMapper.selectByExample(example);

        //把activityRemarks设置到activity
        activity.setActivityRemarks(activityRemarks);
        return activity;
    }

    @Override
    public void updateActivityRemark(ActivityRemark activityRemark) {
        activityRemark.setEditTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("1");
        int count =  activityRemarkMapper.updateByPrimaryKeySelective(activityRemark);
        if(count == 0){
            throw new CrmException(CrmExceptionEnum.ACTIVITY_REMARK_UPDATE);
        }
    }

    @Override
    public void deleteActivityRemark(String id) {
        int count =  activityRemarkMapper.deleteByPrimaryKey(id);
        if(count == 0){
            throw new CrmException(CrmExceptionEnum.ACTIVITY_REMARK_DELETE);
        }
    }

    @Override
    public void saveActivityRemark(ActivityRemark activityRemark) {
        //指定主键
        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("0");

        int count =  activityRemarkMapper.insertSelective(activityRemark);
        if(count == 0){
            throw new CrmException(CrmExceptionEnum.ACTIVITY_REMARK_SAVE);
        }

    }
}