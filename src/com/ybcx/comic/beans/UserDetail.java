package com.ybcx.comic.beans;

public class UserDetail {
	private String id;
	private String nickName;
	private String accessToken;
	private String avatarLarge;
	private String avatarMini;
	private int wealth;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAvatarLarge() {
		return avatarLarge;
	}
	public void setAvatarLarge(String avatarLarge) {
		this.avatarLarge = avatarLarge;
	}
	public String getAvatarMini() {
		return avatarMini;
	}
	public void setAvatarMini(String avatarMini) {
		this.avatarMini = avatarMini;
	}
	public int getWealth() {
		return wealth;
	}
	public void setWealth(int wealth) {
		this.wealth = wealth;
	}
	
}
