package com.tleaf.tiary.model;

import java.util.ArrayList;

public class Diary {
	long date;
	String image;
	String title;
	String content;
	ArrayList<String> tags;
	String folder;
	String location;
	String emotion;

	public Diary() {
		// TODO Auto-generated constructor stub
	}
	
	//날씨
	public Diary(long date,
			String image,
			String title,
			String content,
			ArrayList<String> tags,
			String folder,
			String location,
			String emotion) 
	{
		this.date = date;
		this.image = image;
		this.title = title;
		this.content = content;
		this.tags = tags;
		this.folder = folder;
		this.location = location;
		this.emotion = emotion;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getLocation() {
		return location;
	}

	public void setLocaton(String location) {
		this.location = location;
	}

	public String getEmotion() {
		return emotion;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}



}
