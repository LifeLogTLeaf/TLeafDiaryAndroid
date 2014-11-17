package com.tleaf.tiary.model;

/** navigation drawer에 메뉴를 담는 모델 클래스 **/
public class MyMenuItem {

	private int myMenuIcon;
	private String myMenuTitle;

	public MyMenuItem(int myMenuIcon, String myMenuTitle) {
		this.myMenuIcon = myMenuIcon;
		this.myMenuTitle = myMenuTitle;
	}

	public int getMyMenuIcon() {
		return myMenuIcon;
	}

	public void setMyMenuIcon(int myMenuIcon) {
		this.myMenuIcon = myMenuIcon;
	}

	public String getMyMenuTitle() {
		return myMenuTitle;
	}

	public void setMyMenuTitle(String myMenuTitle) {
		this.myMenuTitle = myMenuTitle;
	}



	
	
}
