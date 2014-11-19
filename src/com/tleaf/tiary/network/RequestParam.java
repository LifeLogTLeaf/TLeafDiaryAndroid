package com.tleaf.tiary.network;

import java.util.ArrayList;

import org.apache.http.entity.mime.content.InputStreamBody;


/**
 * Created by jangyoungjin on 10/29/14.
 */
public class RequestParam {
	private String docId;
	private String docRev;
    private String url;
    private HttpMethod httpMethod;
    private Request.Callback callback;
    private Object data;
    private TLeafSession session;
    private RequestQueryParam queryParam;
    private ArrayList<String> files;

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

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getDocRev() {
		return docRev;
	}

	public void setDocRev(String docRev) {
		this.docRev = docRev;
	}

	public ArrayList<String> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<String> files) {
		this.files = files;
	}
}
