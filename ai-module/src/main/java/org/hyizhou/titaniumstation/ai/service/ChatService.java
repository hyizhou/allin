package org.hyizhou.titaniumstation.ai.service;

import org.hyizhou.titaniumstation.ai.dao.DialogDao;
import org.hyizhou.titaniumstation.ai.dao.MessageDao;
import org.hyizhou.titaniumstation.ai.entity.DialogEntity;
import org.hyizhou.titaniumstation.ai.exception.NotFoundDialogException;
import org.hyizhou.titaniumstation.ai.processor.ChatRespProcessorChain;
import org.hyizhou.titaniumstation.ai.processor.PromptProcessorChain;
import org.hyizhou.titaniumstation.ai.qwen.QwenChatClient;
import org.hyizhou.titaniumstation.ai.tools.CurrentDialogContext;
import org.hyizhou.titaniumstation.ai.tools.PromptTools;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;
import org.hyizhou.titaniumstation.common.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

/**
 * @date 2024/5/17
 */
@Service
public class ChatService {
    private final MessageDao messageDao;
    private final UserService userService;
    private final DialogDao dialogDao;
    private final PromptProcessorChain promptProcessorChain;
    private final ApplicationContext applicationContext;
    private final ChatRespProcessorChain chatRespProcessorChain;
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(MessageDao messageDao, UserService userService, DialogDao dialogDao,
                       PromptProcessorChain promptProcessorChain, ApplicationContext applicationContext,
                       ChatRespProcessorChain chatRespProcessorChain) {
        this.messageDao = messageDao;
        this.userService = userService;
        this.dialogDao = dialogDao;
        this.promptProcessorChain = promptProcessorChain;
        this.applicationContext = applicationContext;
        this.chatRespProcessorChain = chatRespProcessorChain;
    }

    /**
     * 聊天方法
     * @param req 请求参数
     * @return 聊天响应
     */
    @Transactional
    public ContentResp chat(ContentReq req){
        UserEntity user = userService.getCurrentUser();
        // 1. 验证会话id是否存在与此用户的
        if (!dialogDao.existsByDialogIdAndUserId(req.dialogId(), user.getId())){
            throw new NotFoundDialogException(req.dialogId());
        }
        // 2. 验证通过，则提取出当前消息内容和类型
        String content = req.content();
        String type = req.type();
        // 3. 根据消息类型进行不同处理，比如text类型不处理，image、audio等进行base64解码
        if (!"text".equals(type)){
            throw new RuntimeException("暂未支持更多类型，必须设置：type=text");
        }
        // 4. 通过会话id与用户id查询数据库，得到服务商、模型等设置信息
        DialogEntity dialog = dialogDao.findById(req.dialogId()).get();
        CurrentDialogContext.set(dialog);
        // 5. 将消息内容、会话设置等包装成Prompt类，并进行链式加工
        Prompt prompt = PromptTools.generatePrompt(req, dialog);
        prompt = promptProcessorChain.process(prompt);
        logger.debug("prompt: {}", prompt);
        // 6. 给大语言模型发起对话
        ChatClient chatClient = getClientByClient(dialog.getServiceProvider());
        ChatResponse chatResponse = chatClient.call(prompt);
        chatResponse = chatRespProcessorChain.process(chatResponse);
        // 7. 将大语言模型对话响应解析并包装成ContentResp格式，并返回
        return convertChatResponseToContentResp(chatResponse);

    }

    public Flux<ContentResp> stream(ContentReq req){
        return Flux.empty();
    }

    private ChatClient getClientByClient(String client) {
        if ("qwen".equals(client)) {
            return applicationContext.getBean(QwenChatClient.class);
        }
        return applicationContext.getBean(OpenAiChatClient.class);
    }

    private ContentResp convertChatResponseToContentResp(ChatResponse chatResponse) {
        return new ContentResp(
                chatResponse.getResult().getOutput().getContent(),
                "text",
                MessageType.ASSISTANT.getValue(),
                null,  // 对话id先空着，因为现有的代码结构没法将此值取回来
                chatResponse.getResult().getMetadata().getFinishReason(),
                chatResponse.getMetadata().getUsage().getPromptTokens(),
                chatResponse.getMetadata().getUsage().getGenerationTokens()
        );


    }
}
