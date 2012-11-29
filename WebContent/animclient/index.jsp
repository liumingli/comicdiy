<%@page import="javax.swing.text.Document"%>
<%@ page language="java" import="java.util.*,weibo4j.Oauth"
	contentType="text/html; charset=UTF-8"%>
<%
	response.setHeader("P3P", "CP=CAO PSA OUR");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>自己动手做动画</title>
<meta name="google" value="notranslate" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<style type="text/css" media="screen">
html,body {
	height: 100%;
}

body {
	margin: 0;
	padding: 0;
	overflow: auto;
	text-align: center;
	background: url("assets/icon_web/wood40.png") repeat scroll 0 0 transparent;
}

object:focus {
	outline: none;
}

#purchasePage{	
	padding-top: 60px;
	height:435px;
	width:760px;
	background: url("assets/icon_web/recharge_center.png") no-repeat ;
	background-position: center;
}

img{
	cursor:pointer;
}

#backImg{
	padding-top:30px;
	padding-left:370px;
}

#btnImg1{
	padding-top:42px;
	padding-left:280px;
}
#btnImg2{
	padding-top:32px;
	padding-left:280px;
}
#btnImg5{
	padding-top:32px;
	padding-left:280px;
}
#btnImg10{
	padding-top:32px;
	padding-left:280px;
}

</style>

<script
	src="http://tjs.sjs.sinajs.cn/t35/apps/opent/js/frames/client.js"
	language="JavaScript"></script>
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript" src="swfobject.js"></script>
<script type="text/javascript">
     
  	//加载页面的时候先看token
 	 window.onload = function(){
  		 <%String signed = request.getParameter("signed_request");
			String access_token = "";
			String user_id = "";
			if (signed != null) {
				Oauth oauth = new Oauth();
				access_token = oauth.parseSignedRequest(signed);
				user_id = oauth.user_id;
			}%>
  	     var userId = "<%=user_id%>";
  	     var accessToken = "<%=access_token%>";
		if (accessToken == null || accessToken == "") {
			//弹出授权层
			authLoad();
		} else {
			//看用户是否存在,存库或更新库
			operateUser(userId, accessToken);
			//动态创建应用
			createSWFById('flashContent', userId, "760", "602");
		}
	};
	
	//弹出授权层
	function authLoad() {
		App.AuthDialog.show({
			client_id : '2264908245', //必选，appkey
			redirect_uri : 'http://apps.weibo.com/wwwproducn', //必选，授权后的回调地址
			height : 60
		//可选，默认距顶端120px
		});
	}

	//存或更新用户
	function operateUser(userId, accessToken) {
		$.post("/comicdiy/comicapi", {
			'method' : 'operateWeiboUser',
			'userId' : userId,
			'accessToken' : accessToken
		}, function(result) {
			//返回ture和false
		});
	}

	//动态创建
	function createSWFById(divId, userId, w, h) {
		//获取URL参数
		var flashvars = {};
		flashvars.userId = userId;
		var swfVersionStr = "11.1.0";
		var xiSwfUrlStr = "playerProductInstall.swf";
		var params = {};
		params.quality = "high";
		params.bgcolor = "#ffffff";
		params.allowscriptaccess = "always";
		params.allowfullscreen = "true";
		params.wmode = "opaque";
		var attributes = {};
		attributes.id = divId;
		attributes.name = divId;
		attributes.align = "middle";
		swfobject.embedSWF("Main.swf?v=1.01&date=1029", divId, w, h, swfVersionStr, xiSwfUrlStr,
				flashvars, params, attributes);
		swfobject.createCSS("#" + divId, "display:block;text-align:left;");
	}

	//生成
	function generatePurchasePage(userId) {
		$('#flashContent').attr("style", "display:none");
		$('#purchasePage').attr("style", "display:block");
		$('#userId').attr("value", userId);
	}
	
	//返回(不想充值了，直接退回去)
	function backToApplication(){
		$('#flashContent').attr("style", "display:block");
		$('#purchasePage').attr("style", "display:none");
	}
	
	function generateLoading(count){
		$('#load'+count).attr("style","visibility:visible");
	}
	
	//付款
	function getPayToken(count) {
		generateLoading(count);
		var userId = $('#userId').val();
		var amount = count*100;
		$.post("/comicdiy/comicapi", {
			'method' : 'getPayToken',
			'userId' : userId,
			'amount' : amount
			
		}, function(result) {
			if (result!="[]") {
				$('#return_url').attr("value", result.returnUrl);
				$('#order_id').attr("value", result.orderId);
				$('#order_uid').attr("value", result.orderUid);
				$('#desc').attr("value", result.desc);
				$('#appkey').attr("value", result.appKey);
				$('#amount').attr("value", result.amount);
				$('#token').attr("value", result.payToken);
				$('#version').attr("value", result.version);
			}
			//提交form
			$('#fmPay').submit();
		
		}, "json");
	}
</script>

</head>

<body>

	<div id="flashContent" style="display: none">
		<p>To view this page ensure that Adobe Flash Player version need
			flash player11...</p>
	</div>

	<!-- 购买窗口 -->
	<div id="purchasePage" style="display: none">
		<input type="hidden"id="userId">
		<div id="backImg" onclick="backToApplication();">
			<input type="image" alt ="返回" src="assets/icon_web/back.png" 
					onmousedown="this.src='assets/icon_web/back_on.png';" 
					onmouseup="this.src='assets/icon_web/back.png'">
		</div>
		
		<div id="btnImg1" onclick="getPayToken(1);">
			<span  id="load1"  style="visibility:hidden"><img src="assets/icon_web/loading.gif"></span>
			<input type="image" alt ="充值" src="assets/icon_web/recharge.png" 
					onmousedown="this.src='assets/icon_web/recharge_on.png';" 
					onmouseup="this.src='assets/icon_web/recharge.png'">
		</div>
		
		<div id="btnImg2" onclick="getPayToken(2);">
			<span  id="load2"  style="visibility:hidden"><img src="assets/icon_web/loading.gif"></span>
			<input type="image" alt ="充值" src="assets/icon_web/recharge.png" 
					onmousedown="this.src='assets/icon_web/recharge_on.png';" 
					onmouseup="this.src='assets/icon_web/recharge.png'">
		</div>
		
		<div id="btnImg5" onclick="getPayToken(5);">
			<span  id="load5"  style="visibility:hidden"><img src="assets/icon_web/loading.gif"></span>
			<input type="image" alt ="充值" src="assets/icon_web/recharge.png" 
					onmousedown="this.src='assets/icon_web/recharge_on.png';" 
					onmouseup="this.src='assets/icon_web/recharge.png'">
		</div>
		
		<div id="btnImg10" onclick="getPayToken(10);">
			<span  id="load10"  style="visibility:hidden"><img src="assets/icon_web/loading.gif"></span>
			<input type="image" alt ="充值" src="assets/icon_web/recharge.png" 
					onmousedown="this.src='assets/icon_web/recharge_on.png';" 
					onmouseup="this.src='assets/icon_web/recharge.png'">
		</div>
	</div>
	
	<!-- 提交订单支付 -->
<!-- 	<form action="http://open.weibo.com/paytest/payTestPay.php" -->
	<form action="http://pay.weibo.com/wb/?c=pay"
		method="post" target="_top" style="display: none;" id="fmPay">
		<input type="hidden" id="return_url" name="return_url"/> 
		<input type="text" id="order_id" name="order_id" readonly /> 
		<input type="hidden" id="order_uid" name="order_uid" /> 
		<input type="text" id="desc" name="desc" readonly /> 
		<input type="hidden" id="appkey" name="appkey" />
		<input type="hidden" id="amount" name="amount" /> 
		<input type="hidden" id="version" name="version" /> 
		<input type="text" id="token" name="token" readonly />
	</form>

</body>
</html>
