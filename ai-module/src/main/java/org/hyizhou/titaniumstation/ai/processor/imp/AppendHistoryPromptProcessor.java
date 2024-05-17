package org.hyizhou.titaniumstation.ai.processor.imp;

import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.processor.HistoryRepositoryTypeEntity;
import org.hyizhou.titaniumstation.ai.processor.HistoryRepositoryTypeMessage;
import org.hyizhou.titaniumstation.ai.processor.PromptProcessor;
import org.hyizhou.titaniumstation.ai.tools.CurrentDialogContext;
import org.hyizhou.titaniumstation.ai.tools.PromptTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 将新的Prompt存储到历史，并将历史追加到Prompt中
 * @date 2024/5/16
 */
public class AppendHistoryPromptProcessor implements PromptProcessor {
    private final HistoryRepositoryTypeMessage historyRepository;
    private final HistoryRepositoryTypeEntity historyRepositoryEntity;
    private final Logger logger = LoggerFactory.getLogger(AppendHistoryPromptProcessor.class);

    public AppendHistoryPromptProcessor(HistoryRepositoryTypeMessage historyRepository, HistoryRepositoryTypeEntity historyRepositoryEntity) {
        this.historyRepository = historyRepository;
        this.historyRepositoryEntity = historyRepositoryEntity;
    }

    @Override
    public Prompt process(Prompt prompt) {
        if (prompt.getInstructions().size() != 1){
            throw new RuntimeException("prompt消息个数异常，个数必须为1");
        }
        var messageEntity = generationMessageEntity(prompt);
        messageEntity = historyRepositoryEntity.appendAll(messageEntity);
        return generationPrompt(messageEntity, (ChatOptions) prompt.getOptions());
        //步骤：1. 取出prompt中message对象，2. 取出历史消息，3.添加整合历史消息成message对象，4.创建新的prompt对象并返回
//        List<Message> messages = prompt.getInstructions();
//        messages = historyRepository.appendAll(messages);
//        return new Prompt(messages, (ChatOptions) prompt.getOptions());
    }

    private List<MessageEntity> generationMessageEntity(Prompt prompt) {
        List<Message> messages = prompt.getInstructions();
        DialogEntity dialogEntity = CurrentDialogContext.get();
        return messages.stream().map(message -> {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setDialog(dialogEntity);
            messageEntity.setContent(message.getContent());
            messageEntity.setTimestamp(LocalDateTime.now());
            messageEntity.setRole(message.getMessageType().getValue());
            messageEntity.setType("text");
            return messageEntity;
        }).toList();
    }

    private Prompt generationPrompt(List<MessageEntity> messages, ChatOptions options) {
        logger.debug(String.valueOf(messages.size()));
        List<Message> messageList = messages.stream().map(messageEntity ->
                PromptTools.generateMessage(messageEntity.getContent(), messageEntity.getRole())
        ).toList();
        return new Prompt(messageList, options);
    }
}
