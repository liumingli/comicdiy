package com.ybcx.comic.facade;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.ybcx.comic.beans.Assets;
import com.ybcx.comic.beans.Cart;
import com.ybcx.comic.beans.Cartoon;
import com.ybcx.comic.beans.Category;
import com.ybcx.comic.beans.Friend;
import com.ybcx.comic.beans.Images;
import com.ybcx.comic.beans.Label;
import com.ybcx.comic.beans.UserDetail;

public interface ComicServiceInterface {
	
	// 设置图片文件保存路径，由ApiAdaptor赋值
	public void saveImagePath(String filePath);

	public List<Assets> getAllAssets();
	
	public List<Assets> getAssetsByPage(int pageNum);
	
	public String createAsset(String name, String type, String price,
			String category, String label, String holiday, String assetPath,
			String thumbnailPath);

	public String deleteAssetById(String assetId);

	public int searchByLabel(String labels);
	
	public List<Assets> searchByLabelPage(String labels, String pageNum);
	
	public int searchByLabelAndType(String labels, String type);
	
	public List<Assets> searchByLabelAndTypePage(String labels, String type, String pageNum);
	
	public List<Assets> getByCategoryAndType(String categorys, String type, String pageNum);

	public Assets getAssetById(String assetId);

	public String updateAssetById(String assetId, String name, String price, String holiday, String type, String labelIds);

	public List<Category> getAllCategory();

	public String createCategory(String name,String parent);

	public void getThumbnailFile(String relativePath, HttpServletResponse res);

	public void getAssetFile(String relativePath, HttpServletResponse res);

	public List<Label> getAllParentLabel();

	public List<Label> getLabelByParent(String parentId);

	public String createLabel(String name, String parent);

	public String deleteLabel(String labelId);

	public String deleteLabelByParent(String parentId);

	public List<Assets> getSysAssetsBy(String type, int page);
	
	public Cartoon getAnimationBy(String userId, String animId);

	public List<Cartoon> getAnimationsOf(String userId);

	public String createLocalImage(String userId, FileItem imgData);

	public String createAnimation(FileItem shotData, String userId, String name, String content);

	public String modifyAnimation(String animId, String content);

	public int getAssetCountByType(String type, String category);

	public List<Cartoon> getAllAnimation();

	public List<Images> getAllImages();

	public String examineAnim(String animId);

	public String examineImage(String imgId, String imgPath);

	public int searchAnimation(String keys);

	public List<Images> getImageByPage(String pageNum);

	public List<Cartoon> getAmimByPage(String pageNum);

	public int getAnimCount();

	public int getImageCount();

	public int getAllAssetsCount();

	public List<Cartoon> searchAnimationByPage(String keys, String pageNum);

	public String operateWeiboUser(String userId, String accessToken);

	public UserDetail getUserInfo(String userId);

	public String forwardToWeibo(String userId, String animId, String content);

	public List<Friend> getFriendByPage(String userId, String page);

	public String addAssetToCart(String userId, String assetId);

	public String deleteAssetFromCart(String userId, String assetId);

	public int getAssetState(String userId, String assetId);

	public String changeAssetState(String userId, String assetId);

	public List<Cart> getUserCartState(String userId);

	public String changeAssetState(String userId, int totalPrice);

	public Map<String, Integer> getStateByAssetIds(String userId, String assetIds);


}
