package com.example.yzs.utils;

import java.util.Random;

public class RandomUtil {
    private static final Random random = new Random();
    private RandomUtil() {
        throw new AssertionError();
    }


    /**
     * 生成指定位数的随机数字
     * @param minLength 随机数字的最小位数
     * @param maxLength 随机数字的最大位数
     * @return 随机数字字符串
     */
    public static String generateRandomNumber(int minLength, int maxLength) {
        int length = minLength + random.nextInt(maxLength - minLength + 1); // 生成长度在[minLength, maxLength]之间的随机数
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 生成0到9之间的随机数
        }
        return sb.toString();
    }
}