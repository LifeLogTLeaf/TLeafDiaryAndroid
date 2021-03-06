package com.tleaf.tiary.model;

/** 하나에 템플릿안에 다수의 템플릿컨텐츠를 가지고 이는 다양한 속성을 가지는 클래스로 정의한다 **/
public class TemplateContent {
	private long no;
	private long templateNo;
	private String question;
	private String content;
	private String front;
	private String end;
	private String type;
	
	public long getNo() {
		return no;
	}
	public void setNo(long no) {
		this.no = no;
	}
	public long getTemplateNo() {
		return templateNo;
	}
	public void setTemplateNo(long templateNo) {
		this.templateNo = templateNo;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFront() {
		return front;
	}
	public void setFront(String front) {
		this.front = front;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}

