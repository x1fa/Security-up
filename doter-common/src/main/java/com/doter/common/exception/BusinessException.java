package com.doter.common.exception;


import com.doter.common.enums.ResultStatus;

/**
 * @ClassName BusinessException
 * @Description TODO
 * @Author HanTP
 * @Date 2020/7/3 8:02
 */

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 571377947470337019L;

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BusinessException(){

    }
    public BusinessException(String message){
        super(message);
        this.code=500;
    }
    public BusinessException(ResultStatus resultStatus){
        super(resultStatus.getMessage());
        this.code= resultStatus.getCode();
    }
    public BusinessException(ResultStatus resultStatus, String message){
        super(message);
        this.code= resultStatus.getCode();
    }

}
