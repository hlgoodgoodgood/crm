package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.base.constants.CrmExceptionEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.workbench.bean.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.workbench.service.impl
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/24 15:44
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {


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

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;

    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;





    @Override
    public List<String> queryCustomerName(String customerName) {
        Example example = new Example(Customer.class);
        example.createCriteria().andLike("name","%" + customerName + "%");
        List<Customer> customers = customerMapper.selectByExample(example);
        //专门存储客户的名字
        List<String> customerNames = new ArrayList<>();
        for (Customer customer : customers) {
            customerNames.add(customer.getName());
        }
        return customerNames;
    }

    //保存交易，新建交易历史
    @Override
    public void saveTransaction(Transaction transaction,String company) {

        //保存交易
        transaction.setId(UUIDUtil.getUUID());
        transaction.setCreateTime(DateTimeUtil.getSysTime());
        //取出transaction中customerId，如果为0，需要创建新客户
        if(transaction.getCustomerId().equals("0")){
            //创建新客户
            Customer customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(company);
            customer.setOwner(transaction.getOwner());
            customer.setNextContactTime(transaction.getNextContactTime());
            customer.setCreateBy(transaction.getCreateBy());
            customer.setContactSummary(transaction.getContactSummary());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setDescription(transaction.getDescription());
            int count = customerMapper.insertSelective(customer);
            if(count == 0){
                throw new CrmException(CrmExceptionEnum.CUSTOMER_SAVE);
            }
            //插入客户成功后，把客户的主键放在交易对象中
            transaction.setCustomerId(customer.getId());
        }
        int count = transactionMapper.insertSelective(transaction);
        if(count == 0){
            throw new CrmException(CrmExceptionEnum.TRANSACTION_SAVE);
        }
        //新建交易历史
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setId(UUIDUtil.getUUID());
        transactionHistory.setTranId(transaction.getId());
        transactionHistory.setStage(transaction.getStage());
        transactionHistory.setMoney(transaction.getMoney());
        transactionHistory.setExpectedDate(transaction.getExpectedDate());
        transactionHistory.setCreateTime(DateTimeUtil.getSysTime());
        transactionHistory.setCreateBy(transaction.getCreateBy());
        count = transactionHistoryMapper.insertSelective(transactionHistory);
        if(count == 0){
            throw new CrmException(CrmExceptionEnum.TRANSACTION_SAVE);
        }
    }

    @Override
    public String queryCustomerByName(String name) {
        //先根据客户名称查询客户是否存在，如果不存在，需要新建客户(创建交易的时候新建)
        Example example = new Example(Customer.class);
        example.createCriteria().andEqualTo("name",name);
        List<Customer> customers = customerMapper.selectByExample(example);


        if(customers.size() == 0){
           return "0";
        }

        return customers.get(0).getId();
    }

    @Override
    public Transaction queryTransactionById(String id, Map<String, String> stage2PossibilityMap) {
        Transaction transaction = transactionMapper.selectByPrimaryKey(id);

        //先查询所有者
        User user = userMapper.selectByPrimaryKey(transaction.getOwner());
        transaction.setOwner(user.getName());

        //查询客户信息
        Customer customer = customerMapper.selectByPrimaryKey(transaction.getCustomerId());
        transaction.setCustomerId(customer.getName());

        //查询当前交易的可能性
        String stage = transaction.getStage();
        String possibility = stage2PossibilityMap.get(stage);
        List<String> possibilities = new ArrayList<>();
        possibilities.add(possibility);
        transaction.setPossibility(possibilities);

        //查询联系人信息
        Contacts contacts = contactsMapper.selectByPrimaryKey(transaction.getContactsId());
        transaction.setContactsId(contacts.getFullname());

        //查询市场活动
        Activity activity = activityMapper.selectByPrimaryKey(transaction.getActivityId());
        transaction.setActivityId(activity.getName());

        //查询交易对应备注信息
        Example example = new Example(TransactionRemark.class);
        example.createCriteria().andEqualTo("tranId",transaction.getId());
        List<TransactionRemark> transactionRemarks = transactionRemarkMapper.selectByExample(example);
        transaction.setTransactionRemarks(transactionRemarks);

        //查询交易历史信息
        example = new Example(TransactionHistory.class);
        example.createCriteria().andEqualTo("tranId",transaction.getId());
        List<TransactionHistory> transactionHistories = transactionHistoryMapper.selectByExample(example);
        transaction.setTransactionHistories(transactionHistories);
        return transaction;
    }

    @Override
    public List<Map<String, String>> stageList(String tranId,Map<String,String> map) {
        //查询交易的阶段
        Transaction transaction = transactionMapper.selectByPrimaryKey(tranId);
        //当前交易的阶段
        String currentStage = transaction.getStage();

        //先找到交易可能性为0的那个分界点
        int pointer = 0;
        for(Map.Entry<String,String> entry : map.entrySet()){
            String value = entry.getValue();
            if(value.equals("0")){
                System.out.println(pointer);
                break;
            }
            pointer++;
        }

        int index = 0;
        for(Map.Entry<String,String> entry : map.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();

            //黑x 红x
            if(value.equals("0")){ //当前临界点
                if(currentStage.equals(key)){
                    //七个黑圈
                    for(int i = 0; i < pointer; i++){
                        System.out.println("黑圈");
                    }
                    //交易失败了 当前为红x
                    System.out.println("红x");
                    System.out.println("黑x");
                }else {//绿圈 锚点 黑圈
                    for(Map.Entry<String,String> entry1 : map.entrySet()){
                        index++;
                        if(currentStage.equals(entry.getKey())){
                            break;
                        }else{
                            if(index < pointer){
                                System.out.println("绿圈");
                            }else if(index == pointer){

                            }
                        }
                    }

                }
            }else{


            }

        }


        return null;
    }
}