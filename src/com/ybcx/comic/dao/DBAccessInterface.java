package com.ybcx.comic.dao;

import java.util.List;
import java.util.Map;

import com.ybcx.comic.beans.Assets;
import com.ybcx.comic.beans.Cartoon;
import com.ybcx.comic.beans.Category;
import com.ybcx.comic.beans.Label;




public interface DBAccessInterface {

		public List<Assets> getAllAssets();

		public List<Assets> getAssetsByPage(int pageNum, int pageSize);
		
		public int createAsset(Assets asset);
		
		public int createAstcatRel(String id, String assetId, String category);

		public int createLabRel(List<Map<String, String>> list);
		
		public int updateCategoryHeat(String category);
		
		public int updateLabelHeat(String label);

		public int deleteAssetById(String assetId);

		public Assets getAssetById(String assetId);

		public int updateAssetById(String assetId, String name, String price, String holiday);
		
		public List<Assets> searchByLabelAnd(String[] labelArr);

		public List<Assets> searchByLabelOr(String labels);
		
		public List<Assets> searchByLabelTypeAnd(String[] labelArr,String type);

		public List<Assets> searchByLabelTypeOr(String labels,String type);

		public List<Category> getAllCategory();

		public int createCategory(String id, String name);

		public List<Label> getAllParentLabel();

		public List<Label> getLabelByParent(String parentId);

		public int createLabel(String id, String name, String parent);

		public int deleteLabel(String labelId);

		public int deleteLabelByParent(String parentId);

		public List<Assets> getAsssetsByType(String type, int page, int pageSize);
		
		public int getAssetCountByType(String type);

		public Cartoon getAnimationBy(String userId, String animId);
		
		public List<Cartoon> getAnimationsOf(String userId);

		public int saveAnimation(Cartoon cartoon);

		public List<Label> getAssetLabelsById(String assetId);

		public int createLocalImage(String id, String userId, String path,
				String uploadTime);

		public int updateAnimation(String animId, String content);


}
