package com.wetrade.eprest;

import com.google.gson.JsonElement;

public class BaseResponse {
    private ResponseStatus status;
    private String message;
    private JsonElement data;

    public BaseResponse(ResponseStatus status) {
        this.status = status;
    }

    public BaseResponse(ResponseStatus status, JsonElement json) {
        this.status = status;
        this.data = json;
    }

    public BaseResponse(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}
