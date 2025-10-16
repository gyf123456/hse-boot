package com.example.demo.controller;

import com.hse.common.core.constant.MessageCode;
import com.hse.common.core.domain.R;
import com.hse.common.core.exception.ServiceException;
import org.springframework.web.bind.annotation.*;

/**
 * 国际化使用示例
 *
 * @author HSE
 */
@RestController
@RequestMapping("/api/example")
public class I18nExampleController {

    /**
     * 示例1：返回国际化成功消息
     *
     * 请求：GET /api/example/success
     * 响应（中文）：{"code":"0000","msg":"操作成功","data":"Hello","timestamp":1234567890}
     * 响应（英文，添加请求头 Accept-Language: en-US）：
     *         {"code":"0000","msg":"Operation successful","data":"Hello","timestamp":1234567890}
     */
    @GetMapping("/success")
    public R<String> success() {
        return R.ok("Hello");
    }

    /**
     * 示例2：返回带消息码的成功响应
     */
    @GetMapping("/success-with-code")
    public R<String> successWithCode() {
        return R.ok(MessageCode.SUCCESS, "Hello");
    }

    /**
     * 示例3：抛出国际化异常
     *
     * 请求：GET /api/example/error
     * 响应（中文）：{"code":"0401","msg":"数据不存在","timestamp":1234567890}
     * 响应（英文）：{"code":"0401","msg":"Data does not exist","timestamp":1234567890}
     */
    @GetMapping("/error")
    public R<Void> error() {
        throw new ServiceException(MessageCode.DATA_NOT_EXIST);
    }

    /**
     * 示例4：抛出带参数的国际化异常
     *
     * 请求：GET /api/example/error-with-params
     * 响应（中文）：{"code":"0007","msg":"参数校验失败：用户名不能为空","timestamp":1234567890}
     */
    @GetMapping("/error-with-params")
    public R<Void> errorWithParams() {
        throw new ServiceException(MessageCode.PARAM_INVALID, new Object[]{"用户名不能为空"});
    }

    /**
     * 示例5：参数校验（自动国际化）
     *
     * 请求：POST /api/example/validate
     * Body: {"username": ""}
     * 响应：{"code":"0007","msg":"参数校验失败：用户名不能为空","timestamp":1234567890}
     */
    @PostMapping("/validate")
    public R<Void> validate(@RequestBody UserDTO user) {
        // 参数校验失败会被全局异常处理器捕获，自动返回国际化消息
        return R.ok();
    }

    /**
     * 示例6：切换语言（通过请求参数）
     *
     * 请求（中文）：GET /api/example/lang?lang=zh_CN
     * 请求（英文）：GET /api/example/lang?lang=en_US
     */
    @GetMapping("/lang")
    public R<String> lang() {
        return R.ok("Language switched");
    }

    /**
     * 示例7：在代码中获取国际化消息
     */
    @GetMapping("/custom-message")
    public R<String> customMessage() {
        String message = MessageUtils.getMessage("message.user.not.exist");
        return R.ok(message);
    }

    /**
     * 示例8：获取带参数的国际化消息
     */
    @GetMapping("/custom-message-with-params")
    public R<String> customMessageWithParams() {
        String message = MessageUtils.getMessage(
            "message.param.invalid",
            new Object[]{"用户名"}
        );
        return R.ok(message);
    }
}

/**
 * 用户DTO示例
 */
class UserDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    // getters and setters...
}