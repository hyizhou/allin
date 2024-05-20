package org.hyizhou.titaniumstation.ai.exception;

/**
 * 未找到目标角色异常
 * @author hyizhou
 * @date 2024/5/15
 */
public class NotFoundRoleException extends RuntimeException{
    public NotFoundRoleException(String role) {
        super("Role意外值:"+role);
    }
}
