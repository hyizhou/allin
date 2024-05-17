package org.hyizhou.titaniumstation.ai.processor.imp;

import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.processor.ChatRespProcessor;
import org.hyizhou.titaniumstation.ai.tools.CurrentDialogContext;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 将聊天响应保存到历史
 * @date 2024/5/17
 */
@Component
public class SaveHistoryChatRespProcessor implements ChatRespProcessor {
    @Autowired
    private DbHistoryRepository dbHistoryRepository;


    @Override
    public ChatResponse process(ChatResponse chatResponse) {
        MessageEntity messageEntity = geneMessageEntity(chatResponse);
        dbHistoryRepository.save(messageEntity);
        return chatResponse;
    }

    private MessageEntity geneMessageEntity(ChatResponse chatResponse){
        DialogEntity dialog = CurrentDialogContext.get();
        String content = chatResponse.getResult().getOutput().getContent();
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setRole(MessageType.ASSISTANT.getValue());
        messageEntity.setContent(content);
        messageEntity.setTimestamp(LocalDateTime.now());
        messageEntity.setType("text");  // 暂时只有这种
        messageEntity.setDialog(dialog);
        return messageEntity;
    }
}
