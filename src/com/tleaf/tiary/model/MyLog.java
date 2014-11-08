package com.tleaf.tiary.model;


public class MyLog {
	protected String id;
	protected String rev;

	private double latitude;
	private double longitude;
//	private long locationTime; -> 일단은 time으로 하고 전송타임은  sendTime으로 따로 하는게 낫지 않을까요?

	private long date;
	private String type;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRev() {
		return rev;
	}
	public void setRev(String rev) {
		this.rev = rev;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
//	public long getLocationTime() {
//		return locationTime;
//	}
//	public void setLocationTime(long locationTime) {
//		this.locationTime = locationTime;
//	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}	
}
