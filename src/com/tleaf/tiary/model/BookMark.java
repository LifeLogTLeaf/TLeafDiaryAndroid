package com.tleaf.tiary.model;

public class BookMark  extends MyLog{
	private long no;
	private String title;
	private String url;
	
	public BookMark(String title, String url, long date) {
		this.title = title;
		this.url = url;
		super.setDate(date);
	}
	public long getNo() {
		return no;
	}
	public void setNo(long no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}


}
