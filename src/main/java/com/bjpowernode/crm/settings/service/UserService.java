package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.bean.User;

import java.util.List;

public interface UserService {

    List<User> queryUsers();

    User login(User user);

    List<User> queryAllUsers();
}
