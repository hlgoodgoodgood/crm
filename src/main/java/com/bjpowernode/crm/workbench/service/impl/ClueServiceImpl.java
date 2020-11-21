package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.base.constants.CrmExceptionEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.workbench.bean.Activity;
import com.bjpowernode.crm.workbench.bean.Clue;
import com.bjpowernode.crm.workbench.bean.ClueActivityRelation;
import com.bjpowernode.crm.workbench.bean.ClueRemark;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.mapper.ClueActivityRelationMapper;
import com.bjpowernode.crm.workbench.mapper.ClueRemarkMapper;
import com.bjpowernode.crm.workbench.mapper.ClueMapper;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.workbench.service.impl
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/21 14:33
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;


    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void saveClue(Clue clue) {
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateTime(DateTimeUtil.getSysTime());

        int count = clueMapper.insertSelective(clue);

        if(count == 0){
            throw new CrmException(CrmExceptionEnum.CLUE_SAVE);
        }
    }

    @Override
    public Clue queryClueDetailById(String id) {
        //查询线索
        Clue clue = clueMapper.selectByPrimaryKey(id);

        //根据clue中的owner查询对用的用户
        User user = userMapper.selectByPrimaryKey(clue.getOwner());

        //将用户的姓名设置到owner
        clue.setOwner(user.getName());

        //查询线索下对应的备注信息
        Example example = new Example(ClueRemark.class);
        example.createCriteria().andEqualTo("clueId",clue.getId());
        List<ClueRemark> clueRemarks = clueRemarkMapper.selectByExample(example);

        //查询线索对应的活动信息
        //思路，先查询关联中间表，得到对应市场活动的id集合,在根据集合中的id号查询对应市场活动的信息
        ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
        clueActivityRelation.setClueId(clue.getId());
        List<ClueActivityRelation> clueActivityRelations =
                clueActivityRelationMapper.select(clueActivityRelation);

        List<Activity> activities = new ArrayList<>();
        for (ClueActivityRelation activityRelation : clueActivityRelations) {
            //取出每个对应的activityId，查询市场活动表对应的数据
            Activity activity = activityMapper.selectByPrimaryKey(activityRelation.getActivityId());

            activities.add(activity);

        }
        //把查询出来的市场活动集合设置到clue中
        clue.setActivities(activities);

        //将线索下面对应的所有备注信息封装到线索中
        clue.setClueRemarks(clueRemarks);
        return clue;
    }

    @Override
    public void updateClueRemark(ClueRemark clueRemark) {
        clueRemark.setEditTime(DateTimeUtil.getSysTime());
        clueRemark.setEditFlag("1");
        int count = clueRemarkMapper.updateByPrimaryKeySelective(clueRemark);
        if(count == 0){
            throw new CrmException(CrmExceptionEnum.CLUE_REMARK_UPDATE);
        }

    }

    @Override
    public void saveClueRemark(ClueRemark clueRemark) {
        clueRemark.setEditFlag("0");
        clueRemark.setId(UUIDUtil.getUUID());
        clueRemark.setCreateTime(DateTimeUtil.getSysTime());
        int count = clueRemarkMapper.insertSelective(clueRemark);
        if(count == 0){
            throw new CrmException(CrmExceptionEnum.CLUE_REMARK_SAVE);
        }

    }
}