package org.hyizhou.titaniumstation.systemstatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hyizhou
 * @date 2024/1/11
 */
public abstract sealed class Status<T> permits MemoryStatus,CPUStatus, TemperatureStatus,IOStatus,DiskStatus,NetworkStatus{

    /**
     * 获取实时数据
     * @return 状态的实体类
     */
    abstract public T getRealTimeStatus();

    /**
     * 获取指定时间段内的历史数据
     */
    abstract public List<T> getHistoricalStatus(LocalDateTime startTime, LocalDateTime endTime);
}
