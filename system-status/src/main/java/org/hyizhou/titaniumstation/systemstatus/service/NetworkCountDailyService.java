package org.hyizhou.titaniumstation.systemstatus.service;

import org.hyizhou.titaniumstation.systemstatus.dao.NetworkContDailyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hyizhou
 * @date 2024/1/18
 */
@Service
public class NetworkCountDailyService {
    @Autowired
    private NetworkContDailyDao networkContDailyDao;

}
