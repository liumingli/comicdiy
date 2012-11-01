package com.ybcx.comic.facade;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

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
	 	response.getWriter().write("order_id is "+order_id);
	 	
	 	if(!"".equals(order_id) && order_id != null){
	 		response.setStatus(200);
	 		response.getWriter().write("OK");
	 		return;
	 	}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
