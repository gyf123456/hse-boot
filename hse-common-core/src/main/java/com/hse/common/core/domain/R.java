package com.hse.common.core.domain;

import com.alibaba.fastjson2.JSON;
import com.hse.common.core.constant.MessageCode;
import com.hse.common.core.utils.MessageUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果
 * 支持国际化消息
 *
 * @author HSE
 */
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public R() {
        this.timestamp = System.currentTimeMillis();
    }

    public R(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // ==================== 国际化消息方法 ====================

    /**
     * 成功（国际化）
     */
    public static <T> R<T> ok() {
        return ok(MessageCode.SUCCESS);
    }

    /**
     * 成功（国际化）
     */
    public static <T> R<T> ok(T data) {
        return ok(MessageCode.SUCCESS, data);
    }

    /**
     * 成功（国际化消息码）
     */
    public static <T> R<T> ok(MessageCode messageCode) {
        return ok(messageCode, null, null);
    }

    /**
     * 成功（国际化消息码 + 数据）
     */
    public static <T> R<T> ok(MessageCode messageCode, T data) {
        return ok(messageCode, null, data);
    }

    /**
     * 成功（国际化消息码 + 参数 + 数据）
     */
    public static <T> R<T> ok(MessageCode messageCode, Object[] args, T data) {
        String message = MessageUtils.getMessage(messageCode.getKey(), args);
        return new R<>(messageCode.getCode(), message, data);
    }

    /**
     * 失败（国际化）
     */
    public static <T> R<T> fail() {
        return fail(MessageCode.FAIL);
    }

    /**
     * 失败（国际化消息码）
     */
    public static <T> R<T> fail(MessageCode messageCode) {
        return fail(messageCode, null);
    }

    /**
     * 失败（国际化消息码 + 参数）
     */
    public static <T> R<T> fail(MessageCode messageCode, Object[] args) {
        String message = MessageUtils.getMessage(messageCode.getKey(), args);
        return new R<>(messageCode.getCode(), message, null);
    }

    // ==================== 兼容旧版本方法 ====================

    /**
     * 成功（自定义消息）
     * @deprecated 建议使用 ok(MessageCode) 支持国际化
     */
    @Deprecated
    public static <T> R<T> ok(String msg, T data) {
        return new R<>("0000", msg, data);
    }

    /**
     * 失败（自定义消息）
     * @deprecated 建议使用 fail(MessageCode) 支持国际化
     */
    @Deprecated
    public static <T> R<T> fail(String msg) {
        return new R<>("0001", msg, null);
    }

    /**
     * 失败（自定义码和消息）
     * @deprecated 建议使用 fail(MessageCode) 支持国际化
     */
    @Deprecated
    public static <T> R<T> fail(String code, String msg) {
        return new R<>(code, msg, null);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return "0000".equals(this.code);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}