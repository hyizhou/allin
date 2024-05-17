package org.hyizhou.titaniumstation.systemstatus.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 内存状态对应实体类
 *
 * @author hyizhou
 * @date 2024/1/11
 */
@Data
@Builder
public class MemoryModel implements Serializable {
    // 时间戳
    private long timestamp;
    // 总内存
    private long totalMemory;
    // 空闲内存
    private long freeMemory;
    // swap 总内存
    private long totalSwap;
    // swap 空闲
    private long freeSwap;
}
