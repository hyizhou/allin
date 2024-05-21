package org.hyizhou.titaniumstation.ai.exception;

/**
 * 无权限或业务限制产生的异常
 * @date 2024/5/20
 */
public class UnauthorizedOperationException extends RuntimeException{
    public UnauthorizedOperationException(String message)
    {
        super(message);
    }
}
