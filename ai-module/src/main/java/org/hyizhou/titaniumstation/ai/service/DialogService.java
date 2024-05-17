package org.hyizhou.titaniumstation.ai.service;

import org.hyizhou.titaniumstation.ai.dao.DialogDao;
import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.common.ai.request.CreationDialogReq;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 对话服务，用户与大模型连续的聊天称为对话，对此进行管理。
 * @date 2024/5/16
 */
@Service
public class DialogService {
    private final DialogDao dialogDao;
    private final UserService userService;

    public DialogService(DialogDao dialogDao, UserService userService) {
        this.dialogDao = dialogDao;
        this.userService = userService;
    }

    /**
     * 创建一个对话
     * @param model 前端创建对话的model对象
     * @return 对话id
     */
    public String create(CreationDialogReq model){
        DialogEntity entity = new DialogEntity();
        entity.setUser(userService.getCurrentUser());
        entity.setStartTime(LocalDateTime.now());
        entity.setModel(model.model());
        entity.setServiceProvider(model.serviceProvider());
        entity = dialogDao.save(entity);
        return entity.getDialogId();
    }

    /**
     * 删除一个对话
     * @param dialogId 对话id
     */
    public void delete(String dialogId){
        dialogDao.deleteById(dialogId);
    }

    /**
     * 查询用户当前所有对话
     */
    public void findAll(){
        dialogDao.findAll();
    }
}
