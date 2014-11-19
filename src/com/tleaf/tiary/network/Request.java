package com.tleaf.tiary.network;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
    public Request(String url, HttpMethod method, Callback callback, Object obj){
    	param = new RequestParam();
        this.initParam(null, url, method, callback, null, obj);
    }
    
    /**
     * Post Object
     * @param session
     * @param url
     * @param method
     * @param callback
     * @param obj
     */
    public Request(TLeafSession session, String url, HttpMethod method, Callback callback, Object obj){
    	param = new RequestParam();
    	Map<String, Object> wrapper = new HashMap<>();
    	wrapper.put("data", objToMap(obj));
        this.initParam(session, url, method, callback, null, wrapper);
    }
    
    /**
     * Post Image to Server from Internal Database
     * @param session
     * @param url
     * @param method
     * @param callback
     * @param docId
     * @param docRev
     * @param images
     */
    public Request(TLeafSession session, String url, HttpMethod method, Callback callback, String docId, String docRev, ArrayList<String> images){
    	param = new RequestParam();
    	param.setDocRev(docRev);
    	param.setDocId(docId);
    	param.setFiles(images);
    	this.initParam(session, url, method, callback, null, null);
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
    	param = new RequestParam();
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
    	param = new RequestParam();
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
    	param = new RequestParam();
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
	
	public Map<String, Object> objToMap(Object obj){
		Map<String, Object> data= new HashMap();
		
		Method[] methods = obj.getClass().getMethods();
		Field[] fields = obj.getClass().getDeclaredFields();
		if(fields.length == 0 ){ }
		for(Field f : fields){
			for(Method m : methods){
				String temp = (m.getName()).toLowerCase();
				if(temp.contains(f.getName())){
					if(m.getName().subSequence(0, 3).equals("get")){
						Object tempObj;
						try {
							tempObj = m.invoke(obj);
							if(tempObj != null){	
								data.put(f.getName(), tempObj);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} 
						
					}
				}
			}
		}
		return data;
	}


}
