package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.dao.MessageDao;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 保存对话到历史记录中 <br>
 * 对数据库保存操作应皆于此类，以避免事务问题
 * @date 2024/5/17
 */
@Component
public class SaveHistoryProcessor implements MessageProcessor {
    private final MessageDao messageDao;

    public SaveHistoryProcessor(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    /**
     * 持久化三个信息：
     * 1. 用户发送消息
     * 2. AI响应的消息
     * 3. 历史对话总结的消息（若存在）
     */
    @Override
    @Transactional
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
        saveSummary(context);
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

    /**
     * 保存历史总结内容到数据库
     */
    private void saveSummary(MessageContext context){
        if (context.getSummaryEntity() != null){
            // 总结的时间戳将早于最后一条消息的时间戳，以便能在后续对话中正确获取
            Duration duration = Duration.ofMillis(100);
            MessageEntity summaryEntity = context.getSummaryEntity();
            LocalDateTime theTime;
            // 这里判断消息列表是否只有总结一元素，若是如此则表示所有对话都被总结，总结需要放在最新的消息位置
            if (context.getHistoryMessages().size() > 1){
                theTime = context.getHistoryMessages().get(1).getTimestamp().minus(duration);
            }else {
                theTime = context.getReqTime().minus(duration);
            }
            summaryEntity.setTimestamp(theTime);
            summaryEntity.setDialog(context.getDialog());
            summaryEntity.setIsSummary(true);
            messageDao.save(summaryEntity);
        }
    }
}
