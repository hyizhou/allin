package org.hyizhou.titaniumstation.ai.config;

import org.hyizhou.titaniumstation.ai.exception.NotFoundDialogException;
import org.hyizhou.titaniumstation.ai.exception.NotFoundRoleException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理
 * @date 2024/5/17
 */
@RestControllerAdvice
public class MvcExceptionConfiguration {

    /**
     * 处理NotFoundRoleException异常
     * @param ex NotFoundRoleException 异常对象
     * @return 返回的提示
     */
    @ExceptionHandler(NotFoundRoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundRoleException(NotFoundRoleException ex){
        return ex.getLocalizedMessage();
    }

    /**
     * 处理 NotFoundDialogException 异常
     */
    @ExceptionHandler(NotFoundDialogException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundDialogException(NotFoundDialogException ex){
        return ex.getLocalizedMessage();
    }

    /**
     * 处理通用的RuntimeException
     */
//    @ExceptionHandler(RuntimeException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handleRuntimeException(RuntimeException ex){
//        return ex.getLocalizedMessage();
//    }
}
