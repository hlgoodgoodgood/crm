package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: crm
 * @Package: com.bjpowernode.crm.settings.service.impl
 * @Description: java类作用描述
 * @Author: Andy
 * @CreateDate: 2020/11/16 10:39
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2020
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> queryUsers() {
        return userMapper.selectAll();
    }
}