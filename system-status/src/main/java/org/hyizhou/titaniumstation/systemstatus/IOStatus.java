package org.hyizhou.titaniumstation.systemstatus;

import org.hyizhou.titaniumstation.systemstatus.model.IOModel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hyizhou
 * @date 2024/1/11
 */
public final class IOStatus extends Status<IOModel> {
    @Override
    public IOModel getRealTimeStatus() {
        return null;
    }

    @Override
    public List<IOModel> getHistoricalStatus(LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }
}
