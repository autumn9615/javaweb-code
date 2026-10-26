package com.itheima;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserService 单元测试, 重点覆盖 getAge(String idCard) 方法
 */
class UserServiceTest {

    private static final DateTimeFormatter BIRTHDAY_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String INVALID_ID_CARD_MESSAGE = "无效的身份证号码";

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    /**
     * 根据生日构造一个合法的 18 位身份证号
     * 结构: 6 位地区码 + 8 位生日(yyyyMMdd) + 3 位顺序码 + 1 位校验位
     * 未做 GB 11643 校验位计算, 因为 getAge 只读取生日字段
     */
    private static String buildIdCard(LocalDate birthday) {
        return "500222" + birthday.format(BIRTHDAY_FORMAT) + "001" + "X";
    }

    @Nested
    @DisplayName("getAge - 正常计算年龄")
    class GetAgeNormal {

        @Test
        @DisplayName("有效18位身份证号: 生日为18年前的今天, 返回18")
        void getAge_validIdCard_birthdayExactly18YearsAgo_returns18() {
            String idCard = buildIdCard(LocalDate.now().minusYears(18));

            Integer age = userService.getAge(idCard);

            assertEquals(18, age);
        }

        @Test
        @DisplayName("有效18位身份证号: 生日为30年前, 返回30")
        void getAge_validIdCard_birthday30YearsAgo_returns30() {
            String idCard = buildIdCard(LocalDate.now().minusYears(30));

            assertEquals(30, userService.getAge(idCard));
        }

        @Test
        @DisplayName("有效18位身份证号: 今年生日已过, 返回满岁年龄")
        void getAge_birthdayAlreadyPassedThisYear_returnsFullYears() {
            // 生日取 1 月 1 日, 除去 1 月 1 日当天, 今年都已过生日
            LocalDate birthday = LocalDate.now().minusYears(25).withDayOfYear(1);

            int expected = LocalDate.now().getDayOfYear() == 1 ? 24 : 25;
            assertEquals(expected, userService.getAge(buildIdCard(birthday)));
        }

        @Test
        @DisplayName("有效18位身份证号: 今年生日未到, 返回上一年的年龄")
        void getAge_birthdayNotReachedThisYear_returnsAgeMinusOne() {
            // 生日取 12 月 31 日, 除去 12 月 31 日当天, 今年都还没过生日
            LocalDate birthday = LocalDate.now().minusYears(30).withMonth(12).withDayOfMonth(31);

            int expected = LocalDate.now().getMonthValue() == 12 && LocalDate.now().getDayOfMonth() == 31 ? 30 : 29;
            assertEquals(expected, userService.getAge(buildIdCard(birthday)));
        }
    }

    @Nested
    @DisplayName("getAge - 边界情况")
    class GetAgeBoundary {

        @Test
        @DisplayName("刚出生的用户: 生日为今天, 返回0")
        void getAge_newbornToday_returns0() {
            String idCard = buildIdCard(LocalDate.now());

            assertEquals(0, userService.getAge(idCard));
        }

        @Test
        @DisplayName("生日为当天: 年龄按整年计算, 生日当天即满岁")
        void getAge_birthdayIsToday_countsAsFullYear() {
            int years = 10;
            String idCard = buildIdCard(LocalDate.now().minusYears(years));

            // 生日当天应计满岁, 而不是 still 9 岁
            assertEquals(years, userService.getAge(idCard));
            // 生日前一天则应少 1 岁
            assertEquals(years - 1, userService.getAge(buildIdCard(LocalDate.now().minusYears(years).plusDays(1))));
        }

        @Test
        @DisplayName("生日为昨天: 年龄已满一岁, 返回1")
        void getAge_birthdayYesterday_returns1() {
            String idCard = buildIdCard(LocalDate.now().minusYears(1).minusDays(1));

            assertEquals(1, userService.getAge(idCard));
        }
    }

    @Nested
    @DisplayName("getAge - 非法入参")
    class GetAgeInvalidArgument {

        @Test
        @DisplayName("身份证号为 null, 抛出 IllegalArgumentException")
        void getAge_nullIdCard_throwsIllegalArgumentException() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> userService.getAge(null));
            assertEquals(INVALID_ID_CARD_MESSAGE, ex.getMessage());
        }

        @ParameterizedTest(name = "长度 = {0}")
        @ValueSource(ints = {0, 1, 6, 17, 19, 25})
        @DisplayName("身份证号长度不为18位, 抛出 IllegalArgumentException")
        void getAge_lengthNot18_throwsIllegalArgumentException(int length) {
            String idCard = "5".repeat(length);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> userService.getAge(idCard));
            assertEquals(INVALID_ID_CARD_MESSAGE, ex.getMessage());
        }

        @Test
        @DisplayName("身份证号为空字符串, 抛出 IllegalArgumentException")
        void getAge_emptyString_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> userService.getAge(""));
        }
    }

    @Nested
    @DisplayName("getAge - 生日字段非法")
    class GetAgeInvalidBirthday {

        @Test
        @DisplayName("生日字段含非数字字符, 抛出 DateTimeParseException")
        void getAge_birthdayNotNumeric_throwsDateTimeParseException() {
            String idCard = "500222abcd0101001X";

            assertThrows(DateTimeParseException.class, () -> userService.getAge(idCard));
        }

        @Test
        @DisplayName("生日字段月份为 13, 抛出 DateTimeParseException")
        void getAge_birthdayInvalidMonth_throwsDateTimeParseException() {
            String idCard = "50022220261301001X";

            assertThrows(DateTimeParseException.class, () -> userService.getAge(idCard));
        }

        @Test
        @DisplayName("生日字段为 2 月 30 日, 抛出 DateTimeParseException")
        void getAge_birthdayFebruary30_throwsDateTimeParseException() {
            String idCard = "50022220250230001X";

            assertThrows(DateTimeParseException.class, () -> userService.getAge(idCard));
        }

        @Test
        @DisplayName("非闰年的 2 月 29 日, 抛出 DateTimeParseException")
        void getAge_birthdayFeb29NonLeapYear_throwsDateTimeParseException() {
            // 2023 不是闰年
            String idCard = "50022220230229001X";

            assertThrows(DateTimeParseException.class, () -> userService.getAge(idCard));
        }

        @Test
        @DisplayName("生日字段非法不属于 IllegalArgumentException 的同一报错语义")
        void getAge_invalidBirthday_isNotReportedAsInvalidIdCard() {
            // 说明当前实现: 长度校验与日期解析错误类型不同, UI 层需分别处理
            Exception ex = assertThrows(Exception.class, () -> userService.getAge("50022220261301001X"));
            assertFalse(ex.getMessage().equals(INVALID_ID_CARD_MESSAGE));
        }
    }

    @Nested
    @DisplayName("getGender - 原有行为回归")
    class GetGender {

        @Test
        @DisplayName("第17位为奇数, 返回男")
        void getGender_oddSequenceDigit_returnsMale() {
            assertEquals("男", userService.getGender("500222200710285412"));
        }

        @Test
        @DisplayName("第17位为偶数, 返回女")
        void getGender_evenSequenceDigit_returnsFemale() {
            assertEquals("女", userService.getGender("500222200710285422"));
        }

        @Test
        @DisplayName("身份证号为 null, 抛出 IllegalArgumentException")
        void getGender_nullIdCard_throwsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> userService.getGender(null));
        }
    }
}
