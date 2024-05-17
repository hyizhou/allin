package org.hyizhou.titaniumstation.ai.processor.imp;

import org.hyizhou.titaniumstation.ai.dao.MessageDao;
import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.hyizhou.titaniumstation.ai.processor.HistoryRepositoryTypeEntity;
import org.hyizhou.titaniumstation.ai.tools.CurrentDialogContext;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 存储于数据库中的消息历史仓库
 * @date 2024/5/17
 */
@Component
public class DbHistoryRepository implements HistoryRepositoryTypeEntity {
    private final MessageDao messageDao;

    public DbHistoryRepository(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public void save(MessageEntity message) {
        messageDao.save(message);
    }

    @Override
    @Transactional
    public List<MessageEntity> append(MessageEntity message) {
        messageDao.save(message);
        return messageDao.findAllByDialog(message.getDialog(), Sort.by(Sort.Direction.ASC, "sequence"));
    }

    @Override
    @Transactional
    public List<MessageEntity> appendAll(List<MessageEntity> messages) {
        messageDao.saveAll(messages);
        return messageDao.findAllByDialog(messages.get(0).getDialog(), Sort.by(Sort.Direction.ASC, "timestamp"));
    }

    @Override
    @Transactional
    public Optional<MessageEntity> pop() {
        DialogEntity dialog = CurrentDialogContext.get();
        Optional<MessageEntity> optionalMessage = messageDao.findLatestByDialog(dialog);
        if (optionalMessage.isPresent()) {
            messageDao.delete(optionalMessage.get());
            return optionalMessage;
        }
        return optionalMessage;
    }

    @Override
    @Transactional
    public Optional<MessageEntity> findMessage(String id) {
        return messageDao.findById(id);
    }
}
