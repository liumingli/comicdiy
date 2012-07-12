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

		if (action.equals(AppStarter.GETALLASSETS)) {
				res.setContentType("text/plain;charset=UTF-8");
				PrintWriter pw = res.getWriter();
				String result= apiAdaptor.getAllAssets();
				log.debug(result);
				pw.print(result);
				pw.close();
				
		}else if (action.equals(AppStarter.SEARCHBYLABEL)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String labels = req.getParameter("keys");
			String result = apiAdaptor.searchByLabel(labels);
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
			String result= apiAdaptor.updateAssetById(assetId,name,price,holiday);
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
			String result= apiAdaptor.createCategory(name);
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
			
			
		}else{
			
		}

	}

	public void setApiAdaptor(ApiAdaptor apiAdaptor) {
		this.apiAdaptor = apiAdaptor;
	}

}
