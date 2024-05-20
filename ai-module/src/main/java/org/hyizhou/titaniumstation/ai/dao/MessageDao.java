package org.hyizhou.titaniumstation.ai.dao;

import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    /**
     * 查询“总结消息”之后的元素和“system”角色元素，按照时间排序
     * @param dialog DialogEntity实体类
     * @return List<MessageEntity>
     */
    @Query("""
        select m
        from MessageEntity m
        where (
            m.timestamp >= (
                select coalesce(max(timestamp), timestamp('2000-01-01 00:00:00'))
                from MessageEntity
                where dialog = :dialog and isSummary = true
            ) or m.role = 'system'
        ) and m.dialog = :dialog
        order by m.timestamp asc
        LIMIT 100
    """)
    List<MessageEntity> findMessageAfterLastSummary(@Param("dialog") DialogEntity dialog);

    @Query("""
        select m
        from MessageEntity m
        where (m.isSummary != true or m.isSummary is null ) and m.dialog = :dialog
        order by m.timestamp asc
        limit 100
    """)
    List<MessageEntity> findMessageNotHaveSummary(@Param("dialog") DialogEntity dialog);
}
