package com.example.demo.controller;

import com.hse.common.core.domain.R;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON序列化示例
 * 演示蛇形命名的序列化效果
 *
 * @author HSE
 */
@RestController
@RequestMapping("/api/example/json")
public class JsonSerializationExampleController {

    /**
     * 示例1：基本的蛇形命名序列化
     *
     * 请求：GET /api/example/json/user
     * 响应：
     * {
     *   "code": "0000",
     *   "msg": "操作成功",
     *   "data": {
     *     "user_id": 1,
     *     "user_name": "张三",
     *     "email_address": "zhangsan@example.com",
     *     "phone_number": "13800138000",
     *     "is_active": true,
     *     "created_at": "2024-01-01 10:00:00"
     *   },
     *   "timestamp": 1704067200000
     * }
     */
    @GetMapping("/user")
    public R<UserDTO> getUser() {
        UserDTO user = new UserDTO();
        user.setUserId(1L);
        user.setUserName("张三");
        user.setEmailAddress("zhangsan@example.com");
        user.setPhoneNumber("13800138000");
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
        return R.ok(user);
    }

    /**
     * 示例2：接收蛇形命名的请求参数
     *
     * 请求：POST /api/example/json/user
     * Body（蛇形命名）:
     * {
     *   "user_name": "李四",
     *   "email_address": "lisi@example.com",
     *   "phone_number": "13900139000",
     *   "birth_date": "1990-01-01"
     * }
     *
     * 响应：
     * {
     *   "code": "0000",
     *   "msg": "操作成功",
     *   "data": {
     *     "user_id": 2,
     *     "user_name": "李四",
     *     "email_address": "lisi@example.com",
     *     "phone_number": "13900139000",
     *     "is_active": true,
     *     "birth_date": "1990-01-01",
     *     "created_at": "2024-01-01 12:00:00"
     *   }
     * }
     */
    @PostMapping("/user")
    public R<UserDTO> createUser(@RequestBody UserDTO user) {
        // 自动反序列化蛇形命名为驼峰
        user.setUserId(2L);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        return R.ok(user);
    }

    /**
     * 示例3：列表数据的序列化
     *
     * 请求：GET /api/example/json/users
     * 响应：
     * {
     *   "code": "0000",
     *   "msg": "操作成功",
     *   "data": [
     *     {
     *       "user_id": 1,
     *       "user_name": "张三",
     *       "email_address": "zhangsan@example.com",
     *       "is_active": true
     *     },
     *     {
     *       "user_id": 2,
     *       "user_name": "李四",
     *       "email_address": "lisi@example.com",
     *       "is_active": false
     *     }
     *   ]
     * }
     */
    @GetMapping("/users")
    public R<List<UserDTO>> listUsers() {
        List<UserDTO> users = new ArrayList<>();

        UserDTO user1 = new UserDTO();
        user1.setUserId(1L);
        user1.setUserName("张三");
        user1.setEmailAddress("zhangsan@example.com");
        user1.setIsActive(true);
        users.add(user1);

        UserDTO user2 = new UserDTO();
        user2.setUserId(2L);
        user2.setUserName("李四");
        user2.setEmailAddress("lisi@example.com");
        user2.setIsActive(false);
        users.add(user2);

        return R.ok(users);
    }

    /**
     * 示例4：null值不序列化
     *
     * 请求：GET /api/example/json/user-with-null
     * 响应（phone_number为null，不会出现在JSON中）:
     * {
     *   "code": "0000",
     *   "msg": "操作成功",
     *   "data": {
     *     "user_id": 3,
     *     "user_name": "王五",
     *     "email_address": "wangwu@example.com",
     *     "is_active": true
     *   }
     * }
     */
    @GetMapping("/user-with-null")
    public R<UserDTO> getUserWithNull() {
        UserDTO user = new UserDTO();
        user.setUserId(3L);
        user.setUserName("王五");
        user.setEmailAddress("wangwu@example.com");
        user.setPhoneNumber(null); // null值不会序列化
        user.setIsActive(true);
        return R.ok(user);
    }

    /**
     * 示例5：嵌套对象的序列化
     *
     * 请求：GET /api/example/json/user-detail
     * 响应：
     * {
     *   "code": "0000",
     *   "msg": "操作成功",
     *   "data": {
     *     "user_id": 1,
     *     "user_name": "张三",
     *     "user_profile": {
     *       "real_name": "张三丰",
     *       "id_card_number": "110101199001011234",
     *       "home_address": "北京市朝阳区"
     *     }
     *   }
     * }
     */
    @GetMapping("/user-detail")
    public R<UserDetailDTO> getUserDetail() {
        UserDetailDTO detail = new UserDetailDTO();
        detail.setUserId(1L);
        detail.setUserName("张三");

        UserProfileDTO profile = new UserProfileDTO();
        profile.setRealName("张三丰");
        profile.setIdCardNumber("110101199001011234");
        profile.setHomeAddress("北京市朝阳区");

        detail.setUserProfile(profile);
        return R.ok(detail);
    }
}

/**
 * 用户DTO
 * Java属性：驼峰命名 -> JSON字段：蛇形命名
 */
@Data
class UserDTO {
    // userId -> user_id
    private Long userId;

    // userName -> user_name
    private String userName;

    // emailAddress -> email_address
    private String emailAddress;

    // phoneNumber -> phone_number
    private String phoneNumber;

    // isActive -> is_active
    private Boolean isActive;

    // birthDate -> birth_date
    private LocalDate birthDate;

    // createdAt -> created_at
    private LocalDateTime createdAt;
}

/**
 * 用户详情DTO
 */
@Data
class UserDetailDTO {
    private Long userId;
    private String userName;
    // userProfile -> user_profile
    private UserProfileDTO userProfile;
}

/**
 * 用户资料DTO
 */
@Data
class UserProfileDTO {
    // realName -> real_name
    private String realName;
    // idCardNumber -> id_card_number
    private String idCardNumber;
    // homeAddress -> home_address
    private String homeAddress;
}