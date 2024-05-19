package org.hyizhou.titaniumstation.ai.processor.message;

import org.hyizhou.titaniumstation.ai.dao.DialogDao;
import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 添加会话参数处理器
 * @date 2024/5/17
 */
@Component
public class AddDialogParamProcessor implements MessageProcessor {
    private final DialogDao dialogDao;

    public AddDialogParamProcessor(DialogDao dialogDao) {
        this.dialogDao = dialogDao;
    }

    @Override
    public MessageContext process(MessageContext context) {
        Optional<DialogEntity> dialogOptional = dialogDao.findById(context.getContentReq().dialogId());
        if (dialogOptional.isPresent()){
            DialogEntity dialog = dialogOptional.get();
            context.setDialog(dialog);
        } else {
            throw new RuntimeException("对话ID不存在");
        }
        return context;
    }
}
