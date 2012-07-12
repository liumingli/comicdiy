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
import com.ybcx.comic.beans.Cartoon;
import com.ybcx.comic.beans.Category;
import com.ybcx.comic.beans.Label;
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
	public int createLabRell(final List<Map<String, String>> list) {
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
//				asset.setLabel(map.get("a_label").toString());
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
	public int updateAssetById(String assetId, String name, String price, String holiday) {
		Float priceVal = Float.parseFloat(price);
		String sql = "update t_assets set a_name='"+name+"', a_price="+priceVal+", a_holiday ='"+ holiday+"' where a_id='"+assetId+"'";
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
			labelAnd.append("a.a_id in (select r.alr_assets from t_astlab_rel r, t_label l where r.alr_label= l.l_id and l.l_name ='"+labelArr[i].trim()+"')");
		}
		
		String sql ="select distinct a.a_id,a.a_holiday,a.a_name,a.a_type,a.a_thumbnail,a.a_path,a.a_uploadTime,a.a_price,a.a_heat,a.a_enable,c.c_name" +
				" from t_assets a, t_label l, t_astlab_rel alr, t_category c, t_astcat_rel acr " +
				"where a.a_id=acr.acr_assets and c.c_id=acr.acr_category and a.a_id = alr.alr_assets and l.l_id = alr.alr_label " +
				"and "+ labelAnd.toString() +" and a.a_enable =1 order by a.a_heat desc";
		
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
	public List<Assets> searchByLabelOr(String labels) {
		List<Assets> resList = new ArrayList<Assets>();
		String sql ="select distinct a.a_id,a.a_holiday,a.a_name,a.a_type,a.a_thumbnail,a.a_path,a.a_uploadTime,a.a_price,a.a_heat,a.a_enable,c.c_name" +
				" from t_assets a, t_label l, t_astlab_rel r, t_category c, t_astcat_rel acr" +
				" where a.a_id=acr.acr_assets and c.c_id=acr.acr_category and a.a_id = r.alr_assets and l.l_id = r.alr_label " +
				"and l.l_name in("+ labels+") and a.a_enable =1 order by a.a_heat desc";
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
		String sql = "select * from t_category order by c_name,c_heat desc";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) rows.get(i);
				Category category = new Category();
				category.setId(map.get("c_id").toString());
				category.setName(map.get("c_name").toString());
				category.setHeat(Integer.parseInt(map.get("c_heat").toString()));
				resList.add(category);
			}
		}
		return resList;
	}

	@Override
	public int createCategory(String id, String name) {
		String sql = "insert into t_category values('"+id+"', '"+name+"', 0, '')";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public List<Label> getAllParentLabel() {
		List<Label> resList = new ArrayList<Label>();
		String sql = "select * from t_label where l_parent='parent' order by l_name";
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
		String sql = "select * from t_label where l_parent='"+parentId+"'";
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
		String sql = "select * from t_assets where a_type='"+type+"' order by a_heat desc limit "+startLine+","+pageSize;
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
	public Cartoon getAnimationBy(String userId, String animId) {
		Cartoon cartoon = new Cartoon();
		String sql = "select * from t_cartoon where c_id='"+animId+"' and c_owner='"+userId+"'";
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
			}
		}
		return cartoon;
	}

	@Override
	public List<Cartoon> getAnimationsOf(String userId) {
		List<Cartoon> list = new ArrayList<Cartoon>();
		String sql = "select * from t_cartoon where c_owner='"+userId+"'";
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
				list.add(cartoon);
			}
		}
		return list;
	}

	@Override
	public int saveAnimation(final Cartoon cartoon) {
		String sql = "INSERT INTO t_cartoon "
				+ "(c_id, c_name, c_thumbnail, c_content, c_owner, c_createTime, c_memo) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		int res =jdbcTemplate.update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) {
				try {
					ps.setString(1, cartoon.getId());
					ps.setString(2, cartoon.getName());
					ps.setString(3, cartoon.getThumbnail());
					ps.setString(4, cartoon.getContent());
					ps.setString(5, cartoon.getOwner());
					ps.setString(6, cartoon.getCreateTime());
					ps.setString(7,"");

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
	public int createLocalImage(String id, String userId, String path,
			String uploadTime) {
		String sql = "insert into t_images (i_id,i_path,i_uploadTime,i_owner,i_memo) values('"+id+"', '"+path+"', '"+uploadTime+"', '"+userId+"', '')";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

	@Override
	public int updateAnimation(String animId, String content) {
		String sql = "update t_cartoon set c_content ='"+content+"' where c_id='"+animId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows;
	}

}
