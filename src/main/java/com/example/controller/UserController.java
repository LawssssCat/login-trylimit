package com.example.controller;

import com.example.po.User;
import com.example.service.UserService;
import com.example.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLOutput;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/26 16:46
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @param username 用户名
     * @param password 密码
     * @param valcode  验证码
     * @return
     */
    @ResponseBody
    @RequestMapping("/login")
    public JsonResult login(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password,
            @RequestParam("valcode") final String valcode
    ) {
        /*
         1 验证码比较
         */

        /*
         判断是否锁定
         */
        Integer lockTime = userService.loginUserLock(username);
        if (lockTime != -1) {
            return JsonResult.lock("用户被锁定", lockTime);
        }

        /*
         执行登录功能
         */
        User user = userService.login(username, password);
        if (user == null) {
            /*
             登录失败，计数或锁定用户
             */
            Integer n = userService.afterloginFail(username);
            return JsonResult.err("登录失败", n);
        }
        /*
        登录成功，清空计数
         */
        userService.afterloginSuccess(username);
        return JsonResult.ok("登录成功", user);
    }

    @RequestMapping
    public String toLogin() {
        return "login";
    }

}
