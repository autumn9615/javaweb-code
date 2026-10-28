package com.zhangshaoguo.service;

import com.zhangshaoguo.pojo.User;

import java.util.List;

public interface UserSrevice {
    /*
     * 查询所有用户信息的
     * */
    List<User> findAll();
}
