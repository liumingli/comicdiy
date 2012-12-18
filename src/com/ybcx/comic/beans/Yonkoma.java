package com.ybcx.comic.beans;
/**
 * 四格漫画素材
 * @author liumingli
 *
 */
public class Yonkoma {

	private String id;
	private String name;
	private String thumbnail;
	private String longImg;
	private String swf;
	private String createTime;
	private int frame;
	private String type;
	private String parent;
	private int ad;
	private int enable;
	
	public int getEnable() {
		return enable;
	}
	public int getAd() {
		return ad;
	}
	public void setAd(int ad) {
		this.ad = ad;
	}
	public void setEnable(int enable) {
		this.enable = enable;
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
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getLongImg() {
		return longImg;
	}
	public void setLongImg(String longImg) {
		this.longImg = longImg;
	}
	public String getSwf() {
		return swf;
	}
	public void setSwf(String swf) {
		this.swf = swf;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getFrame() {
		return frame;
	}
	public void setFrame(int frame) {
		this.frame = frame;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	
}
