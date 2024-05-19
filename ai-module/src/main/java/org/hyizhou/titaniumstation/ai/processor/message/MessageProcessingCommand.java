package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;

/**
 * 处理对话消息的命令模式接口
 * @date 2024/5/17
 */
public interface MessageProcessingCommand {
    ContentResp execute(ContentReq contentReq);
}
