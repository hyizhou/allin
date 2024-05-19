package org.hyizhou.titaniumstation.ai.exception;

/**
 * 这个异常不要被Mvc的异常处理捕捉，这是开发提示的，爆出这个异常一般表示代码有问题了
 * @date 2024/5/18
 */
public class AiSystemException extends RuntimeException{

    public AiSystemException(String message) {
        super(message);
    }
}
