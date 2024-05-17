package org.hyizhou.titaniumstation.ai.service;

import org.hyizhou.titaniumstation.ai.dao.DialogDao;
import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.common.ai.model.CreationDialogModel;
import org.hyizhou.titaniumstation.common.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 对话服务，用户与大模型连续的聊天称为对话，对此进行管理。
 * @date 2024/5/16
 */
@Service
public class DialogService {
    private final DialogDao dialogDao;

    public DialogService(DialogDao dialogDao) {
        this.dialogDao = dialogDao;
    }

    public String create(CreationDialogModel model){
        DialogEntity entity = new DialogEntity();
        entity.setUser(new UserEntity(1, "test"));
        entity.setStartTime(LocalDateTime.now());
        entity.setModel(model.model());
        entity.setServiceProvider(model.serviceProvider());
        entity = dialogDao.save(entity);

        return entity.getDialogId();
    }
}
