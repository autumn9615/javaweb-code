package com.zhangshaoguo.junit;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class UserService {

    /** 身份证生日字段格式: 严格模式(uuuu 而非 yyyy), 非法日期如 20250230 不会被自动纠正 */
    private static final DateTimeFormatter BIRTHDAY_FORMAT =
            DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT);

    /**
     * 给定一个身份证号, 计算出该用户的年龄
     * @param idCard 身份证号
     */
    public Integer getAge(String idCard){
        if (idCard == null || idCard.length() != 18) {
            throw new IllegalArgumentException("无效的身份证号码");
        }
        String birthday = idCard.substring(6, 14);
        LocalDate parse = LocalDate.parse(birthday, BIRTHDAY_FORMAT);
        return Period.between(parse, LocalDate.now()).getYears();
    }

    /**
     * 给定一个身份证号, 计算出该用户的性别
     * @param idCard 身份证号
     */
    public String getGender(String idCard){
        if (idCard == null || idCard.length() != 18) {
            throw new IllegalArgumentException("无效的身份证号码");
        }
        return Integer.parseInt(idCard.substring(16,17)) % 2 == 1 ? "男" : "女";
    }

}
