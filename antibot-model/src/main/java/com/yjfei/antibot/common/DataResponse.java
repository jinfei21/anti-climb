package com.yjfei.antibot.common;

public class DataResponse<T> extends ServiceResponse {
    private T data;
    private static final long serialVersionUID = 1L;

    public DataResponse() {
    }

    public DataResponse(T data) {
        this(SUCCESS_CODE, data);
    }

    public DataResponse(Integer code, String resultMessage) {
        super(code, resultMessage);
    }

    public DataResponse(Integer code, T data) {
        super(code, "");
        this.setData(data);
    }

    public DataResponse(Integer code, String resultMessage, T data) {
        super(code, resultMessage);
        this.setData(data);
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }
}