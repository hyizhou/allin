package org.hyizhou.titaniumstation.ai.processor.imp;

import org.hyizhou.titaniumstation.ai.processor.HistoryRepositoryTypeMessage;
import org.hyizhou.titaniumstation.ai.processor.PromptProcessor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

/**
 * @date 2024/5/16
 */
public class AppendHistoryPromptProcessor implements PromptProcessor {
    private final HistoryRepositoryTypeMessage historyRepository;

    public AppendHistoryPromptProcessor(HistoryRepositoryTypeMessage historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public Prompt process(Prompt prompt) {
        //步骤：1. 取出prompt中message对象，2. 取出历史消息，3.添加整合历史消息成message对象，4.创建新的prompt对象并返回
        List<Message> messages = prompt.getInstructions();
        messages = historyRepository.appendAll(messages);
        return new Prompt(messages, (ChatOptions) prompt.getOptions());
    }
}
