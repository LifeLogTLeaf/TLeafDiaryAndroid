package com.tleaf.tiary.model;

public class Weather {

	private String todayWeather;
	private float temperature;
	private float humidity;
	
	public Weather() {
	}
	
	public Weather(String todayWeather, float temperature, float humidity) {
		this.todayWeather = todayWeather;
		this.temperature = temperature;
		this.humidity = humidity;
	}
	
	public String getTodayWeather() {
		return todayWeather;
	}
	public void setTodayWeather(String todayWeather) {
		this.todayWeather = todayWeather;
	}
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public float getHumidity() {
		return humidity;
	}
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	
	
}
