package org.hyizhou.titaniumstation.ai.exception;

/**
 * 表会话正在忙的异常
 * @date 2024/5/22
 */
public class BusyDialogException extends RuntimeException{
    public BusyDialogException(String dialogId) {
        super("对话会话[" + dialogId + "]一次只允许发送一条消息，请稍后再试");
    }
}
