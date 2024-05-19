package ai.dao;

import org.hyizhou.titaniumstation.AiModelApplication;
import org.hyizhou.titaniumstation.ai.dao.DialogDao;
import org.hyizhou.titaniumstation.ai.dao.MessageDao;
import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.entity.MessageEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @date 2024/5/19
 */
@SpringBootTest(classes = AiModelApplication.class)
public class MessageDaoTest {
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private DialogDao dialogDao;

    @Test
    public void test(){
        DialogEntity dialog = dialogDao.findById("a127f666-1d73-4a39-bb28-fcdebcd814e1").get();
        List<MessageEntity> messages = messageDao.findMessageAfterLastSummary(dialog);
        for (MessageEntity message : messages) {
            System.out.println(message.getContent());
            System.out.println("--------------------------------");
        }
    }
}
