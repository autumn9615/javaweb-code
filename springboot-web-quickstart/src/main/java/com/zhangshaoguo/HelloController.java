package com.zhangshaoguo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 标识这是一个请求处理类
@RestController
public class HelloController {
    // 标识请求路径
    @RequestMapping("/hello")
    public String hello(String name){
        System.out.println("HelloController...hello:" + name);
        return "Hello" + name + "~";
    }
}
