package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.base.constants.CrmExceptionEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.workbench.bean.*;
import com.bjpowernode.crm.workbench.mapper.*;
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

    @Autowired
    private CustomerMapper customerMapper;

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

   //解除线索和市场活动的关联
    @Override
    public void deleteBind(ClueActivityRelation clueActivityRelation) {

        int count =  clueActivityRelationMapper.delete(clueActivityRelation);
        if(count == 0){
            throw new CrmException(CrmExceptionEnum.CLUE_ACTIVITY_UNBIND);
        }

    }
    //解除多个线索和市场活动的关联
    @Override
    public void deleteManyBind(String clueId, String activityIds) {
        String[] aids = activityIds.split(",");

        for (String aid : aids) {
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setActivityId(aid);
            int count = clueActivityRelationMapper.delete(clueActivityRelation);
            if(count == 0){
               throw new CrmException(CrmExceptionEnum.CLUE_ACTIVITY_UNBIND);
            }
        }

    }

    @Override
    public List<Activity> queryActivityExculdeNow(String clueId, String activityName) {
        //查询出该线索下的关联的市场活动id
        ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
        clueActivityRelation.setClueId(clueId);
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationMapper.select(clueActivityRelation);
        //将clueActivityRelations集合中每个ClueActivityRelation中的activityId号取出来放入到集合中
        List<String> activityIds = new ArrayList<>();
        for (ClueActivityRelation activityRelation : clueActivityRelations) {
            activityIds.add(activityRelation.getActivityId());
        }

        //因为tkMapper中的andNotIn支持多id号查询，没必须进行遍历单个查询
        Example example = new Example(Activity.class);
        //因为用户可能输入活动名称，也可能不输入
        if(activityName != null && activityName != ""){
            example.createCriteria().andLike("name","%" + activityName + "%")
                    .andNotIn("id",activityIds);
        }else{
            example.createCriteria().andNotIn("id",activityIds);
        }

        List<Activity> activities = activityMapper.selectByExample(example);
        //查询用户表，将用户姓名放置到activity的owner
        for (Activity activity : activities) {
            User user = userMapper.selectByPrimaryKey(activity.getOwner());

            activity.setOwner(user.getName());
        }
        return activities;
    }

    //保存线索和市场活动的关联
    @Override
    public void saveBind(String clueId, String activityIds) {
        String[] aids = activityIds.split(",");

        for (String aid : aids) {
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setActivityId(aid);
            int count = clueActivityRelationMapper.insertSelective(clueActivityRelation);
            if(count == 0){
                throw new CrmException(CrmExceptionEnum.CLUE_ACTIVITY_BIND);
            }
        }

    }

    //线索和市场活动关联成功后，再异步查询关联后的所有市场活动
    @Override
    public List<Activity> queryClueActivity(String clueId) {
        ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
        clueActivityRelation.setClueId(clueId);
        List<ClueActivityRelation> clueActivityRelations =
                clueActivityRelationMapper.select(clueActivityRelation);
        List<Activity> activities = new ArrayList<>();
        //查询对应的所有市场活动id
        for (ClueActivityRelation activityRelation : clueActivityRelations) {
            Activity activity = activityMapper.selectByPrimaryKey(activityRelation.getActivityId());
            User user = userMapper.selectByPrimaryKey(activity.getOwner());
            activity.setOwner(user.getName());
            activities.add(activity);
        }
        return activities;
    }

    @Override
    public void convert(String id,String username) {
        //1、根据线索的主键查询线索的信息(线索包含自身的信息，包含客户的信息，包含联系人信息)
        Clue clue = clueMapper.selectByPrimaryKey(id);

        //先将线索中的客户信息取出来保存在客户表中，当该客户不存在的时候，新建客户(按公司名称精准查询)
        /**
         * 1、先取出clue中company进行判断客户是否存在
         */
        Example example = new Example(Customer.class);
        example.createCriteria().andEqualTo("name",clue.getCompany());
        int count = customerMapper.selectCountByExample(example);

        if(count == 0){
           //客户不存在，创建一个新客户
            Customer customer = new Customer();
            //从线索中取出客户信息
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setContactSummary(clue.getContactSummary());
            //谁点击了转换按钮，谁就是创建人
            customer.setCreateBy(username);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setName(clue.getCompany());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setPhone(clue.getPhone());
            customer.setWebsite(clue.getWebsite());
            //保存客户信息
            int result = customerMapper.insertSelective(customer);
            if(result == 0){
                throw new CrmException(CrmExceptionEnum.CLUE_CONVERT);
            }
        }

    }
}