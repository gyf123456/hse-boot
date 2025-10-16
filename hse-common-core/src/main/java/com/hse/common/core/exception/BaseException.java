package com.hse.common.core.exception;

import com.hse.common.core.constant.MessageCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础异常
 * 支持国际化消息码
 *
 * @author HSE
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 消息码（国际化）
     */
    private MessageCode messageCode;

    /**
     * 消息参数（国际化占位符）
     */
    private Object[] args;

    public BaseException() {
        super();
    }

    /**
     * 构造方法（自定义消息）
     * @deprecated 建议使用 BaseException(MessageCode) 支持国际化
     */
    @Deprecated
    public BaseException(String message) {
        super(message);
        this.message = message;
        this.code = "0001";
    }

    /**
     * 构造方法（自定义码和消息）
     * @deprecated 建议使用 BaseException(MessageCode) 支持国际化
     */
    @Deprecated
    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造方法（国际化消息码）
     */
    public BaseException(MessageCode messageCode) {
        super(messageCode.getKey());
        this.messageCode = messageCode;
        this.code = messageCode.getCode();
    }

    /**
     * 构造方法（国际化消息码 + 参数）
     */
    public BaseException(MessageCode messageCode, Object[] args) {
        super(messageCode.getKey());
        this.messageCode = messageCode;
        this.code = messageCode.getCode();
        this.args = args;
    }

    /**
     * 构造方法（自定义消息 + 异常）
     * @deprecated 建议使用 BaseException(MessageCode) 支持国际化
     */
    @Deprecated
    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = "0001";
    }

    /**
     * 构造方法（国际化消息码 + 异常）
     */
    public BaseException(MessageCode messageCode, Throwable cause) {
        super(messageCode.getKey(), cause);
        this.messageCode = messageCode;
        this.code = messageCode.getCode();
    }
}