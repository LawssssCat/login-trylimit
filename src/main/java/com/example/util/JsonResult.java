package com.example.util;

import lombok.Data;
import org.springframework.util.ObjectUtils;

/**
 * 简单的自定义返回类
 *
 * @author alan smith
 * @version 1.0
 * @date 2020/3/26 17:44
 */
@Data
public class JsonResult {
    /**
     * 返回码
     * 404 操作错误
     * 403 用户被锁定
     * 200 成功
     */
    private Integer code;
    private String message;
    private Object data;

    public static JsonResult lock(String message, Object data) {
        return err(message, data).code(403);
    }

    public static JsonResult ok(String message, Object data) {
        return build().code(200).msg(message).data(data);
    }

    public static JsonResult err(String message, Object data) {
        return build().code(404).msg(message).data(data);
    }

    public JsonResult data(Object data) {
        this.setData(data);
        return this;
    }

    public JsonResult msg(String message) {
        this.setMessage(message);
        return this;
    }

    public JsonResult code(Integer code) {
        this.setCode(code);
        return this;
    }

    private static JsonResult build() {
        return new JsonResult();
    }

}
