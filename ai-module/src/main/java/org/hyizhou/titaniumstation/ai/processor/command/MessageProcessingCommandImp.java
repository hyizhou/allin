package org.hyizhou.titaniumstation.ai.processor.command;

import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;

/**
 * 对话聊天模式命令模式实现
 * @date 2024/5/17
 */
public class MessageProcessingCommandImp implements MessageProcessingCommand {
    private final CheckUserMessageProcessor checkUserMessageProcessor;
    private final AddDialogParamProcessor addDialogParamProcessor;
    private final GenerationPromptProcessor generationPromptProcessor;
    private final AIInvocationProcessor aiInvocationProcessor;
    private final SaveHistoryProcessor saveHistoryProcessor;
    private final GenerationRespProcessor generationRespProcessor;
    private final HistoryAppendProcessor historyAppendProcessor;

    public MessageProcessingCommandImp(CheckUserMessageProcessor checkUserMessageProcessor,
                                       AddDialogParamProcessor addDialogParamProcessor,
                                       GenerationPromptProcessor generationPromptProcessor,
                                       AIInvocationProcessor aiInvocationProcessor,
                                       SaveHistoryProcessor saveHistoryProcessor,
                                       GenerationRespProcessor generationRespProcessor,
                                       HistoryAppendProcessor historyAppendProcessor) {
        this.checkUserMessageProcessor = checkUserMessageProcessor;
        this.addDialogParamProcessor = addDialogParamProcessor;
        this.generationPromptProcessor = generationPromptProcessor;
        this.aiInvocationProcessor = aiInvocationProcessor;
        this.saveHistoryProcessor = saveHistoryProcessor;
        this.generationRespProcessor = generationRespProcessor;
        this.historyAppendProcessor = historyAppendProcessor;
    }

    @Override
    public ContentResp execute(ContentReq contentReq) {
        MessageContext messageContext = new MessageContext();
        messageContext.setContentReq(contentReq);
        // 这里顺序很关键
        messageContext = checkUserMessageProcessor.process(messageContext);
        messageContext = addDialogParamProcessor.process(messageContext);
        messageContext = historyAppendProcessor.process(messageContext);
        messageContext = generationPromptProcessor.process(messageContext);
        messageContext = aiInvocationProcessor.process(messageContext);
        messageContext = saveHistoryProcessor.process(messageContext);
        messageContext = generationRespProcessor.process(messageContext);
        return messageContext.getContentResp();
    }
}
