package org.hyizhou.titaniumstation.common.tools;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

/**
 * @date 2024/5/19
 */
public class DataTimeTool {
    /**
     * 根据给定的毫秒数增减 LocalDateTime 的时间。
     *
     * @param dateTime 原始的 LocalDateTime 对象
     * @param millis   要增减的毫秒数，正数表示增加，负数表示减少
     * @return 新的 LocalDateTime 对象，根据指定的毫秒数进行了增减操作
     */
    public static LocalDateTime addOrSubtractMillis(LocalDateTime dateTime, int millis) {
        // 根据毫秒数的正负决定是否需要转换成负数
        Duration duration = Duration.ofMillis(Math.abs(millis));

        // 计算新的时间
        LocalTime newTime;
        if (millis > 0) {
            newTime = dateTime.toLocalTime().plus(duration);
        } else {
            newTime = dateTime.toLocalTime().minus(duration);
        }

        // 检查时间调整后是否跨越了日期（例如，减去时间导致时间变早）
        LocalDateTime newDateTime;
        if (dateTime.toLocalTime().isAfter(newTime)) {
            newDateTime = LocalDateTime.of(dateTime.toLocalDate().minusDays(1), newTime);
        } else {
            newDateTime = LocalDateTime.of(dateTime.toLocalDate(), newTime);
        }

        return newDateTime;
    }

    public static void main(String[] args) {
        // 假设我们有一个LocalDateTime对象
        LocalDateTime dateTime = LocalDateTime.now();

        // 将LocalDateTime转换为时间戳（毫秒）
        long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();

        // 假设我们要添加24小时的时间间隔
        long interval = 1000*24*60*60; // 24小时转换为毫秒
        long newTimestamp = timestamp + interval; // 直接加减时间戳

        Duration duration = Duration.ofMillis(interval);
        LocalDateTime targetTime = dateTime.minus(duration);
        // 将新的时间戳转换回LocalDateTime
        LocalDateTime newDateTime = LocalDateTime.ofEpochSecond(newTimestamp / 1000, 0, ZoneOffset.UTC);

        System.out.println("Original LocalDateTime: " + dateTime);
        System.out.println("New LocalDateTime after adding 24 hours: " + targetTime);
    }
}
