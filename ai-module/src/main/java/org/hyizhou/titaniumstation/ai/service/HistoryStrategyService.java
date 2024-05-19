package org.hyizhou.titaniumstation.ai.service;

import org.hyizhou.titaniumstation.ai.config.properties.Constants;
import org.hyizhou.titaniumstation.ai.dao.MessageDao;
import org.hyizhou.titaniumstation.ai.dao.UserAiConfigDao;
import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.entity.HistoryStrategyEntity;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.entity.UserAiConfigEntity;
import org.hyizhou.titaniumstation.ai.pojo.HistoryContext;
import org.hyizhou.titaniumstation.ai.processor.historyStrategy.HistoryStrategyBehavior;
import org.hyizhou.titaniumstation.common.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 对历史传递策略进行一些操作的服务
 * @date 2024/5/16
 */
@Service
public class HistoryStrategyService {
    private final UserAiConfigDao userAiConfigDao;
    private final HistoryStrategyBehavior CompositeHistoryStrategy;
    private final MessageDao messageDao;
    private final Logger logger = LoggerFactory.getLogger(HistoryStrategyService.class);


    public HistoryStrategyService(UserAiConfigDao userAiConfigDao,
                                  HistoryStrategyBehavior compositeHistoryStrategy,
                                  MessageDao messageDao) {
        this.userAiConfigDao = userAiConfigDao;
        this.CompositeHistoryStrategy = compositeHistoryStrategy;
        this.messageDao = messageDao;
    }

    /**
     * 应用策略，返回处理后的历史消息
     *  TODO 1. system 角色消息过滤
     * @param dialog 会话实体类，通过其信息找到对应的策略和历史消息
     * @return 专用于策略处理的封装类，处理结果从中获取
     */
    public HistoryContext applyStrategy(DialogEntity dialog) {
        HistoryStrategyEntity option = this.getStrategyOption(dialog);
        HistoryContext context = this.preprocessContext(option, dialog);
        return CompositeHistoryStrategy.applyStrategy(context);
    }


    private HistoryStrategyEntity getStrategyOption(DialogEntity entity) {
        HistoryStrategyEntity strategy = entity.getHistoryStrategy();
        // 若对话没有配置 历史记录传递策略， 则使用 用户默认的策略。
        if (strategy == null) {
            UserEntity user = entity.getUser();
            Optional<UserAiConfigEntity> userAiConfig = userAiConfigDao.findByUser(user);
            strategy = userAiConfig.map(UserAiConfigEntity::getHistoryStrategy).orElse(null);
        }
        // 用户策略也不存在的化，那就用系统默认策略
        if (strategy == null){
            strategy = Constants.GLOBAL_HISTORY_STRATEGY;
        }
        return strategy;
    }

    /**
     * 预生成 Context 以便后续处理
     * @param strategy 策略配置
     * @return 预处理后的 Context
     */
    private HistoryContext preprocessContext(HistoryStrategyEntity strategy, DialogEntity dialog){
        logger.debug("dialog: {}", dialog.getDialogId());
        HistoryContext context = new HistoryContext();
        context.setStrategy(strategy);
        List<MessageEntity> originalMessages;
        // 到策略关闭、总结关闭时，排除总结消息并获取全部
        if (strategy.isClose() || strategy.getSummaryRule() == null){
            originalMessages = messageDao.findMessageNotHaveSummary(dialog);
        }else {
            originalMessages = messageDao.findMessageAfterLastSummary(dialog);
        }
        for (MessageEntity message : originalMessages) {
            logger.debug("{}:{}", message.getRole(), message.getContent());
        }
        context.setOriginalMessages(originalMessages);
        context.setModifiedMessages(originalMessages);
        return context;
    }
}
