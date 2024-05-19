package org.hyizhou.titaniumstation.ai.processor.historyStrategy;

import lombok.extern.log4j.Log4j2;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.pojo.HistoryContext;
import org.hyizhou.titaniumstation.ai.tools.TokenTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 按照token大小进行限制的策略
 * @date 2024/5/18
 */
@Log4j2
public class TokenSizeStrategy implements HistoryStrategyBehavior {

    @Override
    public HistoryContext applyStrategy(HistoryContext context) {
        Long tokenSize = context.getStrategy().getTokenSize();
        log.debug("调用 TokenSizeStrategy");
        if (tokenSize == null){
            log.debug("无需调用 TokenSizeStrategy");
            return context;
        }
        long totalToken = 0L;
        List<MessageEntity> result = new ArrayList<>();
        List<MessageEntity> messagesCopy;
        if (context.getModifiedMessages() == null){
            messagesCopy = context.getOriginalMessages();
        }else {
            messagesCopy = context.getModifiedMessages();
        }

        for (int i = messagesCopy.size() - 1; i >= 0; i--) {
            MessageEntity message = messagesCopy.get(i);
            long currentToken = TokenTools.simpleCount(message.getContent());

            // 检查加入当前消息后是否会超过tokenSize限制
            if (totalToken + currentToken <= tokenSize) {
                totalToken += currentToken;
                // 将符合条件的消息添加到结果列表的开头，以保持收集顺序与遍历一致（反向）
                result.add(0, message);
            } else {
                // 达到tokenSize限制，停止遍历
                break;
            }
        }
        if (!result.isEmpty() && result.get(0).getRole().equals("assistant")){
            log.debug("-------删除首位assistant消息------------");
            result.remove(0);
        }
        context.setModifiedMessages(result);
        return context;
    }
}
