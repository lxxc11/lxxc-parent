package com.lvxc.user.common.base;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * HTTP接口返回对象
 *
 * @author YangKuo 2018/8/20 15:35
 */
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     **/
    private int code;
    /**
     * 返回信息
     **/
    private String message;
    /**
     * 返回具体对象信息
     **/
    private T data;

    private String tid;

    public static ResponseResult<?> error(String s) {
        return error(ResultEnum.CODE_EXCEPTION.getCode(), s);
    }

    public String getTid() {
        return tid;
    }

    public ResponseResult<T> setTid(String tid) {
        this.tid = tid;
        return this;
    }

    private static final ResponseResult success = new ResponseResult(ResultEnum.CODE_SUCCESS.getCode());

    private static final ResponseResult error = new ResponseResult(ResultEnum.CODE_EXCEPTION);

    public ResponseResult() {

    }

    public ResponseResult(int code) {
        this.code = code;
    }

    public ResponseResult(int code, String message) {
        this.message = message;
        this.code = code;
    }

    public ResponseResult(IResultCode resultEnum) {
        this.message = resultEnum.getMsg();
        this.code = resultEnum.getCode();
    }

    public ResponseResult(IResultCode resultEnum, T data) {
        this.message = resultEnum.getMsg();
        this.code = resultEnum.getCode();
        this.data = data;
    }

    public ResponseResult(int code, String message, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public static <T> ResponseResult<T> success() {
        return (ResponseResult<T>) success;
    }

    public static <T> ResponseResult<T> success(T data) {
        if (data == null) {
            List<T> a = new ArrayList<>();
            data = (T) a;
        }
        return new ResponseResult<>(ResultEnum.CODE_SUCCESS, data);
    }

    public static <T> ResponseResult<T> success(String message, T data) {
        if (data == null) {
            List<T> a = new ArrayList<>();
            data = (T) a;
        }
        return new ResponseResult<>(ResultEnum.CODE_SUCCESS.getCode(), message, data);
    }

    public static <T> ResponseResult<T> error() {
        return (ResponseResult<T>) error;
    }

    public static ResponseResult<Object> error(IResultCode resultEnum) {
        ResponseResult<Object> result = new ResponseResult<>();
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getMsg());
        return result;
    }

    public static ResponseResult error(Integer code, String msg) {
        return new ResponseResult(code, msg);
    }

    public static ResponseResult error(ResultEnum resultEnum, String msg) {
        return error(resultEnum.getCode(), msg);
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public ResponseResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public boolean isSuccess() {
        return Objects.equals(this.code, ResultEnum.CODE_SUCCESS.getCode());
    }

    public boolean isError() {
        return !Objects.equals(this.code, ResultEnum.CODE_SUCCESS.getCode());
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
