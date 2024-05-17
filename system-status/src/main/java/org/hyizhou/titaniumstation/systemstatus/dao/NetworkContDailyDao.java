package org.hyizhou.titaniumstation.systemstatus.dao;

import org.hyizhou.titaniumstation.systemstatus.pojo.NetworkContDailyPojo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hyizhou
 * @date 2024/1/18
 */
//@Repository
public interface NetworkContDailyDao extends JpaRepository<NetworkContDailyPojo, Integer> {
}
