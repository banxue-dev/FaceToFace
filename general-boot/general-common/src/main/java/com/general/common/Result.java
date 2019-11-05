package com.general.common;

import lombok.Data;

/**
 * @Description: 返回统一处理
 * @Author LuoJing
 * @Date 2019/11/1 16:29
 */
@Data
public class Result<T> {

    private Integer code;
    private String msg;
    private T data;

    /**
     * 默认是成功信息，code值为0
     */
    public Result() {
        this.code = 0;
    }

    /**
     * 错误结果构造函数
     *
     * @param msg 错误提示信息
     */
    public Result(String msg) {
        this.msg = msg;
        this.code = 10;
    }

    /**
     * 带错误码的构造函数
     *
     * @param code 错误编码
     */
    public Result(Integer code) {
        this.code = code;
    }

    /**
     * 错误编码和错误信息的构造函数
     *
     * @param code 错误编码
     * @param msg  错误信息
     */
    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 带结果数据的构造函数
     *
     * @param code 返回编码
     * @param msg  返回信息
     * @param data 返回的数据
     */
    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回数据结果的
     *
     * @param data
     */
    public Result(T data) {
        this.code = 0;
        this.data = data;
    }


}