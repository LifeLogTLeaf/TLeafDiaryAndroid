package com.tleaf.tiary.network;

/**
 * Created by jangyoungjin on 10/29/14.
 */
public class Response {
    private String JsonStringData;
    private int status;

    public String getJsonStringData() {
        return JsonStringData;
    }

    public void setJsonStringData(String jsonStringData) {
        JsonStringData = jsonStringData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
