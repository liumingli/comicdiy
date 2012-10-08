package com.ybcx.comic.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.ybcx.comic.beans.Assets;
import com.ybcx.comic.beans.Cart;
import com.ybcx.comic.beans.Cartoon;
import com.ybcx.comic.beans.Category;
import com.ybcx.comic.beans.Images;
import com.ybcx.comic.beans.Label;
import com.ybcx.comic.beans.User;
import com.ybcx.comic.dao.DBAccessInterface;
import com.ybcx.comic.utils.ComicUtils;



public class DBAccessImplement  implements DBAccessInterface {

	private JdbcTemplate jdbcTemplate;

	// Inject by Spring
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	// Constructor
	public DBAccessImplement() {

	}

	@Override
	public List<Assets> getAllAssets() {
		List<Assets> resList = new ArrayList<Assets>();
		String sql = "select a.a_id,a.a_holiday,a.a_name,a.a_type,a.a_thumbnail,a.a_path,a.a_uploadTime,a.a_price,a.a_heat,a.a_enable,c.c_name" +
				" from t_assets a, t_category c, t_astcat_rel r " +
				"where a.a_id=r.acr_assets and c.c_id=r.acr_category and a.a_enable = 1" +
				" order by a.a_uploadTime desc";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Assets asset = new Assets();
				asset.setId(map.get("a_id").toString());
				asset.setCategory(map.get("c_name").toString());
				asset.setHoliday(map.get("a_holiday").toString());
				asset.setName(map.get("a_name").toString());
				asset.setType(map.get("a_type").toString());
				asset.setThumbnail(map.get("a_thumbnail").toString());
				asset.setPath(map.get("a_path").toString());
				asset.setUploadTime(map.get("a_uploadTime").toString());
				asset.setPrice(Float.parseFloat(map.get("a_price").toString()));
				asset.setHeat(Integer.parseInt(map.get("a_heat").toString()));
				asset.setEnable(Integer.parseInt(map.get("a_enable").toString()));
				resList.add(asset);
			}
		}
		return resList;
	}
	
	@Override
	public int getAssetCount() {
		String sql = "select count(a_id) from t_assets where a_enable=1";
		int rows = jdbcTemplate.queryForInt(sql);
		return rows;
	}
	
	@Override
	public List<Assets> getAssetsByPage(int pageNum, int pageSize) {
		List<Assets> resList = new ArrayList<Assets>();
		int startLine = (pageNum -1)*pageSize;
		String sql = "select a.a_id,a.a_holiday,a.a_name,a.a_type,a.a_thumbnail,a.a_path,a.a_uploadTime,a.a_price,a.a_heat,a.a_enable,c.c_name" +
				" from t_assets a, t_category c, t_astcat_rel r " +
				"where a.a_id=r.acr_assets and c.c_id=r.acr_category and a.a_enable = 1" +
				" order by a.a_uploadTime desc limit "+startLine+","+pageSize;
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Assets asset = new Assets();
				asset.setId(map.get("a_id").toString());
				asset.setCategory(map.get("c_name").toString());
				asset.setHoliday(map.get("a_holiday").toString());
				asset.setName(map.get("a_name").toString());
				asset.setType(map.get("a_type").toString());
				asset.setThumbnail(map.get("a_thumbnail").toString());
				asset.setPath(map.get("a_path").toString());
				asset.setUploadTime(map.get("a_uploadTime").toString());
				asset.setPrice(Float.parseFloat(map.get("a_price").toString()));
				asset.setHeat(Integer.parseInt(map.get("a_heat").toString()));
				asset.setEnable(Integer.parseInt(map.get("a_enable").toString()));
				resList.add(asset);
			}
		}
		return resList;
	}
	
	@Override
	public int createAsset(final Assets asset) {
		String sql = "INSERT INTO t_assets "
				+ "(a_id, a_name, a_price , a_path, a_thumbnail, a_uploadTime, a_holiday, a_type, a_heat, a_enable ,a_memo) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		int res =jdbcTemplate.update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, asset.getId());
					ps.setString(2, asset.getName());
					ps.setFloat(3, asset.getPrice());
					ps.setString(4, asset.getPath());
					ps.setString(5, asset.getThumbnail());
					ps.setString(6,asset.getUploadTime());
					ps.setString(7, asset.getHoliday());
					ps.setString(8, asset.getType());
					ps.setInt(9, asset.getHeat());
					ps.setInt(10, asset.getEnable());
					ps.setString(11, "");

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});

		return res;
	}

	@Override
	public int createAstcatRel(String id, String assetId, String category) {
		String sql = "insert into t_astcat_rel(acr_id,acr_category,acr_assets,acr_memo) values('"+id+"', '"+category+"', '"+assetId+"', '')";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public int createLabRel(final List<Map<String, String>> list) {
		String sql = "insert into t_astlab_rel(alr_id,alr_label,alr_assets,alr_memo) values(?, ?, ?, ?)";
		int[] res = jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						Map<String,String> map = list.get(i);
						for(String labelId : map.keySet()){
							String assetId =  map.get(labelId);
							ps.setString(1, ComicUtils.generateUID());
							ps.setString(2, labelId);
							ps.setString(3, assetId);
							ps.setString(4, "");
						}
					}

					public int getBatchSize() {
						return list.size();
					}
				});
		return res.length;
	}

	@Override
	public int updateCategoryHeat(String category) {
		String sql = "update t_category set c_heat=c_heat+1 where c_id ='"+category+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public int updateLabelHeat(String label) {
		String sql = "update t_label set l_heat=l_heat+1 where l_id in("+label+")";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}


	@Override
	public int deleteAssetById(String assetId) {
		String sql = "update t_assets set a_enable = 0 where a_id ='"+assetId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public Assets getAssetById(String assetId) {
		Assets asset = new Assets();
		//String sql = "select * from t_assets where a_id='"+assetId+"'";
		String sql = "select a.a_id,a.a_holiday,a.a_name,a.a_type,a.a_thumbnail,a.a_path,a.a_uploadTime,a.a_price,a.a_heat,a.a_enable,c.c_name" +
				" from t_assets a, t_category c, t_astcat_rel r " +
				"where a.a_id=r.acr_assets and c.c_id=r.acr_category and  a_id='"+assetId+"'";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				asset.setId(map.get("a_id").toString());
				asset.setCategory(map.get("c_name").toString());
				asset.setHoliday(map.get("a_holiday").toString());
				asset.setName(map.get("a_name").toString());
				asset.setType(map.get("a_type").toString());
				asset.setThumbnail(map.get("a_thumbnail").toString());
				asset.setPath(map.get("a_path").toString());
				asset.setUploadTime(map.get("a_uploadTime").toString());
				asset.setPrice(Float.parseFloat(map.get("a_price").toString()));
				asset.setHeat(Integer.parseInt(map.get("a_heat").toString()));
				asset.setEnable(Integer.parseInt(map.get("a_enable").toString()));
			}
		}
		return asset;
	}

	@Override
	public int updateAssetById(String assetId, String name, String price, String holiday, String type) {
		Float priceVal = Float.parseFloat(price);
		String sql = "update t_assets set a_name='"+name+"', a_price="+priceVal+", a_holiday ='"+ holiday+"', a_type='"+type+"' where a_id='"+assetId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}
	
	@Override
	public List<Assets> searchByLabelAnd(String[] labelArr) {
		List<Assets> resList = new ArrayList<Assets>();
		
		StringBuffer labelAnd = new StringBuffer();
		
		for(int i=0;i<labelArr.length;i++){
			if(labelAnd.length()>0){
				labelAnd.append(" and ");
			}
			labelAnd.append("l.l_name like '%"+labelArr[i].trim()+"%'");
		}
		
		String sql ="select distinct a.a_id,a.a_holiday,a.a_name,a.a_type,a.a_thumbnail,a.a_path,a.a_uploadTime,a.a_price,a.a_heat,a.a_enable,c.c_name" +
				" from t_assets a, t_label l, t_astlab_rel alr, t_category c, t_astcat_rel acr " +
				"where a.a_id=acr.acr_assets and c.c_id=acr.acr_category and a.a_id = alr.alr_assets and l.l_id = alr.alr_label " +
				"and "+ labelAnd.toString() +" and a.a_enable =1 order by a.a_heat desc, a.a_uploadTime desc";
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Assets asset = new Assets();
				asset.setId(map.get("a_id").toString());
				asset.setHoliday(map.get("a_holiday").toString());
				asset.setName(map.get("a_name").toString());
				asset.setCategory(map.get("c_name").toString());
				asset.setType(map.get("a_type").toString());
				asset.setThumbnail(map.get("a_thumbnail").toString());
				asset.setPath(map.get("a_path").toString());
				asset.setUploadTime(map.get("a_uploadTime").toString());
				asset.setPrice(Float.parseFloat(map.get("a_price").toString()));
				asset.setHeat(Integer.parseInt(map.get("a_heat").toString()));
				asset.setEnable(Integer.parseInt(map.get("a_enable").toString()));
				resList.add(asset);
			}
		}
		return resList;
	}
	
	@Override
	public int searchByLabelCount(String labelIds) {
		String sql = "select count(distinct a.a_id) from t_assets a, t_label l, t_astlab_rel r " +
				"where a.a_id = r.alr_assets and l.l_id = r.alr_label and  l.l_id in ("+ labelIds +")";
		int rows = jdbcTemplate.queryForInt(sql);
		return rows;
	}

	@Override
	public List<Assets> searchByLabelOr(String[] labelArr, int pageSize, int pageNum) {
		int startLine = (pageNum -1)*pageSize;
		List<Assets> resList = new ArrayList<Assets>();
		StringBuffer labelOr = new StringBuffer();
		
		for(int i=0;i<labelArr.length;i++){
			if(labelOr.length()>0){
				labelOr.append(" or ");
			}
			labelOr.append("l.l_name like '%"+labelArr[i].trim()+"%'");
		}
		String sql ="select distinct a.a_id,a.a_holiday,a.a_name,a.a_type,a.a_thumbnail,a.a_path,a.a_uploadTime,a.a_price,a.a_heat,a.a_enable,c.c_name" +
				" from t_assets a, t_label l, t_astlab_rel r, t_category c, t_astcat_rel acr" +
				" where a.a_id=acr.acr_assets and c.c_id=acr.acr_category and a.a_id = r.alr_assets and l.l_id = r.alr_label " +
				"and ("+ labelOr.toString() +") and a.a_enable =1 order by a.a_heat desc, a.a_uploadTime desc limit "+startLine+","+pageSize;
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Assets asset = new Assets();
				asset.setId(map.get("a_id").toString());
				asset.setHoliday(map.get("a_holiday").toString());
				asset.setName(map.get("a_name").toString());
				asset.setCategory(map.get("c_name").toString());
				asset.setType(map.get("a_type").toString());
				asset.setThumbnail(map.get("a_thumbnail").toString());
				asset.setPath(map.get("a_path").toString());
				asset.setUploadTime(map.get("a_uploadTime").toString());
				asset.setPrice(Float.parseFloat(map.get("a_price").toString()));
				asset.setHeat(Integer.parseInt(map.get("a_heat").toString()));
				asset.setEnable(Integer.parseInt(map.get("a_enable").toString()));
				resList.add(asset);
			}
		}
		return resList;
	}
	
	@Override
	public List<Assets> searchByLabelTypeAnd(String[] labelArr,String type) {
		List<Assets> resList = new ArrayList<Assets>();
		
		StringBuffer labelAnd = new StringBuffer();
		
		for(int i=0;i<labelArr.length;i++){
			if(labelAnd.length()>0){
				labelAnd.append(" and ");
			}
			labelAnd.append("l.l_name like '%"+labelArr[i].trim()+"%'");
		}
		
		String sql ="select distinct a.a_id,a.a_holiday,a.a_name,a.a_type,a.a_thumbnail,a.a_path,a.a_uploadTime,a.a_price,a.a_heat,a.a_enable,c.c_name" +
				" from t_assets a, t_label l, t_astlab_rel alr, t_category c, t_astcat_rel acr " +
				"where a.a_id=acr.acr_assets and c.c_id=acr.acr_category and a.a_id = alr.alr_assets and l.l_id = alr.alr_label " +
				"and "+ labelAnd.toString() +" and a.a_type='"+type+"' and a.a_enable =1 order by a.a_heat desc, a.a_uploadTime desc";
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Assets asset = new Assets();
				asset.setId(map.get("a_id").toString());
				asset.setHoliday(map.get("a_holiday").toString());
				asset.setName(map.get("a_name").toString());
				asset.setCategory(map.get("c_name").toString());
				asset.setType(map.get("a_type").toString());
				asset.setThumbnail(map.get("a_thumbnail").toString());
				asset.setPath(map.get("a_path").toString());
				asset.setUploadTime(map.get("a_uploadTime").toString());
				asset.setPrice(Float.parseFloat(map.get("a_price").toString()));
				asset.setHeat(Integer.parseInt(map.get("a_heat").toString()));
				asset.setEnable(Integer.parseInt(map.get("a_enable").toString()));
				resList.add(asset);
			}
		}
		return resList;
	}
	
	@Override
	public int searchByLabelTypeCount(String labelIds, String type) {
		String sql ="select count(distinct a.a_id) from t_assets a, t_label l, t_astlab_rel alr " +
				"where l.l_id in("+labelIds+") and a.a_type = '"+type+"' " +
				"and l.l_id = alr.alr_label and a.a_id=alr.alr_assets and a.a_enable =1";
		int count = jdbcTemplate.queryForInt(sql);
		return count;
	}


	@Override
	public List<Assets> searchByLabelTypeOr(String[] labelArr,String type, int pageSize, int pageNum) {
		int startLine = (pageNum -1)*pageSize;
		List<Assets> resList = new ArrayList<Assets>();
		StringBuffer labelOr= new StringBuffer();
		
		for(int i=0;i<labelArr.length;i++){
			if(labelOr.length()>0){
				labelOr.append(" or ");
			}
			labelOr.append("l.l_name like '%"+labelArr[i].trim()+"%'");
		}
		String sql ="select distinct a.a_id,a.a_holiday,a.a_name,a.a_type,a.a_thumbnail,a.a_path,a.a_uploadTime,a.a_price,a.a_heat,a.a_enable,c.c_name" +
				" from t_assets a, t_label l, t_astlab_rel r, t_category c, t_astcat_rel acr" +
				" where a.a_id=acr.acr_assets and c.c_id=acr.acr_category and a.a_id = r.alr_assets and l.l_id = r.alr_label " +
				"and ("+ labelOr.toString() +") and a.a_type='"+type+"' and a.a_enable =1 order by a.a_heat desc, a.a_uploadTime desc limit "+startLine+","+pageSize;
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Assets asset = new Assets();
				asset.setId(map.get("a_id").toString());
				asset.setHoliday(map.get("a_holiday").toString());
				asset.setName(map.get("a_name").toString());
				asset.setCategory(map.get("c_name").toString());
				asset.setType(map.get("a_type").toString());
				asset.setThumbnail(map.get("a_thumbnail").toString());
				asset.setPath(map.get("a_path").toString());
				asset.setUploadTime(map.get("a_uploadTime").toString());
				asset.setPrice(Float.parseFloat(map.get("a_price").toString()));
				asset.setHeat(Integer.parseInt(map.get("a_heat").toString()));
				asset.setEnable(Integer.parseInt(map.get("a_enable").toString()));
				resList.add(asset);
			}
		}
		return resList;
	}

	@Override
	public List<Category> getAllCategory() {
		List<Category> resList = new ArrayList<Category>();
		String sql = "select * from t_category order by CONVERT( c_name USING gbk )";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Category category = new Category();
				category.setId(map.get("c_id").toString());
				category.setName(map.get("c_name").toString());
				category.setParent(map.get("c_parent").toString());
				category.setHeat(Integer.parseInt(map.get("c_heat").toString()));
				resList.add(category);
			}
		}
		return resList;
	}

	@Override
	public int createCategory(String id, String name, String parent) {
		String sql = "insert into t_category(c_id,c_name,c_parent,c_heat, c_memo) values('"+id+"', '"+name+"','"+parent+"', 0, '')";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public List<Label> getAllParentLabel() {
		List<Label> resList = new ArrayList<Label>();
		String sql = "select * from t_label where l_parent='parent' order by CONVERT( l_name USING gbk )";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Label lab = new Label();
				lab.setId(map.get("l_id").toString());
				lab.setName(map.get("l_name").toString());
				lab.setHeat(Integer.parseInt(map.get("l_heat").toString()));
				lab.setParent(map.get("l_parent").toString());
				resList.add(lab);
			}
		}
		return resList;
	}

	@Override
	public List<Label> getLabelByParent(String parentId) {
		List<Label> resList = new ArrayList<Label>();
		String sql = "select * from t_label where l_parent='"+parentId+"' order by CONVERT( l_name USING gbk )";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Label lab = new Label();
				lab.setId(map.get("l_id").toString());
				lab.setName(map.get("l_name").toString());
				lab.setHeat(Integer.parseInt(map.get("l_heat").toString()));
				lab.setParent(map.get("l_parent").toString());
				resList.add(lab);
			}
		}
		return resList;
	}


	@Override
	public int createLabel(String id, String name, String parent) {
		String sql = "insert into t_label values('"+id+"', '"+name+"', 0,'"+parent+"', '')";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public int deleteLabel(String labelId) {
		String sql = "delete from t_label where l_id ='"+labelId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public int deleteLabelByParent(String parentId) {
		String sql = "delete from t_label where l_parent ='"+parentId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public List<Assets> getAsssetsByType(String type, int page, int pageSize) {
		List<Assets> resList = new ArrayList<Assets>();
		int startLine = (page -1)*pageSize;
		String sql = "select * from t_assets where a_type='"+type+"' and a_enable=1 order by a_heat desc, a_uploadTime desc limit "+startLine+","+pageSize;
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Assets asset = new Assets();
				asset.setId(map.get("a_id").toString());
				asset.setHoliday(map.get("a_holiday").toString());
				asset.setName(map.get("a_name").toString());
				asset.setType(map.get("a_type").toString());
				asset.setThumbnail(map.get("a_thumbnail").toString());
				asset.setPath(map.get("a_path").toString());
				asset.setUploadTime(map.get("a_uploadTime").toString());
				asset.setPrice(Float.parseFloat(map.get("a_price").toString()));
				asset.setHeat(Integer.parseInt(map.get("a_heat").toString()));
				asset.setEnable(Integer.parseInt(map.get("a_enable").toString()));
				resList.add(asset);
			}
		}
		return resList;
	}
	
	@Override
	public int getAssetCountByType(String type) {
		String sql = "select count(a_id) from t_assets where a_type='"+type+"' and a_enable=1";
		int rows = jdbcTemplate.queryForInt(sql);
		return rows;
	}
	
	@Override
	public int getAssetCountByTypeAndCategory(String type, String category) {
		String sql = "select count(a.a_id) from t_assets a, t_category c, t_astcat_rel r " +
				"where a.a_id=r.acr_assets and c.c_id =r.acr_category and c.c_name='" +category+"' " +
				"and a.a_type='"+type+"' and a.a_enable=1 order by a.a_heat desc, a.a_uploadTime desc";
		int rows = jdbcTemplate.queryForInt(sql);
		return rows;
	}

	@Override
	public Cartoon getAnimationBy(String userId, String animId) {
		Cartoon cartoon = new Cartoon();
		String sql = "select * from t_cartoon where c_id='"+animId+"' and c_owner='"+userId+"' and c_enable=1";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				cartoon.setId(map.get("c_id").toString());
				cartoon.setName(map.get("c_name").toString());
				cartoon.setOwner(map.get("c_owner").toString());
				cartoon.setContent(map.get("c_content").toString());
				cartoon.setCreateTime(map.get("c_createTime").toString());
				cartoon.setThumbnail(map.get("c_thumbnail").toString());
				cartoon.setEnable(Integer.parseInt(map.get("c_enable").toString()));
			}
		}
		return cartoon;
	}

	@Override
	public List<Cartoon> getAnimationsOf(String userId) {
		List<Cartoon> list = new ArrayList<Cartoon>();
		String sql = "select * from t_cartoon where c_owner='"+userId+"' and c_enable=1 order by c_createTime desc";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Cartoon cartoon = new Cartoon();
				cartoon.setId(map.get("c_id").toString());
				cartoon.setName(map.get("c_name").toString());
				cartoon.setOwner(map.get("c_owner").toString());
				cartoon.setContent(map.get("c_content").toString());
				cartoon.setCreateTime(map.get("c_createTime").toString());
				cartoon.setThumbnail(map.get("c_thumbnail").toString());
				cartoon.setEnable(Integer.parseInt(map.get("c_enable").toString()));
				list.add(cartoon);
			}
		}
		return list;
	}

	@Override
	public int saveAnimation(final Cartoon cartoon) {
		String sql = "INSERT INTO t_cartoon "
				+ "(c_id, c_name, c_thumbnail, c_content, c_owner, c_createTime, c_enable, c_memo) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		int res =jdbcTemplate.update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, cartoon.getId());
					ps.setString(2, cartoon.getName());
					ps.setString(3, cartoon.getThumbnail());
					ps.setString(4, cartoon.getContent());
					ps.setString(5, cartoon.getOwner());
					ps.setString(6, cartoon.getCreateTime());
					ps.setInt(7, cartoon.getEnable());
					ps.setString(8,"");

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});

		return res;
	}

	@Override
	public List<Label> getAssetLabelsById(String assetId) {
		List<Label> resList = new ArrayList<Label>();
		String sql = "select * from t_label l, t_astlab_rel r where l.l_id = r.alr_label and r.alr_assets='"+assetId+"'";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Label label = new Label();
				label.setId(map.get("l_id").toString());
				label.setName(map.get("l_name").toString());
				label.setHeat(Integer.parseInt(map.get("l_heat").toString()));
				resList.add(label);
			}
		}
		return resList;
	}

	@Override
	public int createLocalImage(final String id, final String userId, final String path,
			final String uploadTime) {
		String sql = "insert into t_images (i_id,i_path,i_uploadTime,i_owner,i_enable,i_memo) values(?,?,?,?,?,?)";
		int res =jdbcTemplate.update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, id);
					ps.setString(2, path);
					ps.setString(3, uploadTime);
					ps.setString(4, userId);
					ps.setInt(5, 1);
					ps.setString(6,"");

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});

		return res;
	}

	@Override
	public int updateAnimation(String animId, String content) {
		String sql = "update t_cartoon set c_content ='"+content+"' where c_id='"+animId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public List<Cartoon> getAllAnimation() {
		List<Cartoon> list = new ArrayList<Cartoon>();
		String sql = "select * from t_cartoon where c_enable=1 order by c_createTime desc";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Cartoon cartoon = new Cartoon();
				cartoon.setId(map.get("c_id").toString());
				cartoon.setName(map.get("c_name").toString());
				cartoon.setOwner(map.get("c_owner").toString());
				cartoon.setContent(map.get("c_content").toString());
				cartoon.setCreateTime(map.get("c_createTime").toString());
				cartoon.setThumbnail(map.get("c_thumbnail").toString());
				cartoon.setEnable(Integer.parseInt(map.get("c_enable").toString()));
				list.add(cartoon);
			}
		}
		return list;
	}

	@Override
	public List<Images> getAllImages() {
		List<Images> list = new ArrayList<Images>();
		String sql = "select * from t_images where i_enable=1 order by i_uploadTime desc";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Images img = new Images();
				img.setId(map.get("i_id").toString());
				img.setOwner(map.get("i_owner").toString());
				img.setUploadTime(map.get("i_uploadTime").toString());
				img.setPath(map.get("i_path").toString());
				img.setEnable(Integer.parseInt(map.get("i_enable").toString()));
				list.add(img);
			}
		}
		return list;
	}

	@Override
	public int updateAnimById(String animId) {
		String sql = "update t_cartoon set c_enable = 0 where c_id='"+animId+"'";
//		String sql = "delete from t_cartoon where c_id='"+animId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public int updateImageById(String imgId) {
		String sql = "update t_images set i_enable = 0 where i_id='"+imgId+"'";
//		String sql = "delete from t_images where i_id='"+imgId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}


	@Override
	public int searchAnimation(String keys) {
		String sql = "select count(distinct c_id) from t_cartoon where c_enable=1 and c_name like '%"+keys+"%'";
		int rows = jdbcTemplate.queryForInt(sql);
		return rows;
	}
	
	@Override
	public List<Cartoon> searchAnimationByPage(String keys,int pageNum,int pageSize) {
		List<Cartoon> list = new ArrayList<Cartoon>();
		String sql = "select * from t_cartoon where c_enable=1 and c_name like '%"+keys+"%' order by c_createTime desc";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Cartoon cartoon = new Cartoon();
				cartoon.setId(map.get("c_id").toString());
				cartoon.setName(map.get("c_name").toString());
				cartoon.setOwner(map.get("c_owner").toString());
				cartoon.setContent(map.get("c_content").toString());
				cartoon.setCreateTime(map.get("c_createTime").toString());
				cartoon.setThumbnail(map.get("c_thumbnail").toString());
				cartoon.setEnable(Integer.parseInt(map.get("c_enable").toString()));
				list.add(cartoon);
			}
		}
		return list;
	}

	@Override
	public int deleteAssetLabelRel(String assetId) {
		String sql = "delete from t_astlab_rel where alr_assets='"+assetId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public int deleteAssetCategoryRel(String assetId) {
		String sql = "delete from t_astcat_rel where acr_assets='"+assetId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public List<Images> getImageBypage(int pageNum, int pageSize) {
		List<Images> list = new ArrayList<Images>();
		int startLine = (pageNum -1)*pageSize;
		String sql = "select * from t_images where  i_enable=1 order by i_uploadTime desc limit "+startLine+","+pageSize;
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Images img = new Images();
				img.setId(map.get("i_id").toString());
				img.setOwner(map.get("i_owner").toString());
				img.setUploadTime(map.get("i_uploadTime").toString());
				img.setPath(map.get("i_path").toString());
				img.setEnable(Integer.parseInt(map.get("i_enable").toString()));
				list.add(img);
			}
		}
		return list;
	}

	@Override
	public List<Cartoon> getAmimByPage(int pageNum, int pageSize) {
		List<Cartoon> list = new ArrayList<Cartoon>();
		int startLine = (pageNum -1)*pageSize;
		String sql = "select * from t_cartoon where  c_enable=1 order by c_createTime desc limit "+startLine+","+pageSize;
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Cartoon cartoon = new Cartoon();
				cartoon.setId(map.get("c_id").toString());
				cartoon.setName(map.get("c_name").toString());
				cartoon.setOwner(map.get("c_owner").toString());
				cartoon.setContent(map.get("c_content").toString());
				cartoon.setCreateTime(map.get("c_createTime").toString());
				cartoon.setThumbnail(map.get("c_thumbnail").toString());
				cartoon.setEnable(Integer.parseInt(map.get("c_enable").toString()));
				list.add(cartoon);
			}
		}
		return list;
	}

	@Override
	public int getAnimCount() {
		String sql = "select count(*) from t_cartoon where c_enable=1";
		int rows = jdbcTemplate.queryForInt(sql);
		return rows;
	}

	@Override
	public int getImageCount() {
		String sql = "select count(*) from t_images where i_enable=1 ";
		int rows = jdbcTemplate.queryForInt(sql);
		return rows;
	}

	@Override
	public List<Assets> getByType( String type, int num, int pageSize) {
		List<Assets> resList = new ArrayList<Assets>();
		int startLine = (num -1)*pageSize;
		String sql ="select distinct a.a_id,a.a_holiday,a.a_name,a.a_type,a.a_thumbnail,a.a_path,a.a_uploadTime,a.a_price,a.a_heat,a.a_enable,c.c_name" +
				" from t_assets a, t_category c, t_astcat_rel acr " +
				"where a.a_id=acr.acr_assets and c.c_id=acr.acr_category and a.a_type='"+type +"' "+
				"and a.a_enable =1 order by a.a_heat desc, a.a_uploadTime desc limit "+startLine+","+pageSize;
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Assets asset = new Assets();
				asset.setId(map.get("a_id").toString());
				asset.setHoliday(map.get("a_holiday").toString());
				asset.setName(map.get("a_name").toString());
				asset.setCategory(map.get("c_name").toString());
				asset.setType(map.get("a_type").toString());
				asset.setThumbnail(map.get("a_thumbnail").toString());
				asset.setPath(map.get("a_path").toString());
				asset.setUploadTime(map.get("a_uploadTime").toString());
				asset.setPrice(Float.parseFloat(map.get("a_price").toString()));
				asset.setHeat(Integer.parseInt(map.get("a_heat").toString()));
				asset.setEnable(Integer.parseInt(map.get("a_enable").toString()));
				resList.add(asset);
			}
		}
		return resList;
	}

	@Override
	public List<Assets> getByCategoryAndType(String categorys, String type,
			int num, int pageSize) {
		List<Assets> resList = new ArrayList<Assets>();
		int startLine = (num -1)*pageSize;
		String sql ="select distinct a.a_id,a.a_holiday,a.a_name,a.a_type,a.a_thumbnail,a.a_path,a.a_uploadTime,a.a_price,a.a_heat,a.a_enable,c.c_name" +
				" from t_assets a, t_category c, t_astcat_rel acr " +
				"where a.a_id=acr.acr_assets and c.c_id=acr.acr_category and a.a_type='"+type +"' "+
				"and c.c_name='"+categorys+"' and a.a_enable =1 order by a.a_heat desc, a.a_uploadTime desc limit "+startLine+","+pageSize;
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Assets asset = new Assets();
				asset.setId(map.get("a_id").toString());
				asset.setHoliday(map.get("a_holiday").toString());
				asset.setName(map.get("a_name").toString());
				asset.setType(map.get("a_type").toString());
				asset.setCategory(map.get("c_name").toString());
				asset.setThumbnail(map.get("a_thumbnail").toString());
				asset.setPath(map.get("a_path").toString());
				asset.setUploadTime(map.get("a_uploadTime").toString());
				asset.setPrice(Float.parseFloat(map.get("a_price").toString()));
				asset.setHeat(Integer.parseInt(map.get("a_heat").toString()));
				asset.setEnable(Integer.parseInt(map.get("a_enable").toString()));
				resList.add(asset);
			}
		}
		return resList;
	}

	@Override
	public List<Label> getAllChildLabel() {
		List<Label> resList = new ArrayList<Label>();
		String sql = "select * from t_label where l_parent != 'parent' order by CONVERT( l_name USING gbk )";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Label lab = new Label();
				lab.setId(map.get("l_id").toString());
				lab.setName(map.get("l_name").toString());
				lab.setHeat(Integer.parseInt(map.get("l_heat").toString()));
				lab.setParent(map.get("l_parent").toString());
				resList.add(lab);
			}
		}
		return resList;
	}

	@Override
	public List<Assets> getAssetByLabel(String labelIds) {
		List<Assets> resList = new ArrayList<Assets>();
		String sql ="select distinct a.a_id from t_assets a, t_label l, t_astlab_rel alr " +
				"where l.l_id in("+labelIds+") and l.l_id = alr.alr_label and a.a_id=alr.alr_assets and a.a_enable =1";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Assets asset = new Assets();
				asset.setId(map.get("a_id").toString());
				resList.add(asset);
			}
		}
		return resList;
	}

	@Override
	public List<Assets> getAsssetsByIdAndPage(String assetIds, int pageNum, int pageSize) {
		List<Assets> resList = new ArrayList<Assets>();
		int startLine = (pageNum -1)*pageSize;
		String sql ="select distinct a.a_id,a.a_holiday,a.a_name,a.a_type,a.a_thumbnail,a.a_path,a.a_uploadTime,a.a_price,a.a_heat,a.a_enable,c.c_name" +
				" from t_assets a, t_category c, t_astcat_rel acr " +
				"where acr.acr_assets in("+assetIds+") and acr.acr_assets=a.a_id and c.c_id=acr.acr_category "+
				"and a.a_enable =1 order by a.a_uploadTime desc limit "+startLine+","+pageSize;
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Assets asset = new Assets();
				asset.setId(map.get("a_id").toString());
				asset.setHoliday(map.get("a_holiday").toString());
				asset.setName(map.get("a_name").toString());
				asset.setType(map.get("a_type").toString());
				asset.setCategory(map.get("c_name").toString());
				asset.setThumbnail(map.get("a_thumbnail").toString());
				asset.setPath(map.get("a_path").toString());
				asset.setUploadTime(map.get("a_uploadTime").toString());
				asset.setPrice(Float.parseFloat(map.get("a_price").toString()));
				asset.setHeat(Integer.parseInt(map.get("a_heat").toString()));
				asset.setEnable(Integer.parseInt(map.get("a_enable").toString()));
				resList.add(asset);
			}
		}
		return resList;
	}

	@Override
	public List<Assets> getAssetByLabelAndType(String labelIds, String type) {
		List<Assets> resList = new ArrayList<Assets>();
		String sql ="select distinct a.a_id from t_assets a, t_label l, t_astlab_rel alr " +
				"where l.l_id in("+labelIds+") and a.a_type = '"+type+"' " +
				"and l.l_id = alr.alr_label and a.a_id=alr.alr_assets and a.a_enable =1";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Assets asset = new Assets();
				asset.setId(map.get("a_id").toString());
				resList.add(asset);
			}
		}
		return resList;
	}

	@Override
	public int checkUserExist(String userId) {
		String sql = "select count(u_id) from t_weibouser where u_id='"+userId+"'";
		int rows = jdbcTemplate.queryForInt(sql);
		return rows;
	}

	@Override
	public int createNewUser(final User user) {
		String sql = "insert into t_weibouser(u_id,u_accessToken,u_createTime,u_wealth,u_memo) values (?,?,?,?,?)";
		int res =jdbcTemplate.update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, user.getId());
					ps.setString(2, user.getAccessToken());
					ps.setString(3, user.getCreateTime());
					ps.setInt(4, user.getWealth());
					ps.setString(5, "");

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});

		return res;
	}

	@Override
	public int updateUserById(String userId, String accessToken) {
		String sql = "update t_weibouser set u_accessToken='"+accessToken+"' where u_id='"+userId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public User getUserById(String userId) {
		User user = new User();
		String sql = "select * from t_weibouser where u_id='"+userId+"'";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				user.setId(map.get("u_id").toString());
				user.setAccessToken(map.get("u_accessToken").toString());
				user.setCreateTime(map.get("u_createTime").toString());
				user.setWealth(Integer.parseInt(map.get("u_wealth").toString()));
			}
		}
		return user;
	}

	@Override
	public int checkAssetExist(String userId, String assetId) {
		String sql = "select count(s_id) from t_shoppingcart where s_asset='"+assetId+"' and s_owner='"+userId+"'";
		int result = jdbcTemplate.queryForInt(sql);
		return result;
	}

	@Override
	public int updateCartAsset(String userId, String assetId) {
		String sql = "update t_shoppingcart set s_count=s_count + 1 where s_asset='"+assetId+"' and s_owner='"+userId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public int addAssetToCart(final Cart cart) {
		String sql = "insert into t_shoppingcart(s_id,s_asset,s_owner,s_count,s_state,s_memo) values (?,?,?,?,?,?)";
		int res =jdbcTemplate.update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, cart.getId());
					ps.setString(2, cart.getAsset());
					ps.setString(3, cart.getOwner());
					ps.setInt(4, cart.getCount());
					ps.setInt(5, cart.getState());
					ps.setString(6, "");

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		return res;
	}

	@Override
	public int deleteAssetFromCart(String userId, String assetId) {
		String sql = "delete from t_shoppingcart where s_state=0 and s_asset='"+assetId+"' and s_owner='"+userId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public int getAssetState(String userId, String assetId) {
		String sql = "select s_state from t_shoppingcart where s_asset='"+assetId+"' and s_owner='"+userId+"'";
		int state = jdbcTemplate.queryForInt(sql);
		return state;
	}

	@Override
	public int changeAssetState(String userId, String assetId) {
		String sql = "update t_shoppingcart set s_state=1 where s_asset='"+assetId+"' and s_owner='"+userId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public List<Cart> getUserCartState(String userId) {
		List<Cart> list = new ArrayList<Cart>();
		String sql = "select s.s_id,s.s_asset,s.s_owner,s.s_count,s.s_state, a.a_name, a.a_price, a.a_thumbnail from t_shoppingcart s, t_assets a " +
				"where s.s_asset = a.a_id and s_owner='"+userId+"' and s_state=0";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Cart cart = new Cart();
				cart.setId(map.get("s_id").toString());
				cart.setAsset(map.get("s_asset").toString());
				cart.setOwner(map.get("s_owner").toString());
				cart.setName(map.get("a_name").toString());
				cart.setPrice(Float.parseFloat(map.get("a_price").toString()));
				cart.setThumbnail(map.get("a_thumbnail").toString());
				cart.setCount(Integer.parseInt(map.get("s_count").toString()));
				cart.setState(Integer.parseInt(map.get("s_state").toString()));
				list.add(cart);
			}
		}
		return list;
	}

	@Override
	public int changeCartState(String userId) {
		String sql = "update t_shoppingcart set s_state=1 where s_owner='"+userId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public int minusUserWealth(String userId, int totalPrice) {
		String sql = "update t_weibouser set u_wealth = u_wealth - "+totalPrice+" where u_id='"+userId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}


}
