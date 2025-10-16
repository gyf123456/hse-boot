package com.hse.common.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息码枚举
 * 规则：模块(2位) + 功能(2位) + 序号(3位)
 *
 * @author HSE
 */
@Getter
@AllArgsConstructor
public enum MessageCode {

    // ========== 通用模块 00xx ==========
    SUCCESS("0000", "message.success"),
    FAIL("0001", "message.fail"),
    BAD_REQUEST("0002", "message.bad.request"),
    UNAUTHORIZED("0003", "message.unauthorized"),
    FORBIDDEN("0004", "message.forbidden"),
    NOT_FOUND("0005", "message.not.found"),
    INTERNAL_ERROR("0006", "message.internal.error"),
    PARAM_INVALID("0007", "message.param.invalid"),
    PARAM_MISSING("0008", "message.param.missing"),

    // ========== 用户模块 01xx ==========
    USER_NOT_EXIST("0101", "message.user.not.exist"),
    USER_DISABLED("0102", "message.user.disabled"),
    USER_PASSWORD_ERROR("0103", "message.user.password.error"),
    USER_ALREADY_EXIST("0104", "message.user.already.exist"),

    // ========== 认证模块 02xx ==========
    TOKEN_INVALID("0201", "message.token.invalid"),
    TOKEN_EXPIRED("0202", "message.token.expired"),
    TOKEN_MISSING("0203", "message.token.missing"),
    LOGIN_FAIL("0204", "message.login.fail"),
    LOGOUT_SUCCESS("0205", "message.logout.success"),

    // ========== 权限模块 03xx ==========
    PERMISSION_DENIED("0301", "message.permission.denied"),
    ROLE_NOT_EXIST("0302", "message.role.not.exist"),

    // ========== 数据模块 04xx ==========
    DATA_NOT_EXIST("0401", "message.data.not.exist"),
    DATA_ALREADY_EXIST("0402", "message.data.already.exist"),
    DATA_IN_USE("0403", "message.data.in.use"),

    ;

    /**
     * 消息码
     */
    private final String code;

    /**
     * 消息键（对应i18n文件中的key）
     */
    private final String key;
}