# JSON序列化配置指南

## 概述

HSE Boot 采用**蛇形命名（snake_case）**作为全局JSON序列化策略，符合RESTful API最佳实践。

## 核心特性

### 1. 全局蛇形命名

Java代码使用驼峰命名，JSON自动转换为蛇形命名：

| Java字段（驼峰） | JSON字段（蛇形） |
|----------------|----------------|
| userId         | user_id        |
| userName       | user_name      |
| emailAddress   | email_address  |
| isActive       | is_active      |
| createdAt      | created_at     |

### 2. 统一日期格式

- **LocalDateTime**: `yyyy-MM-dd HH:mm:ss`
- **LocalDate**: `yyyy-MM-dd`
- **LocalTime**: `HH:mm:ss`
- **Date**: `yyyy-MM-dd HH:mm:ss`
- **时区**: GMT+8

### 3. 其他特性

- ✅ null值不序列化
- ✅ 忽略未知属性
- ✅ 格式化输出（开发环境）
- ✅ 支持嵌套对象
- ✅ 支持集合类型

## 使用示例

### 基本用法

#### Java实体类

```java
@Data
public class User {
    private Long userId;           // -> user_id
    private String userName;       // -> user_name
    private String emailAddress;   // -> email_address
    private Boolean isActive;      // -> is_active
    private LocalDateTime createdAt; // -> created_at
}
```

#### Controller

```java
@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/{id}")
    public R<User> getUser(@PathVariable Long id) {
        User user = new User();
        user.setUserId(1L);
        user.setUserName("张三");
        user.setEmailAddress("zhangsan@example.com");
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        return R.ok(user);
    }
}
```

#### JSON响应

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": {
    "user_id": 1,
    "user_name": "张三",
    "email_address": "zhangsan@example.com",
    "is_active": true,
    "created_at": "2024-01-01 10:00:00"
  },
  "timestamp": 1704067200000
}
```

### 接收请求参数

#### 请求JSON（蛇形）

```json
POST /api/user
Content-Type: application/json

{
  "user_name": "李四",
  "email_address": "lisi@example.com",
  "phone_number": "13900139000",
  "birth_date": "1990-01-01"
}
```

#### Java代码（驼峰）

```java
@PostMapping
public R<User> createUser(@RequestBody User user) {
    // 自动反序列化：user_name -> userName
    System.out.println(user.getUserName()); // 李四
    System.out.println(user.getEmailAddress()); // lisi@example.com
    return R.ok(user);
}
```

### 嵌套对象

#### Java实体

```java
@Data
public class UserDetail {
    private Long userId;
    private String userName;
    private UserProfile userProfile; // 嵌套对象
}

@Data
public class UserProfile {
    private String realName;
    private String idCardNumber;
    private String homeAddress;
}
```

#### JSON响应

```json
{
  "code": "0000",
  "msg": "操作成功",
  "data": {
    "user_id": 1,
    "user_name": "张三",
    "user_profile": {
      "real_name": "张三丰",
      "id_card_number": "110101199001011234",
      "home_address": "北京市朝阳区"
    }
  }
}
```

### null值处理

```java
User user = new User();
user.setUserId(1L);
user.setUserName("张三");
user.setPhoneNumber(null); // null值
```

**JSON输出（null字段不出现）：**

```json
{
  "user_id": 1,
  "user_name": "张三"
}
```

## 日期时间处理

### LocalDateTime示例

```java
@Data
public class Order {
    private Long orderId;
    private LocalDateTime createdTime;
    private LocalDateTime paymentTime;
}
```

```json
{
  "order_id": 1001,
  "created_time": "2024-01-15 10:30:00",
  "payment_time": "2024-01-15 10:35:00"
}
```

### LocalDate示例

```java
@Data
public class Employee {
    private Long employeeId;
    private LocalDate birthDate;
    private LocalDate hireDate;
}
```

```json
{
  "employee_id": 1,
  "birth_date": "1990-05-20",
  "hire_date": "2020-01-01"
}
```

## 特殊情况处理

### 1. 自定义字段名

如果某个字段需要特殊命名，使用 `@JsonProperty`：

```java
@Data
public class User {
    @JsonProperty("uid")  // 强制使用 uid 而不是 user_id
    private Long userId;

    private String userName; // 仍然是 user_name
}
```

```json
{
  "uid": 1,
  "user_name": "张三"
}
```

### 2. 忽略某个字段

使用 `@JsonIgnore` 忽略序列化：

```java
@Data
public class User {
    private Long userId;
    private String userName;

    @JsonIgnore  // 不序列化到JSON
    private String password;
}
```

### 3. 自定义日期格式

使用 `@JsonFormat` 覆盖全局配置：

```java
@Data
public class Log {
    private Long logId;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate logDate;
}
```

```json
{
  "log_id": 1,
  "log_date": "2024/01/15"
}
```

## 配置说明

### Web层配置

位置：`hse-common-web/config/JacksonConfig.java`

```java
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 蛇形命名策略
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        // null值不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 日期格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // 忽略未知属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return objectMapper;
    }
}
```

### Redis层配置

位置：`hse-common-redis/config/RedisConfig.java`

Redis序列化与Web层保持一致，确保缓存数据格式统一。

## 测试建议

### 使用Postman测试

**1. 测试序列化（响应）**

```bash
GET http://localhost:8080/api/user/1
Accept-Language: zh-CN
```

**2. 测试反序列化（请求）**

```bash
POST http://localhost:8080/api/user
Content-Type: application/json

{
  "user_name": "测试用户",
  "email_address": "test@example.com"
}
```

### 单元测试

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetUser() throws Exception {
        mockMvc.perform(get("/api/user/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.user_id").value(1))
            .andExpect(jsonPath("$.data.user_name").value("张三"));
    }

    @Test
    void testCreateUser() throws Exception {
        String json = """
            {
              "user_name": "李四",
              "email_address": "lisi@example.com"
            }
            """;

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.user_name").value("李四"));
    }
}
```

## 常见问题

### Q1: 为什么使用蛇形命名？

**A:** 蛇形命名是RESTful API的最佳实践：
- 符合大多数语言的JSON规范
- 与数据库字段命名保持一致
- 提高前后端协作效率
- 便于与其他系统集成

### Q2: 如何临时使用驼峰命名？

**A:** 在特定字段使用 `@JsonProperty`：

```java
@JsonProperty("userName")  // 强制驼峰
private String userName;
```

### Q3: 日期格式不对怎么办？

**A:** 确保全局配置生效：
1. 检查 `JacksonConfig` 是否被扫描
2. 不要在实体类上使用 `@JsonFormat`（除非有特殊需求）
3. 使用 `LocalDateTime` 而不是 `Date`

### Q4: Redis缓存的数据格式不一致？

**A:** 确保Redis配置也使用了蛇形命名策略。查看 `RedisConfig.java` 中的 `createObjectMapper()` 方法。

### Q5: 前端如何处理？

**A:** 前端可以直接使用蛇形命名，或者使用转换库：

```javascript
// TypeScript/JavaScript
interface User {
  user_id: number;
  user_name: string;
  email_address: string;
}

// 或使用 lodash 转换
import { camelCase } from 'lodash';
```

## 最佳实践

### 1. 保持一致性

✅ **推荐**：全局使用蛇形命名
```java
private String userName;      // -> user_name
private String emailAddress;  // -> email_address
```

❌ **不推荐**：混用命名风格
```java
@JsonProperty("userName")     // 驼峰
private String userName;

private String emailAddress;  // 蛇形
```

### 2. 避免过度使用@JsonProperty

✅ **推荐**：依赖全局配置
```java
private String userName; // 自动转换
```

❌ **不推荐**：每个字段都标注
```java
@JsonProperty("user_name")
private String userName;
```

### 3. 统一日期格式

✅ **推荐**：使用Java 8时间API
```java
private LocalDateTime createdAt;
private LocalDate birthDate;
```

❌ **不推荐**：使用旧的Date类
```java
private Date createdAt;
```

## 参考示例

完整代码示例请查看：
- `examples/JsonSerializationExampleController.java`

## 相关文档

- [Jackson官方文档](https://github.com/FasterXML/jackson-docs)
- [Spring Boot JSON配置](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.spring-mvc.customize-jackson-objectmapper)