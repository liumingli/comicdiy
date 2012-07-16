/**
 * 
 */
package com.ybcx.comic.facade;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ybcx.comic.beans.Assets;
import com.ybcx.comic.beans.Cartoon;
import com.ybcx.comic.beans.Category;
import com.ybcx.comic.beans.Label;




/**
 * Servlet调用服务的参数转换器，用来封装客户端参数并实现服务调用；
 * 
 * @author lwz
 * 
 */

public class ApiAdaptor {
	
	// 由Spring注入
	private ComicServiceInterface comicService;

	public void setComicService(ComicServiceInterface comicService) {
		this.comicService = comicService;
	}
	public ApiAdaptor() {

	}

	// 由AppStarter调用
	public void setImagePath(String filePath) {
		this.comicService.saveImagePath(filePath);
	}

	public String getAllAssets() {
		List<Assets> list = comicService.getAllAssets();
		JSONArray jsonArray = JSONArray.fromCollection(list);
		processPath(jsonArray);
		return jsonArray.toString();
	}

	//处理返回值
	private void processPath(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			//原素材路径
			String assetPath = jsonArray.getJSONObject(i).get("path").toString();
			if(!"".equals(assetPath)){
				//先从字符串中找到文件夹uploadFile的位置，再加上uploadFile的长度10，即可截取到下属文件路径
				int position = assetPath.lastIndexOf("uploadFile");
				String relativePath = assetPath.substring(position+10);
				jsonArray.getJSONObject(i).set("path", relativePath);
			}
			//缩略图路径
			String thumbnailPath = jsonArray.getJSONObject(i).get("thumbnail").toString();
			if(!"".equals(thumbnailPath)){
				//先从字符串中找到文件夹uploadFile的位置，再加上uploadFile的长度10，即可截取到下属文件路径
				int position = thumbnailPath.lastIndexOf("uploadFile");
				String relativePath = thumbnailPath.substring(position+10);
				jsonArray.getJSONObject(i).set("thumbnail", relativePath);
			}
		}
	}
		
	public String createAsset(String name, String type, String price,
			String category, String label, String holiday, String assetPath,
			String thumbnailPath) {
		String res = comicService.createAsset(name,type,price,category,label,holiday,assetPath,thumbnailPath);
		return res;
	}
	public String getAssetById(String assetId) {
		Assets asset = comicService.getAssetById(assetId);
		return  JSONObject.fromObject(asset).toString();
	}

	public String deleteAssetById(String assetId) {
		String result = comicService.deleteAssetById(assetId);
		return result;
	}
	
	public String updateAssetById(String assetId, String name, String price, String holiday) {
		String result = comicService.updateAssetById(assetId,name,price,holiday);
		return result;
	}
	
	public String searchByLabel(String labels) {
		List<Assets> list = comicService.searchByLabel(labels);
		JSONArray jsonArray = JSONArray.fromCollection(list);
		processPath(jsonArray);
		return jsonArray.toString();
	}
	
	public String getAllCategory() {
		List<Category> list = comicService.getAllCategory();
		return JSONArray.fromCollection(list).toString();
	}
	
	public String createCategory(String name) {
		String idVal = comicService.createCategory(name);
		return idVal;
	}
	
	public void getThumbnailFile(String relativePath, HttpServletResponse res) {
		comicService.getThumbnailFile(relativePath,res);
	}
	
	public void getAssetFile(String relativePath, HttpServletResponse res) {
		comicService.getAssetFile(relativePath,res);
	}
	
	public String getAllParentLabel() {
		List<Label> list = comicService.getAllParentLabel();
		return JSONArray.fromCollection(list).toString();
	}
	
	public String getLabelByParent(String parentId) {
		List<Label> list = comicService.getLabelByParent(parentId);
		return JSONArray.fromCollection(list).toString();
	}
	
	
	public String createLabel(String name, String parent) {
		String res = comicService.createLabel(name,parent);
		return res;
	}
	
	public String deleteLabel(String labelId) {
		String res = comicService.deleteLabel(labelId);
		return res;
	}
	
	public String deleteLabelByParent(String parentId) {
		String res = comicService.deleteLabelByParent(parentId);
		return res;
	}
	
	public String getSysAssetsBy(String type, int page) {
		List<Assets> list = comicService.getSysAssetsBy(type,page);
		JSONArray jsonArray = JSONArray.fromCollection(list);
		processPath(jsonArray);
		return jsonArray.toString();
	}
	
	public String getAnimationBy(String userId, String animId) {
		Cartoon cartoon = comicService.getAnimationBy(userId,animId);
		JSONObject jsonObject = JSONObject.fromBean(cartoon);
		processThumbnail(jsonObject);
		return jsonObject.toString();
	}
	
	private void processThumbnail(JSONObject jsonObject){
		//缩略图路径
		String thumbnailPath = jsonObject.get("thumbnail").toString();
		if(!"".equals(thumbnailPath)){
			//先从字符串中找到文件夹uploadFile的位置，再加上uploadFile的长度10，即可截取到下属文件路径
			int position = thumbnailPath.lastIndexOf("uploadFile");
			String relativePath = thumbnailPath.substring(position+10);
			jsonObject.set("thumbnail", relativePath);
		}
	}
	
	
	public String getAnimationsOf(String userId) {
		List<Cartoon> list = comicService.getAnimationsOf(userId);
		JSONArray jsonArray = JSONArray.fromCollection(list);
		processCartoon(jsonArray);
		return jsonArray.toString();
	}
	
	private void processCartoon(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			//DIY成品的缩略图
			String thumbnailPath = jsonArray.getJSONObject(i).get("thumbnail").toString();
			if(!"".equals(thumbnailPath)){
				//先从字符串中找到文件夹uploadFile的位置，再加上uploadFile的长度10，即可截取到下属文件路径
				int position = thumbnailPath.lastIndexOf("uploadFile");
				String relativePath = thumbnailPath.substring(position+10);
				jsonArray.getJSONObject(i).set("thumbnail", relativePath);
			}
		}
	}
	
	
	public String createLocalImage(List<FileItem> fileItems) {
		String result="";
		FileItem imgData = null;
		String userId = "";
		for (int i = 0; i < fileItems.size(); i++) {
			FileItem item = fileItems.get(i);
			if (!item.isFormField()) {
				//图片数据
				imgData = item;
			}
			
			if (item.isFormField()) {
				if (item.getFieldName().equals("userId")) {
					userId = item.getString();
				}
			}
		}
		String imgPath = comicService.createLocalImage(userId,imgData);
		
		//处理全路径，返回相对路径即可
		if(imgPath.contains("uploadFile")){
			//先从字符串中找到文件夹uploadFile的位置，再加上uploadFile的长度10，即可截取到下属文件路径
			int position = imgPath.lastIndexOf("uploadFile");
			result =  imgPath.substring(position+10);
		}else{
			result = imgPath;
		}
		
		return result;
	}
	
	public String createAnimation(List<FileItem> fileItems) {
		FileItem shotData = null;
		String userId = "";
		String name = "";
		String content = "";
		
		for (int i = 0; i < fileItems.size(); i++) {
			FileItem item = fileItems.get(i);
			if (!item.isFormField()) {
				//图片数据
				shotData = item;
			}
			
			if (item.isFormField()) {
				
				if (item.getFieldName().equals("userId")) {
					userId = item.getString();
				}
				
				if (item.getFieldName().equals("name")) {
					try {
						name = item.getString("UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				
				if (item.getFieldName().equals("content")) {
					content = item.getString();
				}
				
			}
		}//取参数完成
	
		String result = comicService.createAnimation(shotData,userId,name,content);
		
		return result;
		
	}
	
	public String modifyAnim(String animId, String content) {
		String result = comicService.modifyAnimation(animId,content);
		return result;
	}
	
	


} // end of class
