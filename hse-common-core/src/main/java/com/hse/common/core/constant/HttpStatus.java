package com.hse.common.core.constant;

/**
 * HTTP状态码
 *
 * @author HSE
 */
public interface HttpStatus {

    /**
     * 成功
     */
    int SUCCESS = 200;

    /**
     * 请求参数错误
     */
    int BAD_REQUEST = 400;

    /**
     * 未授权
     */
    int UNAUTHORIZED = 401;

    /**
     * 禁止访问
     */
    int FORBIDDEN = 403;

    /**
     * 资源不存在
     */
    int NOT_FOUND = 404;

    /**
     * 服务器错误
     */
    int ERROR = 500;
}