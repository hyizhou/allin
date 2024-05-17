package org.hyizhou.titaniumstation.ai.dao;

import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @date 2024/5/16
 */
@Repository
public interface DialogDao extends JpaRepository<DialogEntity, String> {
}
