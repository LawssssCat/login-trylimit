package com.example.po;

import lombok.*;

import java.io.Serializable;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/26 16:51
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    private String id;
    private String username;
    private String password;

    /**
     * 锁定限制登录key： user:loginTime:lock:用户名
     */
    public static String getLoginTimeLockKey(String username) {
        return "user:loginTime:lock:" + username;
    }

    /**
     * 用户登录失败次数
     */
    public static String getLoginFailKey(String username) {
        return "user:login:fail:" + username;
    }

}
