package com.itheima;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void getAge() {
        UserService userService = new UserService();
        Assertions.assertEquals(18,userService.getAge("500222200710285412"));
    }

    @Test
    void getGender() {
        UserService userService = new UserService();
        Assertions.assertEquals("男",userService.getGender("500222200710285412"));
    }
}