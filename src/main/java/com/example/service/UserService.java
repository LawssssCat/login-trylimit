package com.example.service;

import com.example.po.User;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/26 16:49
 */
public interface UserService {

    /**
     * 模仿shiro的subject
     *
     * @param username 用户名
     * @param password 密码
     * @return subject
     */
    public User login(String username, String password);


    /**
     * 用户在2分钟内，仅允许输入错误密码5次。如果超过次数，限制登录1小时
     * 给用户的详细提示
     *
     * @param username 用户名
     * @return 剩余尝试次数
     */
    Integer afterloginFail(String username);

    /**
     * 用户登录成功，清空错误次数的记录
     * 返回详细信息
     *
     * @param username 用户名
     * @return 详细信息
     */
    String afterloginSuccess(String username);

    /**
     * 判断当前用户是否被锁定（限制登录）
     *
     * @param username
     * @return 用户被锁定时间（单位：min），-1表示没锁定
     */
    Integer loginUserLock(String username);
}
