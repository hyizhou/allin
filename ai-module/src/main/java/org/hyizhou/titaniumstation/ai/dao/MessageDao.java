package org.hyizhou.titaniumstation.ai.dao;

import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @date 2024/5/17
 */
@Repository
public interface MessageDao extends JpaRepository<MessageEntity, String> {

    List<MessageEntity> findAllByDialog(DialogEntity dialog, Sort sort);

    /**
     * 匹配 dialog 消息，并使用 timestamp 倒序排序， 取出第一个
     * @param dialog DialogEntity实体类
     * @return MessageEntity实体类
     */
    @Query("select m from MessageEntity m where m.dialog = :dialog order by m.timestamp desc limit 1")
    Optional<MessageEntity> findLatestByDialog(DialogEntity dialog);
}
