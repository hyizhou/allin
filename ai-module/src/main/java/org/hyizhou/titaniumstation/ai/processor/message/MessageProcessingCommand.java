package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;
import org.springframework.stereotype.Component;

/**
 * 处理对话聊天命令模式的实现
 * @date 2024/5/17
 */
@Component
public class MessageProcessingCommand {
    private final CheckDialogMessageProcessor checkDialogMessageProcessor;
    private final GenerationPromptProcessor generationPromptProcessor;
    private final AIInvocationProcessor aiInvocationProcessor;
    private final SaveHistoryProcessor saveHistoryProcessor;
    private final GenerationRespProcessor generationRespProcessor;
    private final HistoryAppendProcessor historyAppendProcessor;
    private final SetNotBusyProcessor setNotBusyProcessor;

    public MessageProcessingCommand(CheckDialogMessageProcessor checkDialogMessageProcessor,
                                    GenerationPromptProcessor generationPromptProcessor,
                                    AIInvocationProcessor aiInvocationProcessor,
                                    SaveHistoryProcessor saveHistoryProcessor,
                                    GenerationRespProcessor generationRespProcessor,
                                    HistoryAppendProcessor historyAppendProcessor, SetNotBusyProcessor setNotBusyProcessor) {
        this.checkDialogMessageProcessor = checkDialogMessageProcessor;
        this.generationPromptProcessor = generationPromptProcessor;
        this.aiInvocationProcessor = aiInvocationProcessor;
        this.saveHistoryProcessor = saveHistoryProcessor;
        this.generationRespProcessor = generationRespProcessor;
        this.historyAppendProcessor = historyAppendProcessor;
        this.setNotBusyProcessor = setNotBusyProcessor;
    }

    public ContentResp execute(ContentReq contentReq) {
        MessageContext messageContext = new MessageContext();
        messageContext.setContentReq(contentReq);
        // 这里顺序很关键
        messageContext = checkDialogMessageProcessor.process(messageContext);
        messageContext = historyAppendProcessor.process(messageContext);
        messageContext = generationPromptProcessor.process(messageContext);
        messageContext = aiInvocationProcessor.process(messageContext);
        messageContext = generationRespProcessor.process(messageContext);
        messageContext = saveHistoryProcessor.process(messageContext);
        setNotBusyProcessor.process(messageContext);
        return messageContext.getContentResp();
    }
}
