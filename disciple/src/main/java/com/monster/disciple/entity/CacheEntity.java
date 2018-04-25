package com.monster.disciple.entity;

import com.monster.disciple.response.BaseResponse;

public class CacheEntity<T extends BaseResponse> {

    private T response;

    public CacheEntity(T response) {
        this.response = response;
    }

    public CacheEntity() {
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public boolean isNil() {
        return null == response;
    }
}
