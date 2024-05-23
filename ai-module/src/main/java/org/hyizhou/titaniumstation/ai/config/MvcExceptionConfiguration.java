package org.hyizhou.titaniumstation.ai.config;

import org.hyizhou.titaniumstation.ai.exception.BusyDialogException;
import org.hyizhou.titaniumstation.ai.exception.NotFoundDialogException;
import org.hyizhou.titaniumstation.ai.exception.NotFoundRoleException;
import org.hyizhou.titaniumstation.ai.exception.UnauthorizedOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Spring MVC异常处理
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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundRoleException(NotFoundRoleException ex){
        return ex.getLocalizedMessage();
    }

    /**
     * 处理 NotFoundDialogException 异常
     */
    @ExceptionHandler(NotFoundDialogException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundDialogException(NotFoundDialogException ex){
        return ex.getLocalizedMessage();
    }

    /**
     * 处理 UnauthorizedOperationException
     */
    @ExceptionHandler(UnauthorizedOperationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleUnauthorizedOperationException(UnauthorizedOperationException ex){
        return ex.getLocalizedMessage();
    }

    /**
     * BusyDialogException
     */
    @ExceptionHandler(BusyDialogException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public String handleBusyDialogException(BusyDialogException ex){
        return ex.getLocalizedMessage();
    }

}
