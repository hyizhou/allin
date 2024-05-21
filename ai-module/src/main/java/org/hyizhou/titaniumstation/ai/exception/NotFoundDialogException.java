package org.hyizhou.titaniumstation.ai.exception;

/**
 * 未找到对话的异常
 * @date 2024/5/17
 */
public class NotFoundDialogException extends RuntimeException{
    public NotFoundDialogException(String dialogId) {
        super("此会话ID不存在或无权操作 " + dialogId);
    }
}
