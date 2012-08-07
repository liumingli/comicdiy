package com.ybcx.comic.facade;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.ybcx.comic.beans.Assets;
import com.ybcx.comic.beans.Cartoon;
import com.ybcx.comic.beans.Category;
import com.ybcx.comic.beans.Label;

public interface ComicServiceInterface {
	
	// 设置图片文件保存路径，由ApiAdaptor赋值
	public void saveImagePath(String filePath);

	public List<Assets> getAllAssets();
	
	public List<Assets> getAssetsByPage(int pageNum);
	
	public String createAsset(String name, String type, String price,
			String category, String label, String holiday, String assetPath,
			String thumbnailPath);

	public String deleteAssetById(String assetId);

	public List<Assets> searchByLabel(String labels);
	
	public List<Assets> searchByLabelAndType(String labels, String type);

	public Assets getAssetById(String assetId);

	public String updateAssetById(String assetId, String name, String price, String holiday);

	public List<Category> getAllCategory();

	public String createCategory(String name);

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

	public int getAssetCountByType(String type);

	
}
