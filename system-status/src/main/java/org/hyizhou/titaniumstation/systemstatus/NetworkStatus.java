package org.hyizhou.titaniumstation.systemstatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hyizhou
 * @date 2024/1/11
 */
public final class NetworkStatus extends Status<NetworkStatus> {
    @Override
    public NetworkStatus getRealTimeStatus() {
        return null;
    }

    @Override
    public List<NetworkStatus> getHistoricalStatus(LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }
}
