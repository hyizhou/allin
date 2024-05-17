package org.hyizhou.titaniumstation.ai.processor.command;

import org.hyizhou.titaniumstation.ai.dao.MessageDao;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.processor.MessageProcessor;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 获取历史记录追加到消息中
 * @date 2024/5/17
 */
public class HistoryAppendProcessor implements MessageProcessor {
    private final MessageDao messageDao;

    public HistoryAppendProcessor(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public MessageContext process(MessageContext context) {
        List<MessageEntity> messageEntityList = messageDao.findAllByDialog(context.getDialog(), Sort.by(Sort.Direction.ASC, "timestamp"));
        context.setMessages(messageEntityList);
        return context;
    }
}
