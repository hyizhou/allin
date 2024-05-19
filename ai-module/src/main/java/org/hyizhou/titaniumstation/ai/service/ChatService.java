package org.hyizhou.titaniumstation.ai.service;

import org.hyizhou.titaniumstation.ai.processor.message.MessageProcessingCommand;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2024/5/17
 */
@Service
public class ChatService {
    private final MessageProcessingCommand messageProcessingCommand;
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService( MessageProcessingCommand messageProcessingCommand) {
        this.messageProcessingCommand = messageProcessingCommand;
    }

    /**
     * 聊天方法
     * @param req 请求参数
     * @return 聊天响应
     */
    @Transactional
    public ContentResp chat(ContentReq req){
        return messageProcessingCommand.execute(req);
    }

}
