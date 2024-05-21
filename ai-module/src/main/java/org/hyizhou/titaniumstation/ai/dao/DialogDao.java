package org.hyizhou.titaniumstation.ai.dao;

import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.common.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @date 2024/5/16
 */
@Repository
public interface DialogDao extends JpaRepository<DialogEntity, String> {
    /**
     * 判断是否同时存在userId和dialogId
     */
    boolean existsByDialogIdAndUserId(String dialogId, int userId);

    /**
     * 获取对话
     */
    Optional<DialogEntity> findByDialogIdAndUser(String dialogId, UserEntity user);
}
