package com.example.client.additional;

public class Response {
    private Object description;
    private int httpCode;

    public Response(Object description, int httpCode) {
        this.description = description;
        this.httpCode = httpCode;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    @Override
    public String toString() {
        return "Response{" +
                "description=" + description +
                ", httpCode=" + httpCode +
                '}';
    }
}
