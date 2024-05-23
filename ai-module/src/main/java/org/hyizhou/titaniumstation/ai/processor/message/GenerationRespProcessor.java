package org.hyizhou.titaniumstation.ai.processor.message;

import lombok.extern.log4j.Log4j2;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.stereotype.Component;

/**
 * 生成响应对象处理器
 * @date 2024/5/17
 */
@Component
@Log4j2
public class GenerationRespProcessor implements MessageProcessor {
    @Override
    public MessageContext process(MessageContext context) {
        ChatResponse chatResponse = context.getChatResponse();
        if (log.isDebugEnabled()){
            log.debug(ModelOptionsUtils.toJsonString(chatResponse));
        }
        ContentResp resp = new ContentResp(
                chatResponse.getResult().getOutput().getContent(),
                "text",
                MessageType.ASSISTANT.getValue(),
               (String) chatResponse.getResult().getOutput().getProperties().get("id"),  // 注意，这里升级 Spring AI 到 1.0.0 后会有所改变
                chatResponse.getResult().getMetadata().getFinishReason(),
                chatResponse.getMetadata().getUsage().getPromptTokens(),
                chatResponse.getMetadata().getUsage().getGenerationTokens()
        );
        context.setContentResp(resp);
        if (context.isAppend()) {
            steamProcess(context);
        }
        return context;
    }

    /**
     * 流式情况下的额外处理
     */
    private void steamProcess(MessageContext context) {
        MessageEntity respEntity = context.getRespMessageEntity();
        if (respEntity !=  null) {
            // 若已存在响应实体，则追加内容
            respEntity.setContent(respEntity.getContent() + context.getContentResp().content());
        }else {
            // 若不存在则添加创建实体
            ContentResp resp = context.getContentResp();
            respEntity = new MessageEntity(
                    resp.id(),
                    context.getDialog(),
                    null,
                    resp.content(),
                    context.getRespTime(),
                    resp.role(),
                    resp.type()
            );
        }
        context.setRespMessageEntity(respEntity);
    }
}
