package com.tleaf.tiary.network;

/**
 * Created by jangyoungjin on 10/29/14.
 */
public class TLeafSession {
    private String accessKey;
    private String userId;
    private String appId;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
