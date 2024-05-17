package org.hyizhou.titaniumstation.systemstatus.dao;

import org.hyizhou.titaniumstation.systemstatus.pojo.NetworkContHourlyPojo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hyizhou
 * @date 2024/1/18
 */
public interface NetworkContHourlyDao  extends JpaRepository<NetworkContHourlyPojo, Integer> {
}
