package com.ybcx.comic.facade;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import weibo4j.Friendships;
import weibo4j.ShortUrl;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.http.HttpClient;
import weibo4j.http.ImageItem;
import weibo4j.http.Response;
import weibo4j.model.Paging;
import weibo4j.model.PostParameter;
import weibo4j.model.Status;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.ybcx.comic.beans.Assets;
import com.ybcx.comic.beans.Cart;
import com.ybcx.comic.beans.Cartoon;
import com.ybcx.comic.beans.Category;
import com.ybcx.comic.beans.Element;
import com.ybcx.comic.beans.Friend;
import com.ybcx.comic.beans.Images;
import com.ybcx.comic.beans.Label;
import com.ybcx.comic.beans.User;
import com.ybcx.comic.beans.UserDetail;
import com.ybcx.comic.beans.Yonkoma;
import com.ybcx.comic.dao.DBAccessInterface;
import com.ybcx.comic.tools.ImageHelper;
import com.ybcx.comic.utils.ComicUtils;
import com.ybcx.comic.utils.IKAnalzyerUtil;
import com.ybcx.comic.utils.MD5Util;

public class ComicServiceImplement implements ComicServiceInterface {
	
	private Logger log = Logger.getLogger(ComicServiceImplement.class);
	// 由Spring注入
	private DBAccessInterface dbVisitor;
	
	private Properties systemConfigurer;

	public void setSystemConfigurer(Properties systemConfigurer) {
		this.systemConfigurer = systemConfigurer;
	}
	
	public void setDbVisitor(DBAccessInterface dbVisitor) {
		this.dbVisitor = dbVisitor;
	}

	//upload
	private String imagePath;
	
	@Override
	public void saveImagePath(String filePath) {
	//	this.imgProcessor.setImagePath(filePath);
		imagePath = filePath;
	}
	
	// 设定输出的类型
	private static final String GIF = "image/gif;charset=UTF-8";

	private static final String JPG = "image/jpeg;charset=UTF-8";

	private static final String PNG = "image/png;charset=UTF-8";
	
	private static final String SWF = "application/x-shockwave-flash;charset=UTF-8";
	
	public List<Assets> getAllAssets() {
		List<Assets> assetsList = dbVisitor.getAllAssets();
		List<Assets> resultList = new ArrayList<Assets>();
		resultList = this.combinLabels(assetsList);
		return resultList;
	}

	@Override
	public int getAllAssetsCount() {
		int res = dbVisitor.getAssetCount();
		return res;
	}
	
	//给素材取标签
	private List<Assets> combinLabels(List<Assets> assetList){
		List<Assets> resultList = new ArrayList<Assets>();
		for(int i=0 ; i<assetList.size() ; i++){
			Assets ast = assetList.get(i);
			String id = ast.getId();
			String labels = this.getLabelsById(id);
			ast.setLabel(labels);
			resultList.add(ast);
		}
		return resultList;
	}
	
	//根据素材id取得所有标签，并用空格连接
	private String getLabelsById(String assetId) {
		StringBuffer labels = new StringBuffer();
		List<Label> labelList = dbVisitor.getAssetLabelsById(assetId);
		if (labelList.size() > 0) {
			for (int i = 0; i < labelList.size(); i++) {
				Label label = labelList.get(i);
				if (labels.length() > 0) {
					labels.append(" ");
				}
				labels.append(label.getName());
			}
		}
		return labels.toString().trim();
	}
	
	@Override
	public List<Assets> getAssetsByPage(int pageNum) {
		int pageSize = Integer.parseInt(systemConfigurer.getProperty("pageSize"));

		List<Assets> assetsList = dbVisitor.getAssetsByPage(pageNum,pageSize);
		
		List<Assets> resultList = new ArrayList<Assets>();
		resultList = this.combinLabels(assetsList);
		
		return resultList;
	}

	
	
	@Override
	public String createAsset(String name, String type, String price,
			String category, String label, String holiday, String assetPath,
			String thumbnailPath) {
		boolean flag = false;
		//新建素材，首先插入asset表，astcat_rel, astlab_rel, 更新category和label表的热度
		String assetId = ComicUtils.generateUID();
		
		Assets asset = this.generateAsset(assetId,name,type,price,holiday,assetPath,thumbnailPath);
		int insAsset = dbVisitor.createAsset(asset);
		
		int insCatRel = dbVisitor.createAstcatRel(ComicUtils.generateUID(),assetId,category);
		int updCatgory = dbVisitor.updateCategoryHeat(category);
		
		//这里前台传来的label是以逗号分隔的id，最多三个
		String[] labelArray =label.split(",");
		
		int insLabRel = this.createLabRel(assetId,labelArray);
		int updLabel = this.updateLabelHeat(labelArray);
		
		if(insAsset>0 && insCatRel>0  && updCatgory>0 && insLabRel>0  && updLabel>0){
			flag = true;
		}
		
		return String.valueOf(flag);
	}
	
	private int updateLabelHeat(String[] labelArray) {
		int rows = 0;
		//在这里将用空格分隔的labels转变成sql可识别的'','
		StringBuffer labelIds = new StringBuffer();
		if(labelArray.length > 0){
			 for (int i = 0; i < labelArray.length; i++) {
					if (!"".equals(labelArray[i].trim())) {
						if (labelIds.length() > 0) {
							labelIds.append(",");
						}
						labelIds.append("'");
						labelIds.append(labelArray[i]);
						labelIds.append("'");
					}
				}
			 
			 rows = dbVisitor.updateLabelHeat(labelIds.toString());
		}
		return rows;
	}

	private int createLabRel(String assetId, String[] labelArray) {
		int rows = 0;
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if(labelArray.length > 0){
			 for (int i = 0; i < labelArray.length; i++) {
				Map<String, String>  labelAsset = new HashMap<String,String>();
				 String labelId = labelArray[i];
				 labelAsset.put(labelId, assetId);
				 list.add(labelAsset);
			 }
			 rows = dbVisitor.createLabRel(list);
		}
		return rows;
	}


	private Assets generateAsset(String assetId, String name, String type, String price,String holiday, String assetPath,
			String thumbnailPath) {
		Assets asset = new Assets();
		asset.setId(assetId);
		asset.setName(name);
		asset.setType(type);
		asset.setPrice(Float.parseFloat(price));
		asset.setHoliday(holiday);
		asset.setPath(assetPath);
		asset.setThumbnail(thumbnailPath);
		asset.setUploadTime(ComicUtils.getFormatNowTime());
		asset.setHeat(0);
		asset.setEnable(1);
		return asset;
	}

	@Override
	public Assets getAssetById(String assetId) {
		Assets asset = dbVisitor.getAssetById(assetId);
		//TODO取label
		StringBuffer labels = new StringBuffer();
		StringBuffer ids = new StringBuffer();
		List<Label> labelList = dbVisitor.getAssetLabelsById(assetId);
		if (labelList.size() > 0) {
			for (int i = 0; i < labelList.size(); i++) {
				Label label = labelList.get(i);
				if (labels.length() > 0) {
					labels.append(",");
				}
				labels.append(label.getName());
				if(ids.length() > 0){
					ids.append(",");
				}
				ids.append(label.getId());
			}
		}
		asset.setLabel(labels.toString().trim());
		asset.setLabelIds(ids.toString().trim());
		return asset;
	}
	
	@Override
	public String deleteAssetById(String assetId) {
		//删除素材时，注意同时要删除与标签和分类的关联
		boolean flag = true;
		int rows = dbVisitor.deleteAssetById(assetId);
		int lab = dbVisitor.deleteAssetLabelRel(assetId);
		int cat = dbVisitor.deleteAssetCategoryRel(assetId);
		
		if(rows < 1 || lab < 1 || cat < 1){
			flag = false;
		}
		return String.valueOf(flag);
	}
	
	@Override
	public String updateAssetById(String assetId, String name, String price, String holiday, String type, String labelIds) {
		boolean flag = false;
		int rows = dbVisitor.updateAssetById(assetId,name,price,holiday,type);
		//删除素材与标签的关联
		int delLabRel = dbVisitor.deleteAssetLabelRel(assetId);
		String[] labelArray = labelIds.split(",");
		int newLabRel = this.createLabRel(assetId,labelArray);
		
		if(rows > 0 && delLabRel >0 && newLabRel>0){
			flag = true;
		}
		return String.valueOf(flag);
	}

	public int searchByLabel(String labels) {
		int result =0;
		 String[] labelArr =labels.split(" ");
		 StringBuffer labelIds = new StringBuffer();
		//先返回所有的标签
		List<Label> childLabel = dbVisitor.getAllChildLabel();
		//再从标签中去匹配关键字，并存下id
		for(int i=0;i<childLabel.size();i++){
			Label label = childLabel.get(i);
			for(int j=0;j<labelArr.length;j++){
				if(!"".equals(labelArr[j]) && label.getName().contains(labelArr[j])){
					if (labelIds.length() > 0) {
						labelIds.append(",");
					}
					labelIds.append("'");
					labelIds.append(label.getId());
					labelIds.append("'");
				}
			}
		}
		
		if(labelIds.length() > 0){
			result = dbVisitor.searchByLabelCount(labelIds.toString());
		}
		return result;
	}
	
	//一句sql全查出来的太耗资源，改成分批查询
//	@Override
//	public List<Assets> searchByLabelPage(String labels,String pageNum) {
//		int pageSize = Integer.parseInt(systemConfigurer.getProperty("pageSize"));
////		List<Assets> resList = new ArrayList<Assets>();
////		List<Assets> andList = new ArrayList<Assets>();
//		List<Assets> orList = new ArrayList<Assets>();
//		 String[] labelArr =labels.split(" ");
////		 StringBuffer labelOr = new StringBuffer();
////		 //在这里将用空格分隔的labels转变成sql可识别的'','
////		 if(labelArr.length > 0){
////			 for (int i = 0; i < labelArr.length; i++) {
////					if (!"".equals(labelArr[i].trim())) {
////						if (labelOr.length() > 0) {
////							labelOr.append(",");
////						}
////						labelOr.append("'");
////						labelOr.append(labelArr[i]);
////						labelOr.append("'");
////					}
////				}
////		 }
////		 
////		 andList = dbVisitor.searchByLabelAnd(labelArr);
//		 orList = dbVisitor.searchByLabelOr(labelArr,pageSize,Integer.parseInt(pageNum));
//		 
////		 resList = combinResult(andList, orList);
//		 
//		List<Assets> resultList = new ArrayList<Assets>();
//		resultList = this.combinLabels(orList);
//		
//		return resultList;
//		 
//	}
	
	
	public List<Assets> searchByLabelPage(String labels,String pageNum) {
		List<Assets> resultList = new ArrayList<Assets>();
		int pageSize = Integer.parseInt(systemConfigurer.getProperty("pageSize"));
		 String[] labelArr =labels.split(" ");
		 StringBuffer labelIds = new StringBuffer();
		//先返回所有的标签
		List<Label> childLabel = dbVisitor.getAllChildLabel();
		//再从标签中去匹配关键字，并存下id
		for(int i=0;i<childLabel.size();i++){
			Label label = childLabel.get(i);
			for(int j=0;j<labelArr.length;j++){
				if(!"".equals(labelArr[j]) && label.getName().contains(labelArr[j])){
					if (labelIds.length() > 0) {
						labelIds.append(",");
					}
					labelIds.append("'");
					labelIds.append(label.getId());
					labelIds.append("'");
				}
			}
		}
		
		if(labelIds.length() > 0){
			//取出糊糊匹配上的素材，然后拼一个assetIds
			List<Assets> assetList = dbVisitor.getAssetByLabel(labelIds.toString());
			
			StringBuffer assetIds = new StringBuffer();
			for(int i=0;i<assetList.size();i++){
				Assets ast = assetList.get(i);
				for(int j=0;j<labelArr.length;j++){
					if(!"".equals(labelArr[j])){
						if (assetIds.length() > 0) {
							assetIds.append(",");
						}
						assetIds.append("'");
						assetIds.append(ast.getId());
						assetIds.append("'");
					}
				}
			}
			
			//这里需要加一个判断如果没有素材就不去搜索返回了
			if(assetIds.length()>0){
				//最后,根据assetIds将分类拼上,并分页返回
				List<Assets> resList = dbVisitor.getAsssetsByIdAndPage(assetIds.toString(),Integer.parseInt(pageNum),pageSize);
				
				//给素材加上标签
				resultList = this.combinLabels(resList);
			}
		}
		return resultList;
	}
	
//	private List<Assets> combinResult(List<Assets> andList, List<Assets> orList){
//		for(int m=0; m<andList.size(); m++){
//			Assets asset = andList.get(m);
//			String id = asset.getId();
//			for(int n=0; n <orList.size(); n++){
//				Assets asse = orList.get(n);
//				if(id.equals(asse.getId())){
//					orList.remove(n);
//				}
//			}
//		}
//		andList.addAll(orList);
//		return andList;
//	}

	@Override
	public int searchByLabelAndType(String labels, String type) {
	   String reslabels = IKAnalzyerUtil.analyzerKeys(labels);
	   int result = 0 ;
	   String[] labelArr =reslabels.split(" ");
		 StringBuffer labelIds = new StringBuffer();
		//先返回所有的标签
		//List<Label> childLabel = dbVisitor.getAllChildLabel();
		//FIXME 这里改为从缓存中取标签
		List<Label> childLabel = AppStarter.labelList;
		//再从标签中去匹配关键字，并存下id
		for(int i=0;i<childLabel.size();i++){
			Label label = childLabel.get(i);
			for(int j=0;j<labelArr.length;j++){
				if(!"".equals(labelArr[j]) && label.getName().contains(labelArr[j])){
					if (labelIds.length() > 0) {
						labelIds.append(",");
					}
					labelIds.append("'");
					labelIds.append(label.getId());
					labelIds.append("'");
				}
			}
		}
		
		if(labelIds.length()>0){
			result = dbVisitor.searchByLabelTypeCount(labelIds.toString(),type);
		}
		return result;
		
	}
	
//	@Override
//	public List<Assets> searchByLabelAndTypePage(String labels, String type, String pageNum) {
//		int pageSize = Integer.parseInt(systemConfigurer.getProperty("pageSize"));
////		List<Assets> resList = new ArrayList<Assets>();
////		List<Assets> andList = new ArrayList<Assets>();
//		List<Assets> orList = new ArrayList<Assets>();
//		 String[] labelArr =labels.split(" ");
////		 StringBuffer labelOr = new StringBuffer();
////		 //在这里将用空格分隔的labels转变成sql可识别的'','
////		 if(labelArr.length > 0){
////			 for (int i = 0; i < labelArr.length; i++) {
////					if (!"".equals(labelArr[i].trim())) {
////						if (labelOr.length() > 0) {
////							labelOr.append(",");
////						}
////						labelOr.append("'");
////						labelOr.append(labelArr[i]);
////						labelOr.append("'");
////					}
////				}
////		 }
//		 
////		 andList = dbVisitor.searchByLabelTypeAnd(labelArr,type);
//		 orList = dbVisitor.searchByLabelTypeOr(labelArr,type,pageSize,Integer.parseInt(pageNum));
//		 
////		 resList = combinResult(andList, orList);
//		 
//		List<Assets> resultList = new ArrayList<Assets>();
//		resultList = this.combinLabels(orList);
//		
//		return resultList;
//	}
	

	public List<Assets> searchByLabelAndTypePage(String labels, String type, String pageNum) {
		 String reslabels = IKAnalzyerUtil.analyzerKeys(labels);
		List<Assets> resultList = new ArrayList<Assets>();
		int pageSize = Integer.parseInt(systemConfigurer.getProperty("pageSize"));
		 String[] labelArr =reslabels.split(" ");
		 StringBuffer labelIds = new StringBuffer();
		//先返回所有的标签
		//List<Label> childLabel = dbVisitor.getAllChildLabel();
		//FIXME 这里改为从缓存中取标签
		List<Label> childLabel = AppStarter.labelList;
		
		//再从标签中去匹配关键字，并存下id
		for(int i=0;i<childLabel.size();i++){
			Label label = childLabel.get(i);
			for(int j=0;j<labelArr.length;j++){
				if(!"".equals(labelArr[j]) && label.getName().contains(labelArr[j])){
					if (labelIds.length() > 0) {
						labelIds.append(",");
					}
					labelIds.append("'");
					labelIds.append(label.getId());
					labelIds.append("'");
				}
			}
		}
		
		if(labelIds.length()>0){
			//取出糊糊匹配上的素材，然后拼一个assetIds
			List<Assets> assetList = dbVisitor.getAssetByLabelAndType(labelIds.toString(),type);
			
			StringBuffer assetIds = new StringBuffer();
			for(int i=0;i<assetList.size();i++){
				Assets ast = assetList.get(i);
				for(int j=0;j<labelArr.length;j++){
					if(!"".equals(labelArr[j])){
						if (assetIds.length() > 0) {
							assetIds.append(",");
						}
						assetIds.append("'");
						assetIds.append(ast.getId());
						assetIds.append("'");
					}
				}
			}
			
			//这里需要加一个判断如果没有素材就不去搜索返回了
			if(assetIds.length()>0){
				//最后,根据assetIds将分类拼上,并分页返回
				List<Assets> resList = dbVisitor.getAsssetsByIdAndPage(assetIds.toString(),Integer.parseInt(pageNum),pageSize);
				
				//给素材加上标签
				resultList = this.combinLabels(resList);
			}
		}
		return resultList;
	}
	
	@Override
	public List<Assets> getByCategoryAndType(String categorys, String type,String pageNum) {
		int num = Integer.parseInt(pageNum);
		int pageSize = Integer.parseInt(systemConfigurer.getProperty("pageSize"));
		List<Assets> resList = new ArrayList<Assets>();
		
		if("all".equals(categorys)){
			
			resList = dbVisitor.getByType(type,num,pageSize);
			
		}else{
			
			resList = dbVisitor.getByCategoryAndType(categorys,type,num,pageSize);
		}
		 
		List<Assets> resultList = new ArrayList<Assets>();
		resultList = this.combinLabels(resList);
		
		return resultList;
	}

	@Override
	public List<Category> getAllCategory() {
		List<Category> cateList = dbVisitor.getAllCategory();
		return cateList;
	}

	@Override
	public String createCategory(String name,String parent) {
		String id = ComicUtils.generateUID();
		int rows = dbVisitor.createCategory(id,name,parent);
		if(rows<1){
			id = "";
		}
		return id;
	}

	@Override
	public void getThumbnailFile(String relativePath, HttpServletResponse res) {
		try {
			//默认
			File defaultImg = new File(imagePath + File.separator + "default.png");
			InputStream defaultIn = new FileInputStream(defaultImg);
			
			String type = relativePath.substring(relativePath.lastIndexOf(".") + 1);
			File file = new File(imagePath+File.separator+relativePath);
			
			if (file.exists()) {
				InputStream imageIn = new FileInputStream(file);
				if (type.toLowerCase().equals("jpg") || type.toLowerCase().equals("jpeg")) {
					writeJPGImage(imageIn, res, file);
				} else if (type.toLowerCase().equals("png")) {
					writePNGImage(imageIn, res, file);
				} else if (type.toLowerCase().equals("gif")) {
					writeGIFImage(imageIn, res, file);
				} else {
					writePNGImage(defaultIn, res, file);
				}
			} else {
				writePNGImage(defaultIn, res, defaultImg);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void writeJPGImage(InputStream imageIn, HttpServletResponse res, File file) {
		try {
//			res.addHeader("content-length",String.valueOf(file.length()));
			res.setContentType(JPG);
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
			// 得到编码后的图片对象
			BufferedImage image = decoder.decodeAsBufferedImage();
			// 得到输出的编码器
			OutputStream out = res.getOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			// 对图片进行输出编码
			imageIn.close();
			// 关闭文件流
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writePNGImage(InputStream imageIn, HttpServletResponse res, File file) {
//		res.addHeader("content-length",String.valueOf(file.length()));
		res.setContentType(PNG);
		getOutInfo(imageIn, res);
	}

	private void writeGIFImage(InputStream imageIn, HttpServletResponse res, File file) {
//		res.addHeader("content-length",String.valueOf(file.length()));
		res.setContentType(GIF);
		getOutInfo(imageIn, res);
	}

	private void getOutInfo(InputStream imageIn, HttpServletResponse res) {
		try {
			OutputStream out = res.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(imageIn);
			// 输入缓冲流
			BufferedOutputStream bos = new BufferedOutputStream(out);
			// 输出缓冲流
			byte data[] = new byte[4096];
			// 缓冲字节数
			int size = 0;
			size = bis.read(data);
			while (size != -1) {
				bos.write(data, 0, size);
				size = bis.read(data);
			}
			bis.close();
			bos.flush();
			// 清空输出缓冲流
			bos.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void writeSWF(InputStream imageIn, HttpServletResponse res, File file) {
//		res.addHeader("content-length",String.valueOf(file.length()));
		res.setContentType(SWF);
		getOutInfo(imageIn, res);
	}

	@Override
	public void getAssetFile(String relativePath, HttpServletResponse res) {
		try {
			//默认
			File defaultImg = new File(imagePath + File.separator + "default.png");
			InputStream defaultIn = new FileInputStream(defaultImg);
			
			String type = relativePath.substring(relativePath.lastIndexOf(".") + 1);
			File file = new File(imagePath+File.separator +relativePath);
			
			if (file.exists()) {
				InputStream imageIn = new FileInputStream(file);
				if(type.toLowerCase().equals("swf")){
					writeSWF(imageIn,res,file);
				}else if (type.toLowerCase().equals("jpg") || type.toLowerCase().equals("jpeg")) {
					writeJPGImage(imageIn, res, file);
				} else if (type.toLowerCase().equals("png")) {
					writePNGImage(imageIn, res, file);
				} else if (type.toLowerCase().equals("gif")) {
					writeGIFImage(imageIn, res, file);
				} else {
					writePNGImage(defaultIn, res, file);
				}
			} else {
				writePNGImage(defaultIn, res, defaultImg);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Label> getAllParentLabel() {
		List<Label> resList = dbVisitor.getAllParentLabel();
		return resList;
	}

	@Override
	public List<Label> getLabelByParent(String parentId) {
		List<Label> resList = dbVisitor.getLabelByParent(parentId);
		return resList;
	}


	@Override
	public String createLabel(String name, String parent) {
		boolean flag = true;
		String id = ComicUtils.generateUID();
		int rows = dbVisitor.createLabel(id,name,parent);
		if(rows<1){
			flag = false;
		}
		return String.valueOf(flag);
	}

	@Override
	public String deleteLabel(String labelId) {
		boolean flag = false;
		int rows = dbVisitor.deleteLabel(labelId);
		if(rows>0){
			flag = true;
		}
		return String.valueOf(flag);
	}

	@Override
	public String deleteLabelByParent(String parentId) {
		boolean flag = true;
		//删除父标签时，将其下子标签也删掉
		List<Label> list = dbVisitor.getLabelByParent(parentId);
		if(list.size()>0){
			int rows = dbVisitor.deleteLabelByParent(parentId);
			if(rows<0){
				flag = false;
			}
		}
		return String.valueOf(flag);
	}

	@Override
	public List<Assets> getSysAssetsBy(String type, int page) {
		int pageSize = Integer.parseInt(systemConfigurer.getProperty("pageSize"));
		List<Assets> assetList = dbVisitor.getAsssetsByType(type,page,pageSize);
		
//		List<Assets> resultList = new ArrayList<Assets>();
//		resultList = this.combinLabels(assetList);
		
		return assetList;
	}

	@Override
	public Cartoon getAnimationBy(String userId, String animId) {
		Cartoon cartoon = new Cartoon();
		if("null".equals(userId)){
			cartoon = dbVisitor.getAnimationById(animId);
		}else{
			cartoon = dbVisitor.getAnimationBy(userId,animId);
		}
		return cartoon;
	}

	@Override
	public List<Cartoon> getAnimationsOf(String userId) {
		List<Cartoon> cartoonList = dbVisitor.getAnimationsOf(userId);
		return cartoonList;
	}

	@Override
	public String createLocalImage(String userId, FileItem imgData) {
		String fileName = imgData.getName();

		String path = imagePath + File.separator + fileName;
		try {
			BufferedInputStream in = new BufferedInputStream(imgData.getInputStream());
			// 获得文件输入流
			BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(new File(path)));// 获得文件输出流
			Streams.copy(in, outStream, true);// 开始把文件写到你指定的上传文件夹
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//插入到数据库
		boolean flag = this.localImageToDB(userId,path);
		
		//上传成功
		if (new File(path).exists() && flag) {
			return path;
		}else{
			return String.valueOf(flag);
		}
	
	}

	private boolean localImageToDB(String userId, String path) {
		boolean flag = true;
		String id = ComicUtils.generateUID();
		String uploadTime = ComicUtils.getFormatNowTime();
		int rows = dbVisitor.createLocalImage(id,userId,path,uploadTime);
		if(rows < 1){
			flag = false;
		}
		return flag;
	}

	@Override
	public String createAnimation(FileItem shotData, String userId, String name,
			String content, String app) {
		boolean flag = true;
		String rawPath = this.saveAnimationRaw(shotData);
		log.info("Animation raw imagePath  is : "+rawPath);
		
		String thumbnail = this.saveThumbnailOf(shotData);
		
		Cartoon cartoon = generateCartoon(userId,name,content,thumbnail,app);
		
		int rows = dbVisitor.saveAnimation(cartoon);
		if(rows < 1){
			flag = false;
			return String.valueOf(flag);
		}else{
			return cartoon.getId();
		}
	}
		
	//这里加了一步保存动画的时候生成一个大图并取名为类似abc_Raw.jpg
	private String saveAnimationRaw(FileItem imgData) {
		String fileName = imgData.getName();
		int position = fileName.lastIndexOf(".");
		String extend = fileName.substring(position);
		String newName = fileName.substring(0,position)+"_Raw"+extend;
		String filePath = imagePath + File.separator + newName;
		File file = new File(filePath);
		try {
			imgData.write(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}

	private Cartoon generateCartoon(String userId, String name, String content,
			String thumbnail, String app){
		Cartoon cartoon = new Cartoon();
		cartoon.setId(ComicUtils.generateUID());
		cartoon.setContent(content);
		cartoon.setOwner(userId);
		cartoon.setName(name);
		cartoon.setThumbnail(thumbnail);
		cartoon.setCreateTime( ComicUtils.getFormatNowTime());
		cartoon.setEnable(1);
		log.info("app----------"+app);
		if(app.equals("watui")){
			cartoon.setApp(app);
		}else{
			cartoon.setApp("produ");
		}
		return cartoon;
	}

	//将diy成品的截屏缩小并保存
	private String saveThumbnailOf(FileItem imgData) {
		String fileName = imgData.getName();
		String filePath = imagePath + File.separator + fileName;
		try {
			ImageHelper.handleImage(imgData, 200, 200, filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	@Override
	public String modifyAnimation(String animId, String content) {
		boolean flag = true;
		int rows = dbVisitor.updateAnimation(animId,content);
		if(rows < 1){
			flag = false;
		}
		return String.valueOf(flag);
	}

	@Override
	public int getAssetCountByType(String type,String category) {
		int rows = 0;
		if("all".equals(category)){
			rows = dbVisitor.getAssetCountByType(type);
		}else{
			rows = dbVisitor.getAssetCountByTypeAndCategory(type,category);
		}
		 
		return rows;
	}

	@Override
	public List<Cartoon> getAllAnimation() {
		List<Cartoon> cartoonList = dbVisitor.getAllAnimation();
		return cartoonList;
	}

	@Override
	public List<Images> getAllImages() {
		List<Images> cartoonList = dbVisitor.getAllImages();
		return cartoonList;
	}

	@Override
	public String examineAnim(String animId) {
		boolean flag = false;
		int rows = dbVisitor.updateAnimById(animId);
		if(rows > 0){
			flag = true;
		}
		return String.valueOf(flag);
	}

	@Override
	public String examineImage(String imgId, String imgPath) {
		boolean flag = false;
		int rows = dbVisitor.updateImageById(imgId);
		if(rows > 0){
			flag = true;
		}
		return String.valueOf(flag);
	}

	@Override
	public int searchAnimation(String keys) {
		int result = dbVisitor.searchAnimation(keys);
		return result;
	}

	@Override
	public List<Images> getImageByPage(String pageNum) {
		int pageSize = Integer.parseInt(systemConfigurer.getProperty("pageSize"));
		List<Images> list = dbVisitor.getImageBypage(Integer.parseInt(pageNum),pageSize);
		return list;
	}

	@Override
	public List<Cartoon> getAmimByPage(String pageNum) {
		int pageSize = Integer.parseInt(systemConfigurer.getProperty("pageSize"));
		List<Cartoon> list = dbVisitor.getAmimByPage(Integer.parseInt(pageNum),pageSize);
		return list;
	}

	@Override
	public int getAnimCount() {
		int rows = dbVisitor.getAnimCount();
		return rows;
	}

	@Override
	public int getImageCount() {
		int rows = dbVisitor.getImageCount();
		return rows;
	}

	@Override
	public List<Cartoon> searchAnimationByPage(String keys, String pageNum) {
		int pageSize = Integer.parseInt(systemConfigurer.getProperty("pageSize")); 
		List<Cartoon> list = dbVisitor.searchAnimationByPage(keys,Integer.parseInt(pageNum),pageSize);
		return list;
	}

	@Override
	public String operateWeiboUser(String userId, String accessToken) {
		boolean flag = false;
		
		weibo4j.model.User weiboUser = this.getUserByIdAndToken(userId, accessToken);
		String nickName = "佚名";
		if(weiboUser != null){
			nickName = weiboUser.getScreenName();
		}else{
			log.warn("weibo user is null");
		}
		//先判断用户是否存在
		int rows = dbVisitor.checkUserExist(userId);
		//存在即更新数据，不存在就插入新记录
		if(rows >0){
			int udpRows = dbVisitor.updateUserById(userId,accessToken,nickName);
			if(udpRows > 0){
				flag = true;
			}
		}else{
			User user = this.generateUser(userId, accessToken, nickName);
			int crtRows = dbVisitor.createNewUser(user);
			if(crtRows > 0){
				flag = true;
			}
		}
		return String.valueOf(flag);
	}

	private User generateUser(String userId, String accessToken, String nickName){
		User user = new User();
		user.setId(userId);
		user.setNickName(nickName);
		user.setCreateTime(ComicUtils.getFormatNowTime());
		user.setAccessToken(accessToken);
		user.setWealth(100);
		return user;
	}

	@Override
	public UserDetail getUserInfo(String userId) {
		//（包括三步，首先从库里取token,wealth 再到去新浪取用户返回昵称和头像等 最后支付账户里去查到钱数累加）
		UserDetail userDetail = new UserDetail();
		User dbUser = new User();
		dbUser = dbVisitor.getUserById(userId);
		String accessToken = dbUser.getAccessToken();
		
		weibo4j.model.User weiboUser = this.getUserByIdAndToken(userId, accessToken);
		
		userDetail.setId(dbUser.getId());
		userDetail.setAccessToken(accessToken);
		if(weiboUser != null){
			userDetail.setAvatarLarge(weiboUser.getAvatarLarge());
			userDetail.setNickName(weiboUser.getScreenName());
			userDetail.setAvatarMini(weiboUser.getProfileImageUrl());
		}else{
			log.warn("weibo user is null");
		}
		//TODO 这个财富后面要加上与支付账户中的累加
		userDetail.setWealth(dbUser.getWealth());

		return userDetail;
	}
	
	//根据token和uid获取用户信息
	private weibo4j.model.User  getUserByIdAndToken(String userId, String accessToken) {
		weibo4j.model.User wbUser = null;
		Users um = new Users();
		um.client.setToken(accessToken);
		try {
			wbUser = um.showUserById(userId);
		} catch (WeiboException e) {
			e.printStackTrace();
			log.info("catch WeiboException : "+ExceptionUtils.getStackTrace(e));
		}
		return wbUser;
	}
	
	@Override
	public String forwardToWeibo(String userId, String animId ,String content) {
		// 这里主要分两步 1、根据userId去找token 2、根据animId取动画，以用于取图片 最后再转发内容到微博
		User user = dbVisitor.getUserById(userId);
		String token = user.getAccessToken();
		
		Cartoon cartoon= dbVisitor.getAnimationById(animId);
		
		String thumbnailPath = cartoon.getThumbnail();
		
		String cname = cartoon.getName();
	
		int position = thumbnailPath.lastIndexOf(".");
		String extend = thumbnailPath.substring(position);
		String imgPath = thumbnailPath.substring(0,position)+"_Raw"+extend;	
		log.info("Cartoon image path : "+imgPath);
		
		//FIXME 分享内容链接请指向站内应用页面
		//String longUrl = "http://diy.produ.cn/comicdiy/animclient/Aplayer_simple.html?userId="+userId+"&animId="+animId;
		String longUrl = "http://apps.weibo.com/wwwproducn?id="+animId;
		boolean flag = uploadToWeibo(token,imgPath,cname,content,longUrl);
		return String.valueOf(flag);
	}
	
	//转发内容到新浪微博
	private boolean uploadToWeibo(String token, String imgPath,String cname, String text, String longUrl) {
		
		try{
			//将长地址生成短地址
			ShortUrl su = new ShortUrl();
			su.client.setToken(token);
//			JSONObject result = su.longToShortUrl(longUrl);
//			weibo4j.org.json.JSONArray url = result.getJSONArray("urls");
//			weibo4j.org.json.JSONObject object =url.getJSONObject(0);
//			String shortUrl = object.getString("url_short");
			
			try{
				byte[] content= readFileImage(imgPath);
				ImageItem pic=new ImageItem("pic",content);
				
				//String resultText =text+"  观看地址："+shortUrl;
				String resultText = text + "  "+cname+" : "+longUrl;

				String s=java.net.URLEncoder.encode(resultText,"utf-8");
				Timeline tl = new Timeline();
				tl.client.setToken(token);
				Status status=tl.UploadStatus(s, pic);

				log.info("Successfully upload the status to ["
						+status.getText()+"].");
			}catch(Exception e1){
				e1.printStackTrace();
				log.info("WeiboException: invalid_access_token.");
				return false;
			}
		}catch(Exception ioe){
			ioe.printStackTrace();
			log.info("Failed to read the system input.");
			return false;
		}
		
		return true;
	}
	
	private static byte[] readFileImage(String filename)throws IOException{
		BufferedInputStream bufferedInputStream=new BufferedInputStream(
				new FileInputStream(filename));
		int len =bufferedInputStream.available();
		byte[] bytes=new byte[len];
		int r=bufferedInputStream.read(bytes);
		if(len !=r){
			bytes=null;
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}


	private List<Friend> generateFriendList(List<weibo4j.model.User> userList,int totalNum) {
		List<Friend> friendList = new ArrayList<Friend>();
		for(int i=0;i<userList.size();i++){
			Friend friend = new Friend();
			weibo4j.model.User usr = userList.get(i);
			friend.setId(usr.getId());
			friend.setNickName(usr.getScreenName());
			friend.setAvatarLarge(usr.getAvatarLarge());
			friend.setAvatarMini(usr.getProfileImageUrl());
			friend.setTotalNumber(totalNum);
			friendList.add(friend);
		}
		return friendList;
	}

	@Override
	public List<Friend> getFriendByPage(String userId, String page) {
		List<Friend> list = new ArrayList<Friend>();
		User user = dbVisitor.getUserById(userId);
		String token = user.getAccessToken();
		try {
			Friendships fship = new Friendships();
			fship.client.setToken(token);
			
			Paging pageing = new Paging(Integer.parseInt(page));
			// FIXME 获取好友列表
			UserWapper friends = fship.getFriendsBilateral(userId,0,pageing);
			//UserWapper friends = fship.getFollowersById(userId);
			
			int totalNum = (int)friends.getTotalNumber();
			List<weibo4j.model.User> userList = friends.getUsers();
			
			if(userList.size()>0){
				list = generateFriendList(userList,totalNum);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			log.info("catch WeiboException : "+ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public String addAssetToCart(String userId, String assetId) {
		boolean flag = false;
		int exist = dbVisitor.checkAssetExist(userId,assetId);
		if(exist > 0){
			int updRows = dbVisitor.updateCartAsset(userId,assetId);
			if(updRows > 0)
				flag = true;
		}else{
			Cart cart = this.generateCartAsset(userId,assetId);
			int newRows = dbVisitor.addAssetToCart(cart);
			if(newRows > 0)
				flag = true;
		}
		return String.valueOf(flag);
	}

	private Cart generateCartAsset(String userId, String assetId) {
		Cart cart = new Cart();
		cart.setId(ComicUtils.generateUID());
		cart.setOwner(userId);
		cart.setAsset(assetId);
		cart.setCount(1);
		cart.setState(0);
		return cart;
	}

	@Override
	public String deleteAssetFromCart(String userId, String assetId) {
		boolean flag = false;
		//删除购物车素材时，判断使用次数是否大于1，大于则减1，否则直接删除
		int count = dbVisitor.checkAssetCount(userId, assetId);
		if(count > 1){
			int udpRows = dbVisitor.updateAssetFromCart(userId,assetId);
			if(udpRows > 0)
				flag = true;
		}else{
			int rows = dbVisitor.deleteAssetFromCart(userId,assetId);
			if(rows > 0)
				flag = true;
		}
		return String.valueOf(flag);
	}

	@Override
	public int getAssetState(String userId, String assetId) {
		int result = dbVisitor.getAssetState(userId,assetId);
		return result;
	}

	@Override
	public String changeAssetState(String userId, String assetId) {
		boolean flag = false;
		int rows = dbVisitor.changeAssetState(userId,assetId);
		if(rows > 0)
			flag = true;
		return String.valueOf(flag);
	}

	@Override
	public List<Cart> getUserCartState(String userId) {
		List<Cart> list = dbVisitor.getUserCartState(userId);
		return list;
	}

	@Override
	public String changeAssetState(String userId,int totalPrice) {
		boolean flag = false;
		int rows = dbVisitor.changeCartState(userId);
		int res = 0;
		if(totalPrice > 0){
		   res = dbVisitor.minusUserWealth(userId,totalPrice);
		   if(rows > 0 && res > 0){
				flag = true;
			}
		}else{
			if(rows > 0){
				flag = true;
			}
		}
		return String.valueOf(flag);
	}

	@Override
	public Map<String, Integer> getStateByAssetIds(String userId, String assetIds) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		String[] idArr =assetIds.split(",");
		for(int i=0;i<idArr.length;i++){
			String assetId = idArr[i];
			int count = dbVisitor.checkAssetExist(userId, assetId);
			//在购物车里的去取付费状态，不在的就默认已付费，其实是免费的
			if(count > 0){
				int state = dbVisitor.getAssetState(userId, assetId);
				map.put(assetId, state);
			}else{
				map.put(assetId, 1);
			}
		}
		return map;
	}

	@Override
	public String getPayToken(String userId, String amount) {
		//FIXME  
		JSONObject jsonObject = new JSONObject();
		String returnUrl = systemConfigurer.getProperty("returnUrl");
		String payToken ="";
		String orderUid ="";
		
		String paymentId = systemConfigurer.getProperty("paymentId");
		String orderId = paymentId + UUID.randomUUID().toString().replace("-", "").substring(0,9);
		String desc = systemConfigurer.getProperty("desc");
		String url = systemConfigurer.getProperty("getTokenUrl");
		String appKey = systemConfigurer.getProperty("appKey");
		String appSecret = systemConfigurer.getProperty("appSecret");
		
		String sign = MD5Util.MD5( orderId+"|"+amount+"|"+desc+"|"+appSecret);
		
		//sign=md5（order_id|amount|desc|app_secret）
		String encodeDesc = "";
		try {
			encodeDesc = URLEncoder.encode(desc,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	
		User user = dbVisitor.getUserById(userId);
		String accessToken = user.getAccessToken();
		try {
			HttpClient client = new HttpClient();
			client.setToken(accessToken);
			PostParameter idparams = new PostParameter("order_id",orderId);
			PostParameter amountparams = new PostParameter("amount",Integer.parseInt(amount));
			PostParameter descparams = new PostParameter("desc",encodeDesc);
			PostParameter signparams = new PostParameter("sign",sign);
			PostParameter sourceparams = new PostParameter("source",appKey);
			PostParameter accessparams = new PostParameter("access_token",accessToken);
			
			PostParameter[] params = new PostParameter[]{idparams,amountparams,descparams,signparams,sourceparams,accessparams};
			Response response = client.post(url, params);
			
			log.info(response.toString());
			
			JSONObject payJson = response.asJSONObject();
			if(payJson.has("token")){
				payToken = payJson.get("token").toString();
			}	
			if(payJson.has("order_uid")){
				orderUid = payJson.get("order_uid").toString();
			}	
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			jsonObject.append("returnUrl", returnUrl);
			jsonObject.append("payToken", payToken);
			jsonObject.append("orderUid", orderUid);
			jsonObject.append("orderId", orderId);
			jsonObject.append("amount", amount);
			jsonObject.append("appKey", appKey);
			jsonObject.append("desc", desc);
			jsonObject.append("version", "1.0");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	@Override
	public int getOrderStatus(String userId,String orderId) {
		int status = 0;
		
		String url = systemConfigurer.getProperty("callbackUrl");
		String appSecret = systemConfigurer.getProperty("appSecret");
		String appKey = systemConfigurer.getProperty("appKey");
		
		User user = dbVisitor.getUserById(userId);
		String accessToken = user.getAccessToken();
		//	sign = md5(order_id|app_secret)
		String sign =  MD5Util.MD5(orderId+"|"+appSecret);
		
		HttpClient client = new HttpClient();
		client.setToken(accessToken);
		
		PostParameter idparams = new PostParameter("order_id",orderId);
		PostParameter signparams = new PostParameter("sign",sign);
		PostParameter sourceparams = new PostParameter("source",appKey);
		PostParameter accessparams = new PostParameter("access_token",accessToken);
		
		PostParameter[] params = new PostParameter[]{idparams,signparams,sourceparams,accessparams};
		try {
			Response response = client.post(url, params);
			JSONObject json = response.asJSONObject();
			if(json.has("order_status")){
				status = Integer.parseInt(json.get("order_status").toString());
			}
			
		} catch (WeiboException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return status;
	}

	@Override
	public String loginSystem(String account, String password) {
		boolean flag = false;
		String user = systemConfigurer.getProperty("account");
		String pwd = systemConfigurer.getProperty("password");
		if(account.equals(user) && password.equals(pwd)){
			flag = true;
		}
		return String.valueOf(flag);
	}

	@Override
	public String createPrimary(String name, String frame, String swf,
			String thumbnail, String longImg, String ad) {
		boolean flag = false;
		String type = "Primary";
		String parent = "parent";
		String author = "admin";
		Yonkoma yonkoma = this.generateYonkoma(name, frame, swf, thumbnail, longImg, type, parent,ad,author);
		int rows = dbVisitor.createYonkoma(yonkoma);
		if(rows > 0){
			return yonkoma.getId();
		}else{
			return String.valueOf(flag);
		}	
	}
	
	private Yonkoma generateYonkoma(String name, String frame, String swf,String thumbnail,String longImg, String type,String parent,String ad, String userId){
		Yonkoma yonkoma = new Yonkoma();
		yonkoma.setId(ComicUtils.generateUID());
		yonkoma.setName(name);
		yonkoma.setSwf(swf);
		yonkoma.setThumbnail(thumbnail);
		yonkoma.setLongImg(longImg);
		yonkoma.setCreateTime(ComicUtils.getFormatNowTime());
		yonkoma.setFrame(Integer.parseInt(frame));
		yonkoma.setType(type);
		yonkoma.setAuthor(userId);
		yonkoma.setParent(parent);
		yonkoma.setEnable(1);
		yonkoma.setAd(Integer.parseInt(ad));
		return yonkoma;
	}

	@Override
	public String createEnding(String name, String swf, String thumbnail, String longImg, String parent, String ad) {
		boolean flag = false;
		String type = "Ending";
		String frame = "0";
		String author = "admin";
		Yonkoma yonkoma = this.generateYonkoma(name, frame, swf, thumbnail, longImg, type, parent,ad,author);
		int rows = dbVisitor.createYonkoma(yonkoma);
		if(rows > 0){
			return yonkoma.getId();
		}else{
			return String.valueOf(flag);
		}	
	}

	@Override
	public int getYonkomaCount(String primary) {
		int rows = dbVisitor.getYonkomaCount(primary);
		return rows;
	}
	
	@Override
	public List<Yonkoma> getYonkomaByPage(String primary, String pageSize,
			String pageNum) {
		int size = Integer.parseInt(pageSize);
		int num = Integer.parseInt(pageNum);
		List<Yonkoma> list  = dbVisitor.getYonkomaByPage(primary,size,num);
		return list;
	}

	@Override
	public String updatePrimary(String id, String name, String frame) {
		boolean flag = false;
		int rows = dbVisitor.updatePrimary(id,name,frame);
		if(rows>0){
			flag = true;
		}
		return String.valueOf(flag);
	}

	@Override
	public String deletePrimary(String primaryId) {
		boolean flag = false;
		int rows = dbVisitor.deleteYonkoma(primaryId);
		//删除某一主动画的所有结局
		dbVisitor.deleteEndingByPrimary(primaryId);
		if(rows>0){
			flag = true;
		}
		return String.valueOf(flag);
	}

	@Override
	public String deleteEnding(String endingId) {
		boolean flag = false;
		int rows = dbVisitor.deleteYonkoma(endingId);
		if(rows>0){
			flag = true;
		}
		return String.valueOf(flag);
	}

	@Override
	public String checkYonkomaName(String name) {
		boolean flag = false;
		int rows = dbVisitor.checkYonkomaName(name);
		if(rows<1){
			flag = true;
		}
		return String.valueOf(flag);
	}

	@Override
	public String createCustomEnding(FileItem shotData, String parent,
			String name,String userId) {
		boolean flag = false;
		String longImg = this.saveLongImage(shotData);
		String thumbnail = this.saveThumbnailOf(shotData);
		String frame="0";
		String type = "Ending";
		String ad = "0";
		String swf = longImg;
		Yonkoma yonkoma = this.generateYonkoma(name, frame, swf, thumbnail, longImg, type, parent,ad,userId);
		int rows = dbVisitor.createYonkoma(yonkoma);
		if(rows > 0){
			return yonkoma.getId();
		}else{
			return String.valueOf(flag);
		}	
	}
	
	//保存自定义结局上传的原图片 width = 400
	private String saveLongImage(FileItem imgData) {
		String fileName = imgData.getName();
		int position = fileName.lastIndexOf(".");
		String extend = fileName.substring(position);
		String newName = fileName.substring(0,position)+"_custom"+extend;
		String filePath = imagePath + File.separator + newName;
		File file = new File(filePath);
		try {
			imgData.write(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}


	@Override
	public String yonkomaToWeibo(String userId,String type, String primaryId,
			String endingId, String weibo, String animId) {
		String result = "";
		String url = "http://diy.produ.cn/watui/watuiapi?method=yonkomaToWeibo";
		
		HttpClient client = new HttpClient();
		//这个没有用
		client.setToken("123");
		
		PostParameter idparams = new PostParameter("userId",userId);
		PostParameter typeparams = new PostParameter("type",type);
		PostParameter primaryparams = new PostParameter("primaryId",primaryId);
		PostParameter endingparams = new PostParameter("endingId",endingId);
		PostParameter contentparams = new PostParameter("content",weibo);
		PostParameter animparams = new PostParameter("animId",animId);
		
		PostParameter[] params = new PostParameter[]{idparams,typeparams,primaryparams,endingparams,contentparams,animparams};
		try {
			Response response = client.post(url, params);
			result = response.getResponseAsString();
			
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String createElement(String name, String swf, String thumbnail,
			String classify) {
		// TODO Auto-generated method stub
		boolean flag = false;
		Element ele = this.generateElement(name, swf, thumbnail, classify);
		int rows = dbVisitor.createElement(ele);
		if(rows > 0){
			flag = true;
		}
		return String.valueOf(flag);
	}
	
	private Element generateElement(String name, String swf, String thumbnail,String classify){
		Element ele = new Element();
		ele.setId(ComicUtils.generateUID());
		ele.setName(name);
		ele.setSwf(swf);
		ele.setThumbnail(thumbnail);
		ele.setClassify(classify);
		ele.setEnable(1);
		ele.setCreateTime(ComicUtils.getFormatNowTime());
		return ele;
	}

	@Override
	public String checkEleName(String name) {
		boolean flag = false;
		int count = dbVisitor.checkEleName(name);
		if(count < 1){
			flag = true;
		}
		return String.valueOf(flag);
	}
}
