package com.hse.common.web.exception;

import com.hse.common.core.constant.MessageCode;
import com.hse.common.core.domain.R;
import com.hse.common.core.exception.BaseException;
import com.hse.common.core.exception.ServiceException;
import com.hse.common.core.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 支持国际化异常消息
 *
 * @author HSE
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 基础异常
     */
    @ExceptionHandler(BaseException.class)
    public R<Void> handleBaseException(BaseException e) {
        log.error("业务异常: {}", e.getMessage(), e);

        // 如果异常包含国际化消息码，使用国际化消息
        if (e.getMessageCode() != null) {
            return R.fail(e.getMessageCode(), e.getArgs());
        }

        // 否则使用异常中的自定义消息（向后兼容）
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R<Void> handleServiceException(ServiceException e) {
        log.error("业务异常: {}", e.getMessage(), e);

        // 如果异常包含国际化消息码，使用国际化消息
        if (e.getMessageCode() != null) {
            return R.fail(e.getMessageCode(), e.getArgs());
        }

        // 否则使用异常中的自定义消息（向后兼容）
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常 - @RequestBody
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("参数校验失败: {}", message);

        // 使用国际化消息
        String i18nMessage = MessageUtils.getMessage(MessageCode.PARAM_INVALID.getKey(), new Object[]{message});
        return R.fail(MessageCode.PARAM_INVALID.getCode(), i18nMessage);
    }

    /**
     * 参数校验异常 - @ModelAttribute
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("参数绑定失败: {}", message);

        // 使用国际化消息
        String i18nMessage = MessageUtils.getMessage(MessageCode.PARAM_INVALID.getKey(), new Object[]{message});
        return R.fail(MessageCode.PARAM_INVALID.getCode(), i18nMessage);
    }

    /**
     * 参数校验异常 - @RequestParam
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.error("参数约束违反: {}", message);

        // 使用国际化消息
        String i18nMessage = MessageUtils.getMessage(MessageCode.PARAM_INVALID.getKey(), new Object[]{message});
        return R.fail(MessageCode.PARAM_INVALID.getCode(), i18nMessage);
    }

    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R<Void> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常: {}", e.getMessage(), e);
        return R.fail(MessageCode.INTERNAL_ERROR);
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return R.fail(MessageCode.INTERNAL_ERROR);
    }
}