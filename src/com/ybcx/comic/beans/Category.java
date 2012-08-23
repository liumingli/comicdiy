package com.ybcx.comic.beans;

public class Category {

	private String id;
	private String name;
	private int heat;
	private String parent;
	
	
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHeat() {
		return heat;
	}
	public void setHeat(int heat) {
		this.heat = heat;
	}
	
}
