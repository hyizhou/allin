package org.hyizhou.titaniumstation.ai.processor.command;

import org.hyizhou.titaniumstation.ai.dao.MessageDao;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.processor.MessageProcessor;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;

/**
 * 保存对话到历史记录中
 * @date 2024/5/17
 */
public class SaveHistoryProcessor implements MessageProcessor {
    private final MessageDao messageDao;

    public SaveHistoryProcessor(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public MessageContext process(MessageContext context) {
        ContentReq req = context.getContentReq();
        ContentResp resp = context.getContentResp();
        MessageEntity reqMsgEntity = new MessageEntity(
                null,
                context.getDialog(),
                null,
                req.content(),
                context.getReqTime(),
                req.role(),
                req.type()
        );
        MessageEntity respMsgEntity = new MessageEntity(
                null,
                context.getDialog(),
                null,
                resp.content(),
                context.getRespTime(),
                resp.role(),
                resp.type()
        );
        reqMsgEntity = messageDao.save(reqMsgEntity);
        respMsgEntity = messageDao.save(respMsgEntity);
        // 最后将响应消息的id更新到resp上
        context.setContentResp(updateContentResp(respMsgEntity.getMessageId(), resp));
        return context;
    }

    private ContentResp updateContentResp(String messageId, ContentResp resp){
        return new ContentResp(
                resp.content(),
                resp.type(),
                resp.role(),
                messageId,
                resp.finishReason(),
                resp.promptTokens(),
                resp.generationTokens()
        );
    }
}
