package org.hyizhou.titaniumstation.systemstatus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网络接口统计信息
 *
 * @author hyizhou
 * @date 2024/1/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NetworkCountModel {
    // 时间戳
    private long timestamp;
    // 统计开始时间
    private long startTime;
    // 统计结束时间
    private long endTime;
    // 接口名
    private String name;
    // 统计的这段时间内发送的数据量，单位字节
    private long totalSent;
    private long totalRecv;

}
