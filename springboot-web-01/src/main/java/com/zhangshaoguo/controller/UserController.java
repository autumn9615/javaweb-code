package com.zhangshaoguo.controller;

import com.zhangshaoguo.pojo.User;
import com.zhangshaoguo.service.UserService;
import com.zhangshaoguo.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
* 用户信息的Controller
* */
// 底层用到了ResponseBody注解，该注解拿到的返回值如果是对象或者集合，会先转为json格式，再响应
@RestController
public class UserController {
    // 引入成员变量，下面好拿来调用
    private UserService userService  = new UserServiceImpl();
    @RequestMapping("/list")
    public List<User> list(){
        // 调用UserService，拿到数据
        List<User> userList = userService .findAll();

        //3. 向前端返回数据（json格式的）
        return userList;
    }
}
