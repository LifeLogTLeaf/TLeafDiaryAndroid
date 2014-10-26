package com.tleaf.tiary.model;

import java.util.ArrayList;

//"'" + diary.getDate() + "', " +
//"'" + diary.getTitle() + "', " +
//"'" + diary.getContent() + "', " +
//"'" + diary.getEmotion() + "', " +
//"'" + diary.getImage() + "', " +
//"'" + diary.getTags() + "', " +
//"'" + diary.getFolder() + "', " +
//"'" + diary.getLocation() +"')";		

public class Diary {
	private long date;
	private String title;
	private String content;
	private String emotion;
	private ArrayList<String> images;
	private ArrayList<String> tags;
	private ArrayList<String> folders;
	private String location;
	private Weather weather;

	public Diary() {
	}

	//날씨
	public Diary(long date,
			String title,
			String content,
			String emotion,
			ArrayList<String> images,
			ArrayList<String> tags,
			ArrayList<String> folders,
			String location,
			Weather weather) 
	{
		this.date = date;
		this.title = title;
		this.content = content;
		this.emotion = emotion;
		this.images = images;
		this.tags = tags;
		this.folders = folders;
		this.location = location;
		this.weather = weather;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
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

	public String getEmotion() {
		return emotion;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public ArrayList<String> getFolders() {
		return folders;
	}

	public void setFolders(ArrayList<String> folders) {
		this.folders = folders;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}


}
