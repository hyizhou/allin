package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.pojo.MessageContext;

/**
 * 判断上一条消息是否处理完毕，只有处理完毕才允许继续，否则会弹出错误提示
 * @date 2024/5/18
 */
public class BacklogAvoidanceProcessor implements MessageProcessor{
    @Override
    public MessageContext process(MessageContext context) {
        return context;
    }
}
