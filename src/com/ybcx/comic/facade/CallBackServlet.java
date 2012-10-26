package com.ybcx.comic.facade;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CallBackServlet
 */
public class CallBackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CallBackServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String order_id = request.getParameter("order_id");
	 	String appkey = request.getParameter("appkey");
	 	String order_uid = request.getParameter("order_uid");
	 	String amount = request.getParameter("amount");
	 	String sign = request.getParameter("sign");
	 	System.out.println("order_id : "+order_id);
	 	System.out.println("appkey : "+appkey);
	 	System.out.println("order_uid : "+order_uid);
	 	System.out.println("amount : "+amount);
	 	System.out.println("sign : "+sign);
	 	
	 	if(!"".equals(order_id) && order_id != null){
	 		response.setStatus(200);
	 		return;
	 	}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
