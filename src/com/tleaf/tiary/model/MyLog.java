package com.tleaf.tiary.model;

public class MyLog {
	protected String id;
	protected String rev;

	private double latitude;
	private double longitude;
	private long myNo;
	private String myAddress;
	private String myAddressName;
	private long date; //로그들의 발생 타임
	
	private long shackLogTime;
	private String shackLogType;
	
	public MyLog() {
		
	}
	
	public MyLog(double latitude, double longitude, String myAddress, String myAddressName, long date) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.myAddress = myAddress;
		this.myAddressName = myAddressName;
		this.date = date;
	}	
	
	public long getMyNo() {
		return myNo;
	}

	public void setMyNo(long myNo) {
		this.myNo = myNo;
	}

	public String getMyAddressName() {
		return myAddressName;
	}
	public void setMyAddressName(String myAddressName) {
		this.myAddressName = myAddressName;
	}
	
	public String getMyAddress() {
		return myAddress;
	}
	public void setMyAddress(String myAddress) {
		this.myAddress = myAddress;
	}
	public long getShackLogTime() {
		return shackLogTime;
	}
	public void setShackLogTime(long shackLogTime) {
		this.shackLogTime = shackLogTime;
	}
	public String getShackLogType() {
		return shackLogType;
	}
	public void setShackLogType(String shackLogType) {
		this.shackLogType = shackLogType;
	}
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

	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}

}
