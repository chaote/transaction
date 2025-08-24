package com.hsbc.transaction.demo.model;

import com.hsbc.transaction.demo.common.ErrorCodeEnums;

import java.io.Serializable;

/**
 * @ author: chaote
 * @ date: 2025/8/21  21:08
 * @ <p><p>
 */

public class Response<T> implements Serializable{
    private String code;
    private String message;
    private T data;

    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T>Response<T> success(T data){
        return new Response<>("1000","success",data);
    }

    public static <T>Response<T> success(){
        return new Response<>("1000","success",null);
    }

    public static <T>Response<T> fail(ErrorCodeEnums errorCode){
        return new Response<>(errorCode.getErrorCode(), errorCode.getErrorName(), null);
    }

    public static <T>Response<T> validFail(String message){
        return new Response<>(ErrorCodeEnums.TRANS_ARG_ERROR.getErrorCode(), message,null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

