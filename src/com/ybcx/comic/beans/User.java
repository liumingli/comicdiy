package com.ybcx.comic.beans;

public class User {
	private String id;
	private int wealth;
	private String createTime;
	private String accessToken;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getWealth() {
		return wealth;
	}
	public void setWealth(int wealth) {
		this.wealth = wealth;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
}
