package org.hyizhou.titaniumstation.systemstatus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 网速
 *
 * @author hyizhou
 * @date 2024/1/11
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class NetSpeedModel implements Serializable {
    // 时间戳
    private long timestamp;
    // 接口名，一般就是 wlan0、els1 这些
    private String name;
    // 发送字节数 / 秒
    private long sentSpeed;
    // 接收字节数 / 秒
    private long recvSpeed;
}
