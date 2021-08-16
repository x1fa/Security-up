package com.doter.common.enums;

/**
 * @ClassName: Status
 * @Description: Status
 * @Author HanTP
 * @Date 2021/5/24 22:36
 * @Version 1.0
 */
public enum ResultStatus {

    /**
     * 返回值
     */
    ACCESS_DENIED(403, "权限不足！"),
    BAD_REQUEST(400, "请求失败！"),

    ERROR(500, "网络异常！"),
    HTTP_BAD_METHOD(405, "请求方式不支持！"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效") ,
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    REQUEST_NOT_FOUND(404, "请求不存在！"),
    SUCCESS(200, "操作成功！"),

    /* 用户错误 */
    TOKEN_EXPIRED(401, "登录信息已过期，请重新登录！"),
    TOKEN_OUT_OF_CTRL(401, "用户在别处登录，请更改密码或重新登录！"),
    TOKEN_PARSE_ERROR(401, "用户读取失败，请重新登录！"),
    USER_ACCOUNT_DISABLE(401, "账号被禁用"),
    USER_ACCOUNT_EXPIRED(401, "账号过期"),
    USER_ACCOUNT_LOCKED(401, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(401, "账号不存在"),

    USER_CREDENTIALS_ERROR(401, "密码错误"),
    USER_CREDENTIALS_EXPIRED(401, "密码过期"),
    USER_NOT_LOGIN(401, "用户未登录");

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ResultStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}