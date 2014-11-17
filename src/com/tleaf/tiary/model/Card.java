package com.tleaf.tiary.model;

public class Card extends MySms {
	private long cardNo;
	private String cardType;
	private long cardDate;
	private int spendedMoney;
	private String spendedPlace;
	private int leftMoney;
	
	
	public long getCardNo() {
		return cardNo;
	}
	public void setCardNo(long cardNo) {
		this.cardNo = cardNo;
	}
	
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public long getCardDate() {
		return cardDate;
	}
	public void setCardDate(long cardDate) {
		this.cardDate = cardDate;
	}
	public int getSpendedMoney() {
		return spendedMoney;
	}
	public void setSpendedMoney(int spendedMoney) {
		this.spendedMoney = spendedMoney;
	}
	public String getSpendedPlace() {
		return spendedPlace;
	}
	public void setSpendedPlace(String spendedPlace) {
		this.spendedPlace = spendedPlace;
	}
	public int getLeftMoney() {
		return leftMoney;
	}
	public void setLeftMoney(int leftMoney) {
		this.leftMoney = leftMoney;
	}
	
	



}
