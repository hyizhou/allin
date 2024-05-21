package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.dao.DialogDao;
import org.hyizhou.titaniumstation.ai.exception.NotFoundDialogException;
import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.service.UserService;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 检查用户消息的处理器
 * @date 2024/5/17
 */
@Component
public class CheckUserMessageProcessor implements MessageProcessor {
    private final DialogDao dialogDao;
    private final UserService userService;

    public CheckUserMessageProcessor(DialogDao dialogDao, UserService userService) {
        this.dialogDao = dialogDao;
        this.userService = userService;
    }


    @Override
    public MessageContext process(MessageContext context) {
        UserEntity user = userService.getCurrentUser();
        ContentReq req = context.getContentReq();
        // 1. 验证会话id是否存在与此用户的
        if (!dialogDao.existsByDialogIdAndUserId(req.getDialogId(), user.getId())){
            throw new NotFoundDialogException(req.getDialogId());
        }
        context.setReqTime(LocalDateTime.now());
        return context;
    }
}
