package com.doter.common.core.result;

import com.doter.common.enums.ResultStatus;
import lombok.Data;


/**
 * @ClassName: Result
 * @Description: Result
 * @Author  HanTP
 * @Date 2021/5/24 22:35
 * @Version 1.0
 */
@Data
public class Result {
    private int code;
    private String message;
    private Object data;

    /**
     * 无参构造函数
     */
    private Result() {

    }

    /**
     * 全参构造函数
     * @param code
     * @param message
     * @param data
     */
    private Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     *
     *
     */
    public static Result success() {
        return new Result(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     *
     */
    public static Result success(Object data) {
        return new Result(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMessage(), data);
    }
    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     */
    public static Result success(Object data, String message) {
        return new Result(ResultStatus.SUCCESS.getCode(), message, data);
    }
    /**
     * 失败返回结果
     *
     * @param resultStatus
     * @return ResultUtil
     */
    public static Result failed(ResultStatus resultStatus) {
        return new Result(resultStatus.getCode(), resultStatus.getMessage(), null);
    }
    /**
     * 失败返回结果
     *
     * @param message
     * @return ResultUtil
     */
    public static Result failed(String message) {
        return new Result(ResultStatus.BAD_REQUEST.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param resultStatus
     * @return ResultUtil
     */
    public static Result failed(ResultStatus resultStatus, String message) {
        return new Result(resultStatus.getCode(), message, null);
    }
    /**
     * 失败返回结果
     *
     * @param code
     * @return ResultUtil
     */
    public static Result failed(Integer code, String message) {
        return new Result(code, message, null);
    }
}
