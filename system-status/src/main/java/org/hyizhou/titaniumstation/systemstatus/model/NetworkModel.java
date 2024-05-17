package org.hyizhou.titaniumstation.systemstatus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 存储网络接口相关信息
 *
 * @author hyizhou
 * @date 2024/1/15
 */
@Data
@AllArgsConstructor
@Builder
public class NetworkModel implements Serializable {
    // 时间戳
    private long timestamp;
    // 接口名
    private String name;
    // 发送数据量
    private long bytesSent;
    // 接收数据量
    private long bytesRecv;
}
