package com.htsc.transaction.demo.common;

/**
 * @ author: chaote
 * @ date: 2025/8/20  22:10
 * @ <p><p>
 */
public enum ErrorCodeEnums {



    TRANS_NOT_FOUND("1001","transaction not found"),
    TRANS_DUPLICATE("1002","duplicate transaction"),
    TRANS_ARG_ERROR("1003","validation failed"),

    TRANS_ERROR("9999","transaction error")
    ;

    private final String errorCode;
    private final String errorName;


    ErrorCodeEnums(String errorCode, String errorName) {
        this.errorCode = errorCode;
        this.errorName = errorName;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorName() {
        return errorName;
    }
}
