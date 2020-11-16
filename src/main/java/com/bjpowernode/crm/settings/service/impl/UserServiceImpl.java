package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.base.constants.CrmExceptionEnum;
import com.bjpowernode.crm.base.exception.CrmException;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.base.util.DateTimeUtil;
import com.bjpowernode.crm.base.util.MD5Util;
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

    @Override
    public User login(User user) {
        //将用户输入的密码进行MD5加密
        String loginPass = MD5Util.getMD5(user.getLoginPwd());
        user.setLoginPwd(loginPass);

        //获取当前用户登录的ip地址，不取出就会当做条件参与查询
        String ip = user.getAllowIps();

        user.setAllowIps(null);
        user = userMapper.selectOne(user);
        //用户名或者密码错误
        if(user == null){
            throw new CrmException(CrmExceptionEnum.LOGIN_ACCOUNT_ERROR);
        }else{
            //校验账户是否失效
            String expireTime = user.getExpireTime();

            String now = DateTimeUtil.getSysTime();
            //失效了
            if(now.compareTo(expireTime) > 0){
                throw new CrmException(CrmExceptionEnum.LOGIN_ACCOUNT_EXPIRE);
            }
            //用户账户是否被禁用 0:禁用 1:可用
            if("0".equals(user.getLockState())){
                //用户账户被禁用了
                throw new CrmException(CrmExceptionEnum.LOGIN_ACCOUNT_FORBID);
            }
            //允许登录的ip地址
            if(user.getAllowIps() != null){
                if(!user.getAllowIps().contains(ip)){
                    //不允许登录的ip地址
                    throw new CrmException(CrmExceptionEnum.LOGIN_ACCOUNT_IP);
                }
            }
        }

        return user;
    }
}