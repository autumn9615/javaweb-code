package com.zhangshaoguo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ResponseController {
    // 基于传统的HttpServletResponse来设置
    @RequestMapping("/response1")
    public void response(HttpServletResponse response) throws IOException {
        // 设置响应状态码
        response.setStatus(401);
        // 设置响应头
        response.setHeader("name","zhangshaoguo");
        // 设置响应体
        response.getWriter().write("<h1>基于传统的HttpServletResponse来设置</h1>");
    }
    // 基于SpringBoot里面的封装
    @RequestMapping("/response2")
    public ResponseEntity<String> response(){
        return ResponseEntity.status(401)
                .header("name","zhangshaoguo2")
                .body("<h1>基于SpringBoot里面的封装</h1>");
    }
}
