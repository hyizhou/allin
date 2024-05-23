package org.hyizhou.titaniumstation.ai.service;

import org.hyizhou.titaniumstation.ai.dao.DialogDao;
import org.hyizhou.titaniumstation.ai.exception.BusyDialogException;
import org.hyizhou.titaniumstation.ai.pojo.MessageContext;
import org.hyizhou.titaniumstation.ai.processor.message.FluxMessageProcessingCommand;
import org.hyizhou.titaniumstation.ai.processor.message.MessageProcessingCommand;
import org.hyizhou.titaniumstation.ai.processor.message.SetNotBusyProcessor;
import org.hyizhou.titaniumstation.common.ai.request.ContentReq;
import org.hyizhou.titaniumstation.common.ai.response.ContentResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @date 2024/5/17
 */
@Service
public class ChatService {
    private final MessageProcessingCommand messageProcessingCommand;
    private final FluxMessageProcessingCommand fluxMessageProcessingCommand;
    private final SetNotBusyProcessor setNotBusyProcessor;
    private final DialogDao dialogDao;
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(MessageProcessingCommand messageProcessingCommand, FluxMessageProcessingCommand fluxMessageProcessingCommand, SetNotBusyProcessor setNotBusyProcessor, DialogDao dialogDao) {
        this.messageProcessingCommand = messageProcessingCommand;
        this.fluxMessageProcessingCommand = fluxMessageProcessingCommand;
        this.setNotBusyProcessor = setNotBusyProcessor;
        this.dialogDao = dialogDao;
    }

    /**
     * 聊天方法
     *
     * @param req 请求参数
     * @return 聊天响应
     */
    public ContentResp chat(ContentReq req) {
        logger.debug("成功调用chat方法");
        try {
            return messageProcessingCommand.execute(req);
        } catch (BusyDialogException e) {
            // 因为对话繁忙弹出的业务逻辑，不应解除状态
            throw e;
        } catch (Exception e) {
            MessageContext context = new MessageContext();
            context.setContentReq(req);
            // 出现异常中断，转换繁忙状态，以免影响后续消息
            setNotBusyProcessor.process(context);
            throw e;
        }

    }


    public Flux<ContentResp> streamChat(ContentReq req) {
        // 异常已通过响应式处理
        try {
            return fluxMessageProcessingCommand.execute(req);
        }catch (BusyDialogException e) {
            throw e;
        }
        catch (Exception e){
            MessageContext context = new MessageContext();
            context.setContentReq(req);
            // 出现异常中断，转换繁忙状态，以免影响后续消息
            setNotBusyProcessor.process(context);
            throw e;
        }
    }


}
