package com.zhangshaoguo.controller;

import cn.hutool.core.io.IoUtil;
import com.zhangshaoguo.pojo.User;
import com.zhangshaoguo.service.UserSrevice;
import com.zhangshaoguo.service.impl.UserSreviceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
* 用户信息的Controller
* */
// 底层用到了ResponseBody注解，该注解拿到的返回值如果是对象或者集合，会先转为json格式，再响应
@RestController
public class UserController {
    // 引入成员变量，下面好拿来调用
    private UserSrevice UserSrevice = new UserSreviceImpl();
    @RequestMapping("/list")
    public List<User> list(){
        // 调用UserService，拿到数据
        List<User> userList = UserSrevice.findAll();

        //3. 向前端返回数据（json格式的）
        return userList;
    }
}



// --------------------------------------------原本的写法----------------------------------------------



//        //1. 加载并读取user.txt文件，获取用户信息
//        InputStream in = this.getClass().getClassLoader().getResourceAsStream("user.txt");
//        ArrayList<String> lines = IoUtil.readLines(in, StandardCharsets.UTF_8,new ArrayList<>());
//
//        //2.解析用户信息，封装为User对象 -> list集合（用IO流完成）
//        List<User> userList = lines.stream().map((line)->{
//            String[] split = line.split(",");
//            Integer id = Integer.parseInt( split[0] );
//            String username = split[1];
//            String password = split[2];
//            String name = split[3];
//            Integer age = Integer.parseInt( split[4] );
//            LocalDateTime updatetime = LocalDateTime.parse( split[5],DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//            return new User( id,username,password,name,age,updatetime);
//        }).toList();

