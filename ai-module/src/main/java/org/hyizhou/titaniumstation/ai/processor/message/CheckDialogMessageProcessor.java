package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.dao.DialogDao;
import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.exception.BusyDialogException;
import org.hyizhou.titaniumstation.ai.exception.NotFoundDialogException;
import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.service.UserService;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 检查用户消息的处理器
 * @date 2024/5/17
 */
@Component
public class CheckDialogMessageProcessor implements MessageProcessor {
    private final DialogDao dialogDao;
    private final UserService userService;

    public CheckDialogMessageProcessor(DialogDao dialogDao, UserService userService) {
        this.dialogDao = dialogDao;
        this.userService = userService;
    }


    @Override
    @Transactional
    public MessageContext process(MessageContext context) {
        UserEntity user = userService.getCurrentUser();
        ContentReq req = context.getContentReq();
        // 检查用户是否拥有此会话
        Optional<DialogEntity> dialogEntityOptional = dialogDao.findByDialogIdAndUser(req.getDialogId(), user);
        if (dialogEntityOptional.isEmpty()){
            throw new NotFoundDialogException(req.getDialogId());
        }

        DialogEntity dialog = dialogEntityOptional.get();
        // 检查会话是否繁忙
        if (dialog.getIsBusy()) {
            throw new BusyDialogException(dialog.getDialogId());
        }
        dialog.setIsBusy(true);
        // 将会话标记为繁忙
        dialogDao.save(dialog);
        context.setReqTime(LocalDateTime.now());
        context.setDialog(dialog);
        return context;
    }
}
