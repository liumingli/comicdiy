package com.ybcx.comic.facade;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class AssistProcess {
	
	// 由Spring注入
	private ApiAdaptor apiAdaptor;


	private Logger log = Logger.getLogger(AssistProcess.class);
	
	/**
	 * 处理正常用户登录post的请求
	 * 
	 * @param action
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPostProcess(String action, HttpServletRequest req,
			HttpServletResponse res) throws ServletException, IOException {

		// 安全验证：如果不是上传文件请求，取用户id参数，判断是否为正常用户
		if(!GlobalController.isDebug){
		}

		if (action.equals(AppStarter.GETALLASSETSCOUNT)) {
				res.setContentType("text/plain;charset=UTF-8");
				PrintWriter pw = res.getWriter();
				int result= apiAdaptor.getAllAssetsCount();
				log.debug(result);
				pw.print(result);
				pw.close();
				
		}else if (action.equals(AppStarter.GETASSETSBYPAGE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String pageNum = req.getParameter("pageNum");
			String result = apiAdaptor.getAssetsByPage(pageNum);
			log.debug(result);
			pw.print(result);
			pw.close();
				
		}else if (action.equals(AppStarter.GETSEARCHCOUNTBYLABEL)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String labels = req.getParameter("keys");
			int result = apiAdaptor.searchByLabel(labels);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.SEARCHBYLABELPAGE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String labels = req.getParameter("keys");
			String pageNum = req.getParameter("pageNum");
			String result = apiAdaptor.searchByLabelPage(labels,pageNum);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETSEARCHCOUNTBYLABELANDTYPE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			//根据标签和类型查素材
			String labels = req.getParameter("keys");
			String type = req.getParameter("type");
			int result = apiAdaptor.searchByLabelAndType(labels,type);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.SEARCHBYLABELANDTYPEPAGE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			//根据标签和类型查素材
			String labels = req.getParameter("keys");
			String type = req.getParameter("type");
			String pageNum = req.getParameter("pageNum");
			String result = apiAdaptor.searchByLabelAndTypePage(labels,type,pageNum);
			log.debug(result);
			pw.print(result);
			pw.close();
			
			
		}else if (action.equals(AppStarter.GETBYCATEGORYANDTYPE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			//根据分类和类型分页查素材
			String categorys = req.getParameter("categorys");
			String type = req.getParameter("type");
			String pageNum = req.getParameter("pageNum");
			String result = apiAdaptor.getByCategoryAndType(categorys,type,pageNum);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.CREATEASSET)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String name = req.getParameter("name");
			String type = req.getParameter("type");
			String price = req.getParameter("price");
			String category = req.getParameter("category");
			String label = req.getParameter("label");
			String holiday = req.getParameter("holiday");
			String assetPath = req.getParameter("assetPath");
			String thumbnailPath = req.getParameter("thumbnailPath");
			String result = apiAdaptor.createAsset(name,type,price,category,label,holiday,assetPath,thumbnailPath);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETASSETBYID)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String assetId = req.getParameter("assetId");
			String result= apiAdaptor.getAssetById(assetId);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.DELETEASSETBYID)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String assetId = req.getParameter("assetId");
			String result= apiAdaptor.deleteAssetById(assetId);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.UPDATEASSETBYID)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String assetId = req.getParameter("assetId");
			String name = req.getParameter("name");
			String price = req.getParameter("price");
			String holiday = req.getParameter("holiday");
			String type = req.getParameter("type");
			String labelIds = req.getParameter("labelIds");
			String result= apiAdaptor.updateAssetById(assetId,name,price,holiday,type,labelIds);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETALLCATEGORY)) {
				res.setContentType("text/plain;charset=UTF-8");
				PrintWriter pw = res.getWriter();
				String result= apiAdaptor.getAllCategory();
				log.debug(result);
				pw.print(result);
				pw.close();
			
		}else if (action.equals(AppStarter.CREATECATEGORY)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String name = req.getParameter("name");
			String parent = req.getParameter("parent");
			String result= apiAdaptor.createCategory(name,parent);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.CREATELABEL)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String name = req.getParameter("name");
			String parent = req.getParameter("parent");
			String result= apiAdaptor.createLabel(name,parent);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETALLPARENTLABEL)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String result= apiAdaptor.getAllParentLabel();
			log.debug(result);
			pw.print(result);
			pw.close();
			
			
		}else if (action.equals(AppStarter.GETLABELBYPARENT)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String parentId = req.getParameter("parentId");
			String result= apiAdaptor.getLabelByParent(parentId);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.DELETELABEL)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String labelId = req.getParameter("labelId");
			String result= apiAdaptor.deleteLabel(labelId);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.DELETELABELBYPARENT)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String parentId = req.getParameter("parentId");
			String result= apiAdaptor.deleteLabelByParent(parentId);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETSYSASSETSBY)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String type = req.getParameter("type");
			int page = Integer.parseInt(req.getParameter("page"));
			if(page<=0){
				page = 1;
			}
			String result= apiAdaptor.getSysAssetsBy(type,page);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETASSETCOUNTBY)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String type = req.getParameter("type");
			String category = req.getParameter("category");
			int result= apiAdaptor.getAssetCountByType(type,category);
			log.debug(result);
			pw.print(result);
			pw.close();	
			
		}else if (action.equals(AppStarter.GETANIMATIONBY)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String userId = req.getParameter("userId");
			String animId = req.getParameter("animId");
			String result= apiAdaptor.getAnimationBy(userId,animId);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETANIMATIONSOF)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String userId = req.getParameter("userId");
			String result= apiAdaptor.getAnimationsOf(userId);
			log.debug(result);
			pw.print(result);
			pw.close();
		
		}else if (action.equals(AppStarter.MODIFYANIM)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String animId = req.getParameter("animId");
			String content = req.getParameter("content");
			String result= apiAdaptor.modifyAnim(animId,content);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETALLANIM)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String result= apiAdaptor.getAllAnim();
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETANIMBYPAGE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String pageNum = req.getParameter("pageNum");
			String result = apiAdaptor.getAnimByPage(pageNum);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETANIMCOUNT)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			int result= apiAdaptor.getAnimCount();
			log.debug(result);
			pw.print(result);
			pw.close();	
			
		}else if (action.equals(AppStarter.GETALLIMAGE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String result= apiAdaptor.getAllImage();
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETIMAGEBYPAGE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String pageNum = req.getParameter("pageNum");
			String result = apiAdaptor.getImageByPage(pageNum);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETIMAGECOUNT)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			int result= apiAdaptor.getImageCount();
			log.debug(result);
			pw.print(result);
			pw.close();	
			
		}else if (action.equals(AppStarter.EXAMINEANIM)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String animId = req.getParameter("animId");
			String result= apiAdaptor.examineAnim(animId);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.EXAMINEIMAGE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String imgId = req.getParameter("imgId");
			String imgPath = req.getParameter("imgPath");
			String result= apiAdaptor.examineImage(imgId,imgPath);
			log.debug(result);
			pw.print(result);
			pw.close();
		
		}else if (action.equals(AppStarter.SEARCHANIM)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String keys = req.getParameter("keys");
			int result= apiAdaptor.searchAnim(keys);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.SEARCHANIMBYPAGE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String keys = req.getParameter("keys");
			String pageNum = req.getParameter("pageNum");
			String result= apiAdaptor.searchAnimByPage(keys,pageNum);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		 //添加商品到购物车
		}else if (action.equals(AppStarter.ADDASSETTOCART)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String userId = req.getParameter("userId");
			String assetId = req.getParameter("assetId");
			String result= apiAdaptor.addAssetToCart(userId,assetId);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.DELETEASSETFROMCART)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String userId = req.getParameter("userId");
			String assetId = req.getParameter("assetId");
			String result= apiAdaptor.deleteAssetFromCart(userId,assetId);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETASSETSTATE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String userId = req.getParameter("userId");
			String assetId = req.getParameter("assetId");
		    int result= apiAdaptor.getAssetState(userId,assetId);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETSTATEBYASSETIDS)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String userId = req.getParameter("userId");
			String assetIds = req.getParameter("assetIds");
		    String result= apiAdaptor.getStateByAssetIds(userId,assetIds);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.CHANGEASSETSTATE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String userId = req.getParameter("userId");
			String assetId = req.getParameter("assetId");
		    String result= apiAdaptor.changeAssetState(userId,assetId);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETUSERCARTSTATE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String userId = req.getParameter("userId");
		    String result= apiAdaptor.getUserCartState(userId);
			log.debug(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.CHANGEUSERCARTSTATE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String userId = req.getParameter("userId");
			String totalPrice = req.getParameter("totalPrice");
		    String result= apiAdaptor.changeUserCartState(userId,totalPrice);
			log.debug(result);
			pw.print(result);
			pw.close();
		
		}else{
			
		}

	}

	public void setApiAdaptor(ApiAdaptor apiAdaptor) {
		this.apiAdaptor = apiAdaptor;
	}

}
