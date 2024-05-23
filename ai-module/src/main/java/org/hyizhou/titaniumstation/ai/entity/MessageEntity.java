package org.hyizhou.titaniumstation.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 聊天消息表
 * @date 2024/5/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private String messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id", nullable = false)
    private DialogEntity dialog;

    @Column(nullable = true)
    private Boolean isSummary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String type;

    @PrePersist
    protected void onCreate() {
        if (this.messageId == null)  {
            this.messageId = UUID.randomUUID().toString();
        }
    }
}
