<%@page import="javax.swing.text.Document"%>
<%@ page language="java" import="java.util.*,weibo4j.Oauth"
	contentType="text/html; charset=UTF-8"%>
<%
	response.setHeader("P3P", "CP=CAO PSA OUR");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>哇推--指尖上的动漫创作与分享工具</title>
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
		if (accessToken == null || accessToken == "" || accessToken == "null") {
			//弹出授权层
			authLoad();
		} else {
			//看用户是否存在,存库或更新库
			operateUser(userId, accessToken);
			//动态创建应用
			createSWFById('flashContent', userId, "760", "600");
		}
	};
	
	//弹出授权层
	function authLoad() {
		App.AuthDialog.show({
			client_id : '2069364265', //必选，appkey
			redirect_uri : 'http://apps.weibo.com/watuiup', //必选，授权后的回调地址
			height : 60
		//可选，默认距顶端120px
		});
	}

	//存或更新用户
	function operateUser(userId, accessToken) {
		console.log(userId+"================="+typeof userId);
		$.post("/comicdiy/comicapi", {
			'method' : 'operateWeiboUser',
			'userId' : userId,
			'accessToken' : accessToken,
			'application' : "哇推",
			'platform' : "sina"
		}, function(result) {
			//返回ture和false
		});
	}
	
	//动态创建
	function createSWFById(divId, userId, w, h) {
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
		swfobject.embedSWF("Watui.swf?v=1.0&date=1204", divId, w, h, swfVersionStr, xiSwfUrlStr,
				flashvars, params, attributes);
		swfobject.createCSS("#" + divId, "display:block;text-align:left;");
	}

</script>

</head>

<body>

	<div id="flashContent" style="display: none">
		<p>To view this page ensure that Adobe Flash Player version need
			flash player11...</p>
	</div>

</body>
</html>
