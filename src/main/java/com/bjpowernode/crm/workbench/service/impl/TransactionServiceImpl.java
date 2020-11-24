package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.bean.Customer;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.service.TransactionService;
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
 * @CreateDate: 2020/11/24 15:44
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {
    
    
    @Autowired
    private CustomerMapper customerMapper;
    
    
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
}