package org.hyizhou.titaniumstation.ai.service;

import org.hyizhou.titaniumstation.ai.dao.DialogDao;
import org.hyizhou.titaniumstation.ai.dao.HistoryStrategyDao;
import org.hyizhou.titaniumstation.ai.dao.MessageDao;
import org.hyizhou.titaniumstation.ai.dao.UserAiConfigDao;
import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.entity.HistoryStrategyEntity;
import org.hyizhou.titaniumstation.ai.exception.NotFoundDialogException;
import org.hyizhou.titaniumstation.ai.exception.UnauthorizedOperationException;
import org.hyizhou.titaniumstation.common.ai.request.CreationDialogReq;
import org.hyizhou.titaniumstation.common.ai.request.UpdateDialogReq;
import org.hyizhou.titaniumstation.common.ai.response.DialogInfoResp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 对话服务，用户与大模型连续的聊天称为对话，对此进行管理。
 * @date 2024/5/16
 */
@Service
public class DialogService {
    private final DialogDao dialogDao;
    private final UserService userService;
    private final HistoryStrategyDao historyStrategyDao;
    private final MessageDao messageDao;
    private final UserAiConfigDao userAiConfigDao;

    public DialogService(DialogDao dialogDao, UserService userService, HistoryStrategyDao historyStrategyDao, MessageDao messageDao, UserAiConfigDao userAiConfigDao) {
        this.dialogDao = dialogDao;
        this.userService = userService;
        this.historyStrategyDao = historyStrategyDao;
        this.messageDao = messageDao;
        this.userAiConfigDao = userAiConfigDao;
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
     * 删除一个对话 <br>
     * 需要讲究顺序，先删除所有对话消息、再删除对话、再删除历史策略
     * @param dialogId 对话id
     */
    @Transactional
    public void delete(String dialogId){
        Optional<DialogEntity> dialogOptional = dialogDao.findByDialogIdAndUser(dialogId, userService.getCurrentUser());
        if (dialogOptional.isEmpty()){
            throw new NotFoundDialogException(dialogId);
        }

        DialogEntity dialog = dialogOptional.get();
        if (userAiConfigDao.existsByDialog(dialog)){
            throw new UnauthorizedOperationException("该会话为用户默认会话，禁止删除");
        }
        messageDao.deleteByDialog(dialog);
        dialogDao.delete(dialog);

    }

    /**
     * 查询用户当前所有对话
     */
    public void findAll(){
        dialogDao.findAll();
    }

    /**
     * 获取一个会话的信息
     * @param dialogId 对话id
     * @return 对话信息
     */
    public DialogInfoResp getDialogInfo(String dialogId){
        Optional<DialogEntity> dialogOptional = dialogDao.findByDialogIdAndUser(dialogId, userService.getCurrentUser());
        if (dialogOptional.isEmpty()){
            throw new NotFoundDialogException(dialogId);
        }
        DialogEntity dialog = dialogOptional.get();
        return new DialogInfoResp(
                dialogId,
                dialog.getStartTime(),
                dialog.getModel(),
                dialog.getServiceProvider(),
                dialog.getDialogsSummary(),
                generateRespHistoryStrategy(dialog)
        );
    }

    /**
     * 更新会话信息，注意还涉及了 HistoryStrategyEntity，请确保级联关系正确才能持久化到数据库
     */
    @Transactional
    public void updateDialogInfo(String dialogId, UpdateDialogReq model){
        Optional<DialogEntity> dialogOptional = dialogDao.findByDialogIdAndUser(dialogId, userService.getCurrentUser());
        if (dialogOptional.isEmpty()){
            throw new NotFoundDialogException(dialogId);
        }
        DialogEntity dialog = dialogOptional.get();
        if (model.model() != null){
            dialog.setModel(model.model());
        }
        if (model.serviceProvider() != null){
            dialog.setServiceProvider(model.serviceProvider());
        }
        if (model.removeHistoryStrategy() != null && model.removeHistoryStrategy()){
            safeRemoveHistoryStrategy(dialog);
        }else {
            HistoryStrategyEntity historyStrategy = updateHistoryStrategy(model, dialog);
            dialog.setHistoryStrategy(historyStrategy);
        }
        dialogDao.save(dialog);

    }

    private static HistoryStrategyEntity updateHistoryStrategy(UpdateDialogReq model, DialogEntity dialog) {

        HistoryStrategyEntity historyStrategy = dialog.getHistoryStrategy();
        if (model.historyStrategy() == null){
            return historyStrategy;
        }
        if (historyStrategy == null){
            historyStrategy = new HistoryStrategyEntity();
        }
        UpdateDialogReq.HistoryStrategy modelStrategy = model.historyStrategy();
        if (modelStrategy.isClose() != null){
            historyStrategy.setClose(modelStrategy.isClose());
        }
        if (modelStrategy.tokenLimit() != null) {
            historyStrategy.setTokenSize(modelStrategy.tokenLimit());
        }
        if (modelStrategy.messageLimit() != null) {
            historyStrategy.setMessageSize(modelStrategy.messageLimit());
        }
        if (modelStrategy.enableSummary() != null) {
            historyStrategy.setSummaryRule(HistoryStrategyEntity.SummaryRule.DIALOG_SUMMARY);
        }
        return historyStrategy;
    }


    private static DialogInfoResp.HistoryStrategy generateRespHistoryStrategy(DialogEntity dialog) {
        HistoryStrategyEntity historyStrategy = dialog.getHistoryStrategy();
        DialogInfoResp.HistoryStrategy strategyResp;
        if (historyStrategy == null){
            strategyResp = null;
        }else {
            strategyResp = new DialogInfoResp.HistoryStrategy(
                    historyStrategy.isClose(),
                    historyStrategy.getTokenSize(),
                    historyStrategy.getMessageSize(),
                    historyStrategy.getSummaryRule() != null
            );
        }
        return strategyResp;
    }


    /**
     * 安全删除会话对应的历史策略
     */
    private void safeRemoveHistoryStrategy(DialogEntity dialog){
        HistoryStrategyEntity historyStrategy = dialog.getHistoryStrategy();
        if (historyStrategy != null){
            historyStrategyDao.delete(historyStrategy);
            dialog.setHistoryStrategy(null);
        }
    }

}
