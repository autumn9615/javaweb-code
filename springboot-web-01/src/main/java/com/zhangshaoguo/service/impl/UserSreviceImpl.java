package com.zhangshaoguo.service.impl;

import com.zhangshaoguo.dao.UserDao;
import com.zhangshaoguo.dao.impl.UserDaoImpl;
import com.zhangshaoguo.pojo.User;
import com.zhangshaoguo.service.UserSrevice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserSreviceImpl implements UserSrevice {
    // lines是UserDaoImpl实现类里面的，接口是UserDao，现在没有这个类，就要调用把他传进来（设为成员变量）
    private  UserDao userDao = new UserDaoImpl();
    @Override
    public List<User> findAll() {
        // 调用UserDao，拿到数据
        List<String> lines  = userDao.findAll();

        //2.解析用户信息，封装为User对象 -> list集合（用IO流完成）
        List<User> userList = lines.stream().map((line)->{
            String[] split = line.split(",");
            Integer id = Integer.parseInt( split[0] );
            String username = split[1];
            String password = split[2];
            String name = split[3];
            Integer age = Integer.parseInt( split[4] );
            LocalDateTime updatetime = LocalDateTime.parse( split[5], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return new User( id,username,password,name,age,updatetime);
        }).toList();

        return userList;
    }
}
