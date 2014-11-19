package com.tleaf.tiary.network;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by jangyoungjin on 10/29/14.
 */
public class RequestAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = RequestAsyncTask.class.getSimpleName();
    private static final String URL = "http://14.63.171.66:8081/tleafstructure/";
    private static final String USERID_HEADER_NAME = "x-tleaf-user-id";
    private static final String APPID_HEADER_NAME = "x-tleaf-application-id"; // Same as other company's API Key
    private static final String ACCESSKEY_HEADER_NAME = "x-tleaf-access-token";

    private RequestParam param;

    public RequestAsyncTask(RequestParam param) {
        this.param = param;
    }

    @Override
    protected String doInBackground(Void... params) {
        switch (param.getHttpMethod()) {
            case GET:
                getData();
                break;
            case POST:
                postData();
                break;
            case FILEPOST:
            	postFileData();
            	break;
            case DELETE:
            	deleteData();
            	break;
            default:
                break;
        }
        return null;
    }
    
    /**
     * 파일 데이터 쓰기 요청 [최대 10개 그리고 5MB아래로 요청가능]
     * @author : RichardJ
     * Date   : Nov 18, 2014 2:56:31 PM
     *
     */
    private void postFileData() {
        // Test only Post
        HttpClient client = initHttpClient();
        Log.i(TAG, "doc Id : " + param.getDocId());
        Log.i(TAG, "doc Rev : " + param.getDocRev());
        String paramString = "?docId=" + param.getDocId() + "&docRev=" + param.getDocRev();
        HttpPost httpPost = new HttpPost(URL + param.getUrl()+paramString);
        Log.i(TAG, "Http File Post : " + httpPost.getURI());
        
        
        //set Multipart file
        setMultipart(httpPost); 
        
        //set Header
        setHeaderParam(httpPost);
        try {
            // Execute
            StringBuilder builder = new StringBuilder();
            HttpResponse response = client.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();
            int resultCode = statusLine.getStatusCode();
            HttpEntity resultEntity = response.getEntity();
            InputStream result = resultEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(result));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            param.getCallback().onRecieve(getResponse(builder.toString(), resultCode));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 데이터 쓰기 요청
     * @author : RichardJ
     * Date   : Nov 18, 2014 2:56:42 PM
     *
     */
    @Deprecated
    private void postData() {
        // Test only Post
        HttpClient client = initHttpClient();
        HttpPost httpPost = new HttpPost(URL + param.getUrl());
        Log.i(TAG, "Http Post : " + httpPost.getURI());
        
        //set Entity
        Gson gson = new Gson();
        String str = gson.toJson(param.getData());
        Log.i(TAG, "Json String : " + str);
        
        setEntity(httpPost, str);
        
        //set Header
        setHeaderParam(httpPost);
        try {
            // Execute
            StringBuilder builder = new StringBuilder();
            /**
             * 여기서 HttpStatus.SEE_OTHER 로 에러를 받으면 예외처리 된다.
             * Received redirect response HTTP/1.1 303 See Other but no location header
             */
            HttpResponse response = client.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();
            int resultCode = statusLine.getStatusCode();
            HttpEntity resultEntity = response.getEntity();
            InputStream result = resultEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(result));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            param.getCallback().onRecieve(getResponse(builder.toString(), resultCode));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    /**
     * 데이터 삭제 요청
     * @author : RichardJ
     * Date   : Nov 18, 2014 2:56:55 PM
     *
     */
    private void deleteData() {
        // Test only Post
        HttpClient client = initHttpClient();
        HttpDelete httpDelete = new HttpDelete(URL + param.getUrl());
        Log.i(TAG, "Http Post : " + httpDelete.getURI());
        
        //set Entity
        Gson gson = new Gson();
        String str = gson.toJson(param.getData());
        Log.i(TAG, "Json String : " + str);
        
        
        //set Header
        setHeaderParam(httpDelete);
        try {
            // Execute
            StringBuilder builder = new StringBuilder();
            /**
             * 여기서 HttpStatus.SEE_OTHER 로 에러를 받으면 예외처리 된다.
             * Received redirect response HTTP/1.1 303 See Other but no location header
             */
            HttpResponse response = client.execute(httpDelete);

            StatusLine statusLine = response.getStatusLine();
            int resultCode = statusLine.getStatusCode();
            HttpEntity resultEntity = response.getEntity();
            InputStream result = resultEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(result));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            param.getCallback().onRecieve(getResponse(builder.toString(), resultCode));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 데이터 읽기 요청 
     * @author : RichardJ
     * Date   : Nov 18, 2014 2:57:20 PM
     *
     */
    private void getData() {
        HttpClient client = initHttpClient();
        HttpGet httpGet = new HttpGet(URL + param.getUrl());
        Log.i(TAG, "Http Get : " + httpGet.getURI());
        
        //set Header
        setHeaderParam(httpGet);
        try {
            // Execute
            StringBuilder builder = new StringBuilder();
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int resultCode = statusLine.getStatusCode();
            HttpEntity resultEntity = response.getEntity();
            InputStream result = resultEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(result));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            param.getCallback().onRecieve(getResponse(builder.toString(), resultCode));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * 제이슨 스트링 데이터를 바디에 셋팅한다.
     */
    private void setEntity(HttpPost httpPost, String str) {
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(str, "UTF-8");
            stringEntity.setContentType("application/json; charset=utf-8");
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(stringEntity);
    }

    /*
     * HTTP 클라이언트 초기
     */
    private HttpClient initHttpClient() {
        // Test only Post
        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        return client;
    }

    /*
     * 헤더에 파라미터를 셋팅한다.
     */
    private void setHeaderParam(HttpRequestBase httpRequest) {
    	Log.i(TAG, param.getSession().getAccessKey());
    	Log.i(TAG, param.getSession().getUserId());
    	Log.i(TAG, param.getSession().getAppId());
    	
        if(param.getSession() != null) {
            httpRequest.setHeader(ACCESSKEY_HEADER_NAME, param.getSession().getAccessKey());
            httpRequest.setHeader(USERID_HEADER_NAME, param.getSession().getUserId());
            httpRequest.setHeader(APPID_HEADER_NAME, param.getSession().getAppId());
        }
    }
    
    /*
     * 파일데이터를 요청 파라미터에 셋팅합니다.
     */
    private void setMultipart(HttpPost httpPost){
    	MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    	for(String path : param.getFiles()){
    		Log.i(TAG, "File Path : " + path);
    		
    		try {
    			InputStream inputStream = new FileInputStream(path);
    			InputStreamBody inputStreamBody = new InputStreamBody(inputStream, "image/jpeg", path);
    			
    			multipartEntityBuilder.addPart("file", inputStreamBody);
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
    	}
		httpPost.setEntity(multipartEntityBuilder.build());
    }

    /*
     * 응답데이터를 셋팅합니다.
     */
    private Response getResponse(String jsonString, int status){
        Response response = new Response();
        response.setJsonStringData(jsonString);
        response.setStatus(status);
        return response;
    }
    
}
