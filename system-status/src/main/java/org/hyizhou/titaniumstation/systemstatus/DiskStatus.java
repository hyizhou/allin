package org.hyizhou.titaniumstation.systemstatus;

import org.hyizhou.titaniumstation.systemstatus.model.DiskModel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hyizhou
 * @date 2024/1/11
 */
public final class DiskStatus extends Status<DiskModel> {
    @Override
    public DiskModel getRealTimeStatus() {
        return null;
    }

    @Override
    public List<DiskModel> getHistoricalStatus(LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }
}
