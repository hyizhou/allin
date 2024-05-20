package org.hyizhou.titaniumstation.ai.processor.historyStrategy;

import lombok.extern.log4j.Log4j2;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.pojo.HistoryContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息条数限制的策略
 * @date 2024/5/18
 */
@Log4j2
public class MessageSizeStrategy implements HistoryStrategyBehavior {
    @Override
    public HistoryContext applyStrategy(HistoryContext context) {
        log.debug("调用 MessageSizeStrategy");
        Integer size = context.getStrategy().getMessageSize();
        if (size == null){
            log.debug("无需使用MessageSizeStrategy");
            return context;
        }
        size = size - context.getSystemMessageSize();
        List<MessageEntity> messagesCopy;
        if (context.getModifiedMessages() == null){
            messagesCopy = new ArrayList<>(context.getOriginalMessages());
        }else {
            messagesCopy = new ArrayList<>(context.getModifiedMessages());
        }

        if (messagesCopy.size() > size) {
            List<MessageEntity> subList = messagesCopy.subList(size-1, messagesCopy.size());
            if (!subList.isEmpty() && subList.get(0).getRole().equals("assistant")){
                log.debug("-------删除首位assistant消息------------");
                subList.remove(0);
            }
            context.setModifiedMessages(subList);
        }

        return context;
    }
}
