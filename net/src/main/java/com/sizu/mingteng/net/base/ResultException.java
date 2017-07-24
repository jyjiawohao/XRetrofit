package com.sizu.mingteng.net.base;

/**
 * 介绍：友好的错误提示异常类
 * errorCode, errorMsg
 * 时间： 2017/2/22.
 */
public class ResultException extends RuntimeException {
    //默认是-10000
    private String errorCode = "-10000";

    /**
     * @param code 用于程序判断的code
     * @param msg  友好的提示
     */
    public ResultException(String code, String msg) {
        super(msg);
        errorCode = code;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ResultException setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    @Override
    public String toString() {
        return super.toString()+",ResultException{" +
                "errorCode='" + errorCode + '\'' +
                '}';
    }
}
