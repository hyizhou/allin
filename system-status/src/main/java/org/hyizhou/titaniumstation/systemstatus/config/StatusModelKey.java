package org.hyizhou.titaniumstation.systemstatus.config;

/**
 * 记录存储到 redis 中数据的 key，键值就是枚举的name
 */
public enum StatusModelKey {
    MEM,  // 内存数据
    SPEED, //网速数据
    NETCOUNT  // 流量统计
}