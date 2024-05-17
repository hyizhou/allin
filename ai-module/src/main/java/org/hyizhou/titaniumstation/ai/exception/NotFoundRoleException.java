package org.hyizhou.titaniumstation.ai.exception;

/**
 * 未找到目标角色异常
 * TODO 记得接口处理异常
 * @author hyizhou
 * @date 2024/5/15
 */
public class NotFoundRoleException extends RuntimeException{
    public NotFoundRoleException(String role) {
        super("Role意外值:"+role);
    }
}
