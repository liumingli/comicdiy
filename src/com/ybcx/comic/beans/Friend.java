package com.ybcx.comic.beans;

public class Friend {
	private String id;
	private String nickName;
	private String avatarLarge;
	private String avatarMini;
	private int totalNumber;
	
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
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
	
}
