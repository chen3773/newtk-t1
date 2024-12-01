package com.tiktok.task.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class OrderNumberGenerator {
    private static final Random random = new Random();

    public static String generateOrderNumber() {
        // 获取当前时间的时间戳
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 生成一个随机的三位数
        int randomNum = random.nextInt(900) + 100; // 100到999之间的随机数
        // 拼接时间戳和随机数
        return timestamp + randomNum;
    }

    public static String generateUSOrderNumber() {
        // 定义订单号的长度
        int length = 10;

        // 定义订单号的字符集
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        // 使用 StringBuilder 构建订单号
        StringBuilder orderNumber = new StringBuilder();

        // 使用 Random 生成随机字符
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            orderNumber.append(characters.charAt(random.nextInt(characters.length())));
        }

        return orderNumber.toString();
    }


    public static void main(String[] args) {
        String orderNumber = generateOrderNumber();
        System.out.println("生成的订单号: " + orderNumber);
    }
}
