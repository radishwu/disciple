package com.monster.disciple.response;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {

    @SerializedName("code")
    private Integer code;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
