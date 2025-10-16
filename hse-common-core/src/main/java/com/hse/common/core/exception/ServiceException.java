package com.hse.common.core.exception;

import com.hse.common.core.constant.MessageCode;

/**
 * 业务异常
 * 支持国际化消息码
 *
 * @author HSE
 */
public class ServiceException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ServiceException() {
        super();
    }

    /**
     * 构造方法（自定义消息）
     * @deprecated 建议使用 ServiceException(MessageCode) 支持国际化
     */
    @Deprecated
    public ServiceException(String message) {
        super(message);
    }

    /**
     * 构造方法（自定义码和消息）
     * @deprecated 建议使用 ServiceException(MessageCode) 支持国际化
     */
    @Deprecated
    public ServiceException(String code, String message) {
        super(code, message);
    }

    /**
     * 构造方法（国际化消息码）
     */
    public ServiceException(MessageCode messageCode) {
        super(messageCode);
    }

    /**
     * 构造方法（国际化消息码 + 参数）
     */
    public ServiceException(MessageCode messageCode, Object[] args) {
        super(messageCode, args);
    }

    /**
     * 构造方法（自定义消息 + 异常）
     * @deprecated 建议使用 ServiceException(MessageCode) 支持国际化
     */
    @Deprecated
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造方法（国际化消息码 + 异常）
     */
    public ServiceException(MessageCode messageCode, Throwable cause) {
        super(messageCode, cause);
    }
}