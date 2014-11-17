package com.tleaf.tiary.model;

import java.util.ArrayList;

public class MyTemplate {
	private long no;
	private String name;
	private String category;
	private String information;
	private String author;
	private ArrayList<TemplateContent> contentArr; 
	
	public long getNo() {
		return no;
	}
	public void setNo(long no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public ArrayList<TemplateContent> getContentArr() {
		return contentArr;
	}
	public void setContentArr(ArrayList<TemplateContent> contentArr) {
		this.contentArr = contentArr;
	}
	
	
	
}




