package org.hyizhou.titaniumstation.systemstatus;

import org.hyizhou.titaniumstation.systemstatus.model.CPUModel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hyizhou
 * @date 2024/1/11
 */
public final class CPUStatus extends Status<CPUModel> {

    @Override
    public CPUModel getRealTimeStatus() {
        return null;
    }

    @Override
    public List<CPUModel> getHistoricalStatus(LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }
}
