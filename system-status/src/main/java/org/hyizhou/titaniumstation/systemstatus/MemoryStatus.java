package org.hyizhou.titaniumstation.systemstatus;

import org.hyizhou.titaniumstation.systemstatus.model.MemoryModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hyizhou
 * @date 2024/1/11
 */
@Component
public final class MemoryStatus extends Status<MemoryModel> {
    @Override
    public MemoryModel getRealTimeStatus() {
        return null;
    }

    @Override
    public List<MemoryModel> getHistoricalStatus(LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }
}
