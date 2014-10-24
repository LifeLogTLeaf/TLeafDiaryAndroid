package com.tleaf.tiary.model;

public class ExpandableItem {
	private String title;
	private String content;
	
	public ExpandableItem(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	

}
