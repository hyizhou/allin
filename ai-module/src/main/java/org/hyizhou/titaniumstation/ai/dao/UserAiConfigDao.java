package org.hyizhou.titaniumstation.ai.dao;

import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.entity.UserAiConfigEntity;
import org.hyizhou.titaniumstation.common.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @date 2024/5/18
 */
@Repository
public interface UserAiConfigDao extends JpaRepository<UserAiConfigEntity, Integer> {

    Optional<UserAiConfigEntity> findByUser(UserEntity user);

    boolean existsByDialog(DialogEntity dialog);
}
