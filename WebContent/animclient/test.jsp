<%@page import="javax.swing.text.Document"%>
<%@ page language="java" import="java.util.*,weibo4j.Oauth"
	contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>支付测试页</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript" src="jquery.js"></script>
	
<script type="text/javascript">
	
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
					console.log(key);
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
			//提交form
			console.log("return_url : "+$('#return_url').val());
			console.log("order_id : "+$('#order_id').val());
			console.log("order_uid : "+$('#order_uid').val());
			console.log("desc : "+$('#desc').val());
			console.log("appkey : "+$('#appkey').val());
			console.log("amount : "+$('#amount').val());
			console.log("token : "+$('#token').val());
			console.log("version : "+$('#version').val());
					
			//$('#fmPay').submit();
			
		}, "json");
	}
	
</script>

</head>

<body>
	
	<div id="purchasePage" style="display: block">
		点券数：<input type="text" id="count" value=""> <input
			type="hidden" value="1964124547" id="userId"> <input
			type="button" id="submit" value="充值" onclick="getPayToken();">
	</div>

	<form action="http://open.weibo.com/paytest/payTestPay.php"
		method="post" target="_top" style="display: none;" id="fmPay">
		<input type="hidden" id="return_url" name="return_url"/> 
		<input type="text" id="order_id" name="order_id"  /> 
		<input type="hidden" id="order_uid" name="order_uid" /> 
		<input type="text" id="desc" name="desc"  /> 
		<input type="hidden" id="appkey" name="appkey" />
		<input type="hidden" id="amount" name="amount" /> 
		<input type="hidden" id="version" name="version" /> 
		<input type="text" id="token" name="token" />
	</form>
	
</body>
</html>
