package org.hyizhou.titaniumstation.ai.dao;

import org.hyizhou.titaniumstation.ai.entity.HistoryStrategyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @date 2024/5/18
 */
@Repository
public interface HistoryStrategyDao extends JpaRepository<HistoryStrategyEntity, Long> {
}
