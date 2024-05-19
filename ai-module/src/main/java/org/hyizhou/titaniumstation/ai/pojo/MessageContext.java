package org.hyizhou.titaniumstation.ai.pojo;

import lombok.Data;
import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息上下文，主要用于链式处理统一对象
 * @date 2024/5/17
 */
@Data
public class MessageContext {
    private ContentReq contentReq;
    private ContentResp contentResp;
    private Prompt prompt;
    private ChatResponse chatResponse;
    private DialogEntity dialog;
    private List<MessageEntity> historyMessages;
    /*
    历史总结
     */
    private MessageEntity summaryEntity;
    /*
    请求消息的时间
     */
    private LocalDateTime reqTime;
    /*
    响应消息的时间
     */
    private LocalDateTime respTime;
}
