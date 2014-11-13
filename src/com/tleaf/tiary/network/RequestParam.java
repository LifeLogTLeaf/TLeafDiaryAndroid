package com.tleaf.tiary.network;


/**
 * Created by jangyoungjin on 10/29/14.
 */
public class RequestParam {
    private String url;
    private HttpMethod httpMethod;
    private Request.Callback callback;
    private Object data;
    private TLeafSession session;
    private RequestQueryParam queryParam;

    public RequestParam() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Request.Callback getCallback() {
        return callback;
    }

    public void setCallback(Request.Callback callback) {
        this.callback = callback;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public TLeafSession getSession() {
        return session;
    }

    public void setSession(TLeafSession session) {
        this.session = session;
    }

    public RequestQueryParam getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(RequestQueryParam queryParam) {
        this.queryParam = queryParam;
    }
}
