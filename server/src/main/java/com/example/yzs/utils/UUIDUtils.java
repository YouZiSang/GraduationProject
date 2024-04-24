package com.example.yzs.utils;

import java.util.UUID;

/**
 * UUID工具类
 */
public class UUIDUtils {

    /**
     * 生成带有-的UUID字符串
     * @return 带有-的UUID字符串
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成不带有-的UUID字符串
     * @return 不带有-的UUID字符串
     */
    public static String simpleUuid() {
        return uuid().replaceAll("-", "");
    }

    /**
     * 生成32位长度的订单编号
     * @return 32位长度的订单编号
     */
    public static String orderNo() {
        String prefix = "2024";
        String uuid = UUIDUtils.simpleUuid().replaceAll("[^\\d]", "");
        int remainingLength = 19 - prefix.length();
        if (uuid.length() > remainingLength) {
            return prefix + uuid.substring(0, remainingLength);
        } else {
            return prefix + uuid;
        }
    }

    /**
     * 生成28位长度的支付编号，只包含数字
     * @return 28位长度的支付编号（只包含数字）
     */
    public static String payNo() {
        String uuid = simpleUuid().replaceAll("[^\\d]", "");
        int length = Math.min(uuid.length(), 28);
        return uuid.substring(0, length);
    }
}