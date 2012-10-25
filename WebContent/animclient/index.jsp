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
	background: url("assets/wood40.png") repeat scroll 0 0 transparent;
}

object:focus {
	outline: none;
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
	
	//为了取到站内应用的浏览器地址
	function showBrowserURL(){
  		var url = (window.location != window.parent.location) ? document.referrer : document.location;
  		return url;
  	}

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
		var swfVersionStr = "11.2.0";
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
		swfobject.embedSWF("Main.swf", divId, w, h, swfVersionStr, xiSwfUrlStr,
				flashvars, params, attributes);
		swfobject.createCSS("#" + divId, "display:block;text-align:left;");
	}

	//生成
	function generatePurchasePage(cent) {
		$('#flashContent').attr("style", "display:none");
		$('#purchasePage').attr("style", "display:block");
		$('#count').attr("value", cent);
	}

	//付款
	function getPayToken() {
		var amount = $('#count').val();
		var userId = $('#userId').val();
		$.post("/comicdiy/comicapi", {
			'method' : 'getPayToken',
			'userId' : userId,
			'amount' : amount
			
		}, function(result) {
			if (result.length > 0) {
				for (key in result) {
					console.log(result[key].amount);
					$('#return_url').attr("value", result[key].returnUrl);
					$('#order_id').attr("value", result[key].orderId);
					$('#order_uid').attr("value", result[key].orderUid);
					$('#desc').attr("value", result[key].desc);
					$('#appkey').attr("value", result[key].appKey);
					$('#amount').attr("value", result[key].amount);
					$('#token').attr("value", result[key].payToken);
					$('#version').attr("value", result[key].version);
				}
			}
		}, "json");
		//提交form
		$('#fmPay').submit();
	}
</script>

</head>

<body>

	<div id="flashContent" style="display: none">
		<p>To view this page ensure that Adobe Flash Player version need
			flash player11...</p>
	</div>

	<div id="purchasePage" style="display: none">
		点券数：<input type="text" id="count" value=""> <input
			type="hidden" value="1964124547" id="userId"> <input
			type="button" id="submit" value="充值" onclick="getPayToken();">
	</div>

	<form action="http://open.weibo.com/paytest/payTestPay.php"
		method="post" target="_top" style="display: none;" id="fmPay">
		<input type="hidden" id="return_url" /> 
		<input type="text" id="order_id" readonly /> 
		<input type="hidden" id="order_uid" /> 
		<input type="text" id="desc" readonly /> 
		<input type="hidden" id="appkey" />
		<input type="hidden" id="amount" /> 
		<input type="hidden" id="version"/> 
		<input type="text" id="token" readonly />
	</form>

</body>
</html>
