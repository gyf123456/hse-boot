# HSE Boot 微服务脚手架

## 项目简介

HSE Boot 是一个基于 Spring Boot 的微服务脚手架工程，提供开箱即用的基础组件，帮助快速搭建微服务应用。

## 模块结构

```
hse-boot
├── hse-common-core         # 核心工具模块
├── hse-common-web          # Web通用模块
├── hse-common-mybatis      # 数据库模块
├── hse-common-redis        # Redis缓存模块
├── hse-common-security     # 安全认证模块
└── hse-starter             # 快速启动模块
```

## 技术栈

- **Spring Boot**: 2.7.18
- **MyBatis Plus**: 3.5.5
- **Druid**: 1.2.20
- **Redisson**: 3.24.3
- **Hutool**: 5.8.23
- **Fastjson2**: 2.0.43
- **JWT**: 0.12.3

## 快速开始

### 1. 安装到本地仓库

```bash
mvn clean install
```

### 2. 在微服务中使用

在新建的微服务项目 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>com.hse</groupId>
    <artifactId>hse-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 3. 配置文件

```yaml
spring:
  application:
    name: your-service-name
  datasource:
    url: jdbc:mysql://localhost:3306/your_db
    username: root
    password: root
  data:
    redis:
      host: localhost
      port: 6379
```

## 核心功能

### 1. 国际化支持（i18n）⭐

支持多语言自动切换，开箱即用：

```java
// 返回国际化消息
@GetMapping("/user/{id}")
public R<User> getUser(@PathVariable Long id) {
    User user = userService.getById(id);
    if (user == null) {
        return R.fail(MessageCode.USER_NOT_EXIST);  // 自动国际化
    }
    return R.ok(user);
}

// 抛出国际化异常
throw new ServiceException(MessageCode.DATA_NOT_EXIST);

// 切换语言：请求头添加 Accept-Language: en-US 或 zh-CN
// 或使用请求参数：?lang=en_US
```

**内置语言**：中文（默认）、英文
**详细文档**：[国际化使用指南](hse-starter/src/main/resources/examples/I18N_GUIDE.md)

### 2. JSON序列化（蛇形命名）⭐

全局使用蛇形命名（snake_case），符合RESTful API规范：

```java
// Java实体类（驼峰命名）
@Data
public class User {
    private Long userId;           // -> user_id
    private String userName;       // -> user_name
    private String emailAddress;   // -> email_address
    private LocalDateTime createdAt; // -> created_at
}

// JSON响应（蛇形命名）
{
  "code": "0000",
  "msg": "操作成功",
  "data": {
    "user_id": 1,
    "user_name": "张三",
    "email_address": "zhangsan@example.com",
    "created_at": "2024-01-01 10:00:00"
  }
}
```

**核心特性**：
- 蛇形命名自动转换
- 统一日期格式（yyyy-MM-dd HH:mm:ss）
- null值不序列化
- 忽略未知属性

**详细文档**：[JSON序列化指南](hse-starter/src/main/resources/examples/JSON_SERIALIZATION_GUIDE.md)

### 3. 统一响应封装

```java
@GetMapping("/test")
public R<String> test() {
    return R.ok("success");
}
```

### 4. 全局异常处理

```java
throw new ServiceException(MessageCode.USER_NOT_EXIST);
```

### 5. MyBatis Plus 集成

```java
public class User extends BaseEntity {
    private Long id;
    private String username;
}
```

### 6. Redis 工具类

```java
@Autowired
private RedisUtil redisUtil;

redisUtil.set("key", "value", 60, TimeUnit.SECONDS);
```

### 7. JWT 认证

```java
@Autowired
private JwtUtil jwtUtil;

String token = jwtUtil.generateToken(userId);
```

## 模块说明

### hse-common-core
核心工具模块，包含：
- 统一响应结果 `R`（支持国际化）
- 基础异常类 `BaseException`、`ServiceException`（支持国际化）
- 消息码枚举 `MessageCode`
- 国际化工具类 `MessageUtils`
- HTTP状态码常量
- Spring上下文工具类

### hse-common-web
Web通用模块，包含：
- 全局异常处理器（支持国际化）
- 国际化配置（MessageSource、LocaleResolver）
- Jackson序列化配置（蛇形命名、日期格式）
- 跨域配置
- 参数校验（支持国际化）

### hse-common-mybatis
数据库模块，包含：
- MyBatis Plus 配置
- 分页插件
- 字段自动填充
- 基础实体类

### hse-common-redis
Redis模块，包含：
- Redis序列化配置（蛇形命名、日期格式）
- Redis工具类
- Redisson分布式锁支持

### hse-common-security
安全模块，包含：
- JWT工具类
- 登录用户信息封装

## 特性亮点

- ✅ **开箱即用**：一行依赖即可集成所有基础能力
- 🌍 **国际化支持**：完整的i18n方案，支持多语言自动切换
- 📐 **蛇形命名**：全局JSON序列化使用snake_case，符合RESTful规范
- 🎯 **统一响应**：标准化的API响应格式
- 🛡️ **全局异常**：统一的异常处理机制
- 🔐 **安全认证**：JWT认证开箱即用
- 📊 **数据库集成**：MyBatis Plus + Druid连接池
- 🚀 **Redis支持**：Redisson分布式锁
- 📝 **代码规范**：遵循企业级开发规范

## License

MIT License