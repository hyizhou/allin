package org.hyizhou.titaniumstation.systemstatus;

import org.hyizhou.titaniumstation.systemstatus.model.TemperatureModel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hyizhou
 * @date 2024/1/11
 */
public final class TemperatureStatus extends Status<TemperatureModel> {
    @Override
    public TemperatureModel getRealTimeStatus() {
        return null;
    }

    @Override
    public List<TemperatureModel> getHistoricalStatus(LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }
}
