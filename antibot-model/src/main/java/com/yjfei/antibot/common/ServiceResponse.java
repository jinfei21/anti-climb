package com.yjfei.antibot.common;

import java.io.Serializable;

public class ServiceResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer code;
    private String resultMessage;
    public static final Integer SUCCESS_CODE = 200;

    public ServiceResponse() {
        this.setCode(SUCCESS_CODE);
        this.setResultMessage("");
    }

    public ServiceResponse(Integer code, String resultMessage) {
        this.setCode(code);
        this.setResultMessage(resultMessage);
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getResultMessage() {
        return this.resultMessage;
    }
}
