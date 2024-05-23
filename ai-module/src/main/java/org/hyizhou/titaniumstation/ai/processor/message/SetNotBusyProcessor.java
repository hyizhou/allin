package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.dao.DialogDao;
import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.exception.NotFoundDialogException;
import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.service.UserService;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 将对话设置为非繁忙状态
 * @date 2024/5/22
 */
@Component
public class SetNotBusyProcessor implements MessageProcessor{
    private final DialogDao dialogDao;
    private final UserService userService;

    public SetNotBusyProcessor(DialogDao dialogDao, UserService userService) {
        this.dialogDao = dialogDao;
        this.userService = userService;
    }

    @Override
    @Transactional
    public MessageContext process(MessageContext context) {
        DialogEntity dialog = context.getDialog();
        if (dialog != null) {
            context.getDialog().setIsBusy(false);
            dialogDao.save(context.getDialog());
            return context;
        }
        // 这里尝试从数据库获取会话，因为此处理器可能用在别处
        ContentReq req = context.getContentReq();
        if (req == null) {
            throw new RuntimeException("ContentReq 与 DialogEntity 都为空，如何将会话设置为非繁忙状态");
        }
        UserEntity user = userService.getCurrentUser();
        Optional<DialogEntity> dialogEntityOptional = dialogDao.findByDialogIdAndUser(context.getContentReq().getDialogId(), user);
        if (dialogEntityOptional.isEmpty()) {
            throw new NotFoundDialogException(req.getDialogId());
        }
        dialog = dialogEntityOptional.get();
        dialog.setIsBusy(false);
        dialogDao.save(dialog);
        return context;
    }
}
