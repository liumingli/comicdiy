package com.ybcx.comic.facade;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import weibo4j.http.HttpClient;
import weibo4j.http.Response;
import weibo4j.model.PostParameter;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import com.ybcx.comic.utils.MD5Util;

/**
 * Servlet implementation class CallBackServlet
 */
public class CallBackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CallBackServlet() {
        super();
    }

    private Logger log = Logger.getLogger(CallBackServlet.class);
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/plain;charset=UTF-8");
		log.info("call back page>>>>>>>>>>>>>>>>>>>>");
		
		String order_id = request.getParameter("order_id");
	 	String appkey = request.getParameter("appkey");
	 	String order_uid = request.getParameter("order_uid");
	 	String amount = request.getParameter("amount");
	 	String sign = request.getParameter("sign");
	 	log.info("order_id : "+order_id);
	 	log.info("appkey : "+appkey);
	 	log.info("order_uid : "+order_uid);
	 	log.info("amount : "+amount);
	 	log.info("sign : "+sign);
	 	
	 	int orderStatus = 0;
	 	
	 	if(!"".equals(order_id) && order_id != null && !"".equals(order_uid) && order_uid != null){
	 		//response.sendRedirect("comicapi?method=getOrderStatus&orderId="+order_id+"&userId="+order_uid);
	 		String accessToken = connect(order_uid);
	 		orderStatus = getOrderStatus(order_id,accessToken,appkey);
	 		log.info("orderStatus is "+orderStatus);
	 	}
	 	
	 	if(orderStatus == 1){
	 		//TODO 更新数据库amount
	 		int rows=updateUser(order_uid,amount);
	 		if(rows ==1){
	 			log.info("update user amount success");
	 		}
	 	}
	 	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	private String connect(String userId){
		String accessToken = "";
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url="jdbc:mysql://localhost:3306/comicdiy?user=root&password=root";
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			String sql = "select * from t_weibouser where u_id='"+userId+"'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next() == true) {
				accessToken = rs.getString("u_accessToken");
				log.info("accessToken " + accessToken);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return accessToken;
	}
	

	private int getOrderStatus(String orderId, String accessToken, String appKey) {
		int status = 0;
		String url = "https://pay.api.weibo.com/test/pay/order_status.json";
		String appSecret = "67bb80d9db674165623de5ccf46305d0";
		
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
	
	private int updateUser(String userId, String amount) {
		int result = 0;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url="jdbc:mysql://localhost:3306/comicdiy?user=root&password=root";
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			String sql = "update t_weibouser set u_wealth = u_wealth +"+amount+" where u_id='"+userId+"'";
			result = stmt.executeUpdate(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
