package com.example.service.impl;

import com.example.po.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/26 16:56
 */
@Service
public class UserServiceImpl implements UserService {

    @Value("${login.key.expire-min}")
    private Integer expireMin = 2;

    @Value("${login.lock.hour}")
    private Integer hour = 1;

    @Value("${login.try-limit}")
    private Integer tryLimit = 5;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public User login(String username, String password) {
        if (StringUtils.isEmpty(password)) {
            return null;
        }
        User user = selectByUsername(username);
        if (ObjectUtils.isEmpty(user) || !password.equals(user.getPassword())) {
            return null;
        }
        return user;
    }

    private User selectByUsername(String username) {
        User u = new User();
        u.setId("001");
        u.setUsername("admin");
        u.setPassword("1");
        return u;
    }

    @Override
    public Integer afterloginFail(String username) {
        String key = User.getLoginFailKey(username);
        // 记录登录错误次数
        redisTemplate.opsForValue().setIfAbsent(key, 0, Duration.ofMinutes(expireMin));
        int n = Math.toIntExact(redisTemplate.opsForValue().increment(key));
        if (n >= tryLimit) {
            redisTemplate.delete(key);
            String lockKey = User.getLoginTimeLockKey(username);
            redisTemplate.opsForValue().setIfAbsent(lockKey, hour, Duration.ofHours(hour));
            return 0;
        }
        return tryLimit - n;
    }

    @Override
    public String afterloginSuccess(String username) {
        String key = User.getLoginFailKey(username);
        redisTemplate.delete(key);
        return "错误记录清除成功";
    }

    @Override
    public Integer loginUserLock(String username) {
        // 查询当前key是否存在，如果存在，就被限制，注意，需要给用户提示信息：你当前用户已被限制，还剩多长时间
        // 如果不存在，就不被限制
        String key = User.getLoginTimeLockKey(username);
        if (redisTemplate.hasKey(key)) {
            return Math.toIntExact(redisTemplate.getExpire(key, TimeUnit.MINUTES));
        }

        return -1;
    }
}
