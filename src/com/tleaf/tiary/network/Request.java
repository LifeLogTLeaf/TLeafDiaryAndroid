package com.tleaf.tiary.network;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jangyoungjin on 10/29/14.
 */
public class Request {
    private static final String TAG = Request.class.getSimpleName();

    private RequestParam param;

    /**
     * Login & signUp Request Constructor
     * @param url
     * @param method
     * @param callback
     * @param userInfo
     */
    public Request(String url, HttpMethod method, Callback callback, UserInfo userInfo){
        this.initParam(null, url, method, callback, null, userInfo);
    }

    /**
     * POST Request Constructor
     * @param session
     * @param url
     * @param httpMethod
     * @param callback
     * @param appData
     */
    public Request(TLeafSession session, String url, HttpMethod httpMethod, Callback callback, Map<String, Object> appData) {
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("data", appData);
        this.initParam(session,url,httpMethod,callback, null, wrapper);
    }

    /**
     * GET Request Constructor with Query Parameter
     * @param session
     * @param url
     * @param httpMethod
     * @param callback
     * @param queryParam
     */
    public Request(TLeafSession session, String url, HttpMethod httpMethod, Callback callback, RequestQueryParam queryParam) {
        this.initParam(session, url, httpMethod, callback, queryParam, null);
    }

    /**
     * GET Request Constructor
     * @param session
     * @param url
     * @param httpMethod
     * @param callback
     */
    public Request(TLeafSession session, String url, HttpMethod httpMethod, Callback callback) {
        this.initParam(session, url, httpMethod, callback, null, null);
    }

    /**
     * initialize Request Parameter
     * @param session
     * @param uri
     * @param httpMethod
     * @param callback
     * @param queryParam
     */
    public void initParam(TLeafSession session, String uri, HttpMethod httpMethod, Callback callback, RequestQueryParam queryParam, Object Data){
        param = new RequestParam();
        param.setUrl(uri);
        param.setHttpMethod(httpMethod);
        param.setCallback(callback);
        param.setSession(session);
        param.setQueryParam(queryParam);
        // Set Wraaper
        param.setData(Data);
    }

    /**
     * Run Request Task on the background
     */
    public void execute() {
        RequestAsyncTask task = new RequestAsyncTask(param);
        task.execute();
    }

    /**
     * Callback Interface
     */
    public interface Callback {
        void onRecieve(Response response);
    }


}
