# HSE Boot 国际化使用文档

## 概述

HSE Boot 提供了完整的国际化（i18n）支持，包括：
- 统一的消息码管理
- 自动语言切换
- 异常消息国际化
- 参数校验国际化

## 配置说明

### 1. 默认配置

脚手架已自动配置国际化支持：

```yaml
# 国际化资源文件位置：src/main/resources/i18n/messages*.properties
# 默认语言：中文（zh_CN）
# 支持语言：中文（zh_CN）、英文（en_US）
```

### 2. 切换语言的方式

**方式1：请求头（推荐）**
```bash
curl -H "Accept-Language: en-US" http://localhost:8080/api/user/list
curl -H "Accept-Language: zh-CN" http://localhost:8080/api/user/list
```

**方式2：请求参数**
```bash
curl http://localhost:8080/api/user/list?lang=en_US
curl http://localhost:8080/api/user/list?lang=zh_CN
```

## 使用方式

### 1. Controller 返回国际化消息

```java
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * 使用默认成功消息
     */
    @GetMapping("/list")
    public R<List<User>> list() {
        return R.ok(userList);  // 自动返回国际化的"操作成功"
    }

    /**
     * 使用指定消息码
     */
    @GetMapping("/{id}")
    public R<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return R.fail(MessageCode.DATA_NOT_EXIST);  // 国际化的"数据不存在"
        }
        return R.ok(user);
    }
}
```

### 2. 抛出国际化异常

```java
@Service
public class UserService {

    public void login(String username, String password) {
        User user = userMapper.selectByUsername(username);

        // 用户不存在
        if (user == null) {
            throw new ServiceException(MessageCode.USER_NOT_EXIST);
        }

        // 用户已禁用
        if (user.getStatus() == 0) {
            throw new ServiceException(MessageCode.USER_DISABLED);
        }

        // 密码错误
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException(MessageCode.USER_PASSWORD_ERROR);
        }
    }

    /**
     * 带参数的异常消息
     */
    public void validateAge(int age) {
        if (age < 18) {
            // 消息模板：参数校验失败：{0}
            throw new ServiceException(
                MessageCode.PARAM_INVALID,
                new Object[]{"年龄必须大于18岁"}
            );
        }
    }
}
```

### 3. 参数校验国际化

参数校验失败会自动转换为国际化消息：

```java
@Data
public class UserDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;

    @Email(message = "邮箱格式不正确")
    private String email;
}
```

### 4. 手动获取国际化消息

```java
@Service
public class NotificationService {

    /**
     * 获取简单消息
     */
    public void sendNotification() {
        String message = MessageUtils.getMessage("message.logout.success");
        // 根据当前语言返回："退出成功" 或 "Logout successful"
    }

    /**
     * 获取带参数的消息
     */
    public void sendWelcome(String username) {
        // 假设消息模板：欢迎 {0} 登录系统
        String message = MessageUtils.getMessage(
            "message.welcome",
            new Object[]{username}
        );
    }

    /**
     * 指定语言获取消息
     */
    public void sendEmailInEnglish(String email) {
        String message = MessageUtils.getMessage(
            "message.success",
            null,
            Locale.ENGLISH
        );
    }
}
```

## 消息码规范

### 消息码格式

```
模块(2位) + 功能(2位) + 序号(3位) = 7位数字
```

### 消息码分类

| 前缀 | 模块 | 示例 |
|------|------|------|
| 00xx | 通用模块 | 0000=成功, 0001=失败 |
| 01xx | 用户模块 | 0101=用户不存在 |
| 02xx | 认证模块 | 0201=令牌无效 |
| 03xx | 权限模块 | 0301=权限不足 |
| 04xx | 数据模块 | 0401=数据不存在 |

### 扩展消息码

在 `MessageCode` 枚举中添加新的消息码：

```java
public enum MessageCode {

    // ... 现有消息码

    // ========== 订单模块 05xx ==========
    ORDER_NOT_EXIST("0501", "message.order.not.exist"),
    ORDER_PAID("0502", "message.order.paid"),
    ORDER_CANCELLED("0503", "message.order.cancelled"),

    ;
}
```

在资源文件中添加对应的消息：

```properties
# messages_zh_CN.properties
message.order.not.exist=订单不存在
message.order.paid=订单已支付
message.order.cancelled=订单已取消

# messages_en_US.properties
message.order.not.exist=Order does not exist
message.order.paid=Order has been paid
message.order.cancelled=Order has been cancelled
```

## 新增语言支持

### 1. 添加资源文件

```bash
# 日语
src/main/resources/i18n/messages_ja_JP.properties

# 韩语
src/main/resources/i18n/messages_ko_KR.properties

# 法语
src/main/resources/i18n/messages_fr_FR.properties
```

### 2. 翻译消息

```properties
# messages_ja_JP.properties
message.success=操作が成功しました
message.fail=操作が失敗しました
message.user.not.exist=ユーザーが存在しません
```

### 3. 使用新语言

```bash
curl -H "Accept-Language: ja-JP" http://localhost:8080/api/user/list
```

## API 响应示例

### 成功响应（中文）

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": {
    "id": 1,
    "username": "admin"
  },
  "timestamp": 1704067200000
}
```

### 成功响应（英文）

```json
{
  "code": "0000",
  "msg": "Operation successful",
  "data": {
    "id": 1,
    "username": "admin"
  },
  "timestamp": 1704067200000
}
```

### 错误响应（中文）

```json
{
  "code": "0101",
  "msg": "用户不存在",
  "timestamp": 1704067200000
}
```

### 错误响应（英文）

```json
{
  "code": "0101",
  "msg": "User does not exist",
  "timestamp": 1704067200000
}
```

## 最佳实践

### 1. 统一使用消息码

❌ **不推荐**
```java
return R.fail("用户不存在");  // 硬编码，不支持国际化
```

✅ **推荐**
```java
return R.fail(MessageCode.USER_NOT_EXIST);  // 使用消息码
```

### 2. 异常使用消息码

❌ **不推荐**
```java
throw new ServiceException("数据已存在");
```

✅ **推荐**
```java
throw new ServiceException(MessageCode.DATA_ALREADY_EXIST);
```

### 3. 参数化消息

当消息需要动态内容时，使用参数：

```java
// 消息模板：用户 {0} 登录失败，剩余尝试次数：{1}
throw new ServiceException(
    MessageCode.LOGIN_FAIL,
    new Object[]{username, remainTimes}
);
```

### 4. 日志记录

记录日志时使用原始消息码，便于问题排查：

```java
try {
    // 业务逻辑
} catch (ServiceException e) {
    log.error("业务异常，消息码：{}，详情：{}",
        e.getMessageCode().getCode(),
        e.getMessage());
    throw e;
}
```

## 注意事项

1. **资源文件编码**：所有 `.properties` 文件必须使用 UTF-8 编码
2. **消息码唯一性**：确保消息码在整个系统中唯一
3. **向后兼容**：旧版本的字符串消息仍然支持，但建议迁移到消息码
4. **缓存配置**：生产环境建议设置消息源缓存，开发环境可关闭便于调试
5. **默认语言**：当请求的语言不存在时，会回退到默认语言（中文）

## 完整示例

参考 `examples/I18nExampleController.java` 查看完整的使用示例。