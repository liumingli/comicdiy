package com.ybcx.comic.facade;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
			
		}else if (action.equals(AppStarter.GETWEBANIM)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String pageNum = req.getParameter("pageNum");
			String callBack = req.getParameter("callback");
			String result = callBack+"("+apiAdaptor.getAnimByPage(pageNum)+")";
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
			
		}else if (action.equals(AppStarter.GETPAYTOKEN)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String userId = req.getParameter("userId");
			String amount = req.getParameter("amount");
		    String result= apiAdaptor.getPayToken(userId,amount);
			log.debug(result);
			pw.print(result);
			pw.close();
		
		}else if (action.equals(AppStarter.GETORDERSTATUS)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String userId = req.getParameter("userId");
			String orderId = req.getParameter("orderId");
		    int result= apiAdaptor.getOrderStatus(userId,orderId);
			log.info(result);
			pw.print(result);
			pw.close();
			
	   }else if (action.equals(AppStarter.LOGINSYSTEM)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String account = req.getParameter("account");
			String password = req.getParameter("password");
		    String result= apiAdaptor.loginSystem(account,password);
		    if(result.equals("true")){
		    	HttpSession session = req.getSession(false);
				session.setAttribute("user", account);
				session.setMaxInactiveInterval(2*60*60);
		    }
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.CREATEPRIMARY)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String name = req.getParameter("name");
			String frame = req.getParameter("frame");
			String swf = req.getParameter("swf");
			String thumbnail = req.getParameter("thumbnail");
			String longImg = req.getParameter("longImg");
			String ad = req.getParameter("ad");
		    String result= apiAdaptor.createPrimary(name,frame,swf,thumbnail,longImg,ad);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.CREATEENDING)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String name = req.getParameter("name");
			String swf = req.getParameter("swf");
			String thumbnail = req.getParameter("thumbnail");
			String parent = req.getParameter("parent");
			String longImg = req.getParameter("longImg");
			String ad = req.getParameter("ad");
		    String result= apiAdaptor.createEnding(name,swf,thumbnail,longImg,parent,ad);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETPRIMARYCOUNT)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
		    int result= apiAdaptor.getYonkomaCount("parent");
			log.info(result);
			pw.print(result);
			pw.close();
			
			
		}else if (action.equals(AppStarter.GETPRIMARYBYPAGE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String pageSize = req.getParameter("pageSize");
			String pageNum = req.getParameter("pageNum");
		    String result= apiAdaptor.getYonkomaByPage("parent",pageSize,pageNum);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETENDINGCOUNTBYPRIMARY)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String primary = req.getParameter("primary");
		    int result= apiAdaptor.getYonkomaCount(primary);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETENDINGBYPRIMARYANDPAGE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			//主动画id--primary
			String primary = req.getParameter("primary");
			String pageSize = req.getParameter("pageSize");
			String pageNum = req.getParameter("pageNum");
		    String result= apiAdaptor.getYonkomaByPage(primary,pageSize,pageNum);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.UPDATEPRIMARY)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String id = req.getParameter("id");
			String name = req.getParameter("name");
			String frame = req.getParameter("frame");
		    String result= apiAdaptor.updatePrimary(id,name,frame);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.DELPRIMARY)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String primaryId = req.getParameter("primaryId");
		    String result= apiAdaptor.delPrimary(primaryId);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.DELENDING)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String endingId = req.getParameter("endingId");
		    String result= apiAdaptor.delEnding(endingId);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.CHECKYONKOMANAME)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String name = req.getParameter("name");
		    String result= apiAdaptor.checkYonkomaName(name);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.CREATMOVIECLIP)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String name = req.getParameter("name");
			String swf = req.getParameter("swf");
			String thumbnail = req.getParameter("thumbnail");
			String type = req.getParameter("type");
		    String result= apiAdaptor.createMovieClip(name,swf,thumbnail,type);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.CHECKCLIPNAME)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String name = req.getParameter("name");
		    String result= apiAdaptor.checkClipName(name);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETMOVIECLIPCOUNT)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
		    int result= apiAdaptor.getMovieClipCount();
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETMOVIECLIPBYPAGE)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String pageNum = req.getParameter("pageNum");
		    String result= apiAdaptor.getMovieClipByPage(pageNum);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.DELMOVIECLIP)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String id = req.getParameter("id");
		    String result= apiAdaptor.delMovieClip(id);
			log.info(result);
			pw.print(result);
			pw.close();
			
		}else if (action.equals(AppStarter.GETMOVIECLIP)) {
			res.setContentType("text/plain;charset=UTF-8");
			PrintWriter pw = res.getWriter();
			String pageNum = req.getParameter("pageNum");
			String pageSize = req.getParameter("pageSize");
			String type = req.getParameter("type");
		    String result= apiAdaptor.getMovieClip(pageNum,pageSize,type);
			log.info(result);
			pw.print(result);
			pw.close();
		}
		
	}

	public void setApiAdaptor(ApiAdaptor apiAdaptor) {
		this.apiAdaptor = apiAdaptor;
	}

}
