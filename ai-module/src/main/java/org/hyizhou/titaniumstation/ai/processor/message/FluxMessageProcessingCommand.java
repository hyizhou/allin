package org.hyizhou.titaniumstation.ai.processor.message;

import lombok.extern.log4j.Log4j2;
import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @date 2024/5/22
 */
@Component
@Log4j2
public class FluxMessageProcessingCommand {
    private final CheckDialogMessageProcessor checkDialogMessageProcessor;
    private final GenerationPromptProcessor generationPromptProcessor;
    private final AIInvocationFluxProcessor aiInvocationFluxProcessor;
    private final SaveHistoryProcessor saveHistoryProcessor;
    private final GenerationRespProcessor generationRespProcessor;
    private final HistoryAppendProcessor historyAppendProcessor;
    private final SetNotBusyProcessor setNotBusyProcessor;

    public FluxMessageProcessingCommand(CheckDialogMessageProcessor checkDialogMessageProcessor, GenerationPromptProcessor generationPromptProcessor, AIInvocationFluxProcessor aiInvocationFluxProcessor, SaveHistoryProcessor saveHistoryProcessor, GenerationRespProcessor generationRespProcessor, HistoryAppendProcessor historyAppendProcessor, SetNotBusyProcessor setNotBusyProcessor) {
        this.checkDialogMessageProcessor = checkDialogMessageProcessor;
        this.generationPromptProcessor = generationPromptProcessor;
        this.aiInvocationFluxProcessor = aiInvocationFluxProcessor;
        this.saveHistoryProcessor = saveHistoryProcessor;
        this.generationRespProcessor = generationRespProcessor;
        this.historyAppendProcessor = historyAppendProcessor;
        this.setNotBusyProcessor = setNotBusyProcessor;
    }

    public Flux<ContentResp> execute(ContentReq contentReq) {
        MessageContext messageContext = new MessageContext();
        MessageContext context = new MessageContext();
        context.setAppend(true);
        messageContext.setContentReq(contentReq);
        checkDialogMessageProcessor.process(messageContext);
        historyAppendProcessor.process(messageContext);
        generationPromptProcessor.process(messageContext);
        aiInvocationFluxProcessor.process(messageContext);

        context.setDialog(messageContext.getDialog());
        context.setReqTime(messageContext.getReqTime());
        context.setRespTime(messageContext.getRespTime());
        context.setContentReq(messageContext.getContentReq());
        context.setSummaryEntity(messageContext.getSummaryEntity());

        Flux<ChatResponse> flux = messageContext.getFluxChatResponse();
        return flux.map(e -> {
                    context.setChatResponse(e);
                    generationRespProcessor.process(context);
                    return context.getContentResp();
                })
                .doOnComplete(() -> {
                    saveHistoryProcessor.process(context);
                })
                .onErrorResume(throwable -> {
                    log.error("Flux出现异常", throwable);
                    return Flux.error(throwable);
                })
                .doFinally(signalType -> {
                    setNotBusyProcessor.process(context);
                });
    }
}
