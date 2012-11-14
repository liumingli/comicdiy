<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Login Page</title>
<style type="text/css">
	
body {
	height:438px;
	width:780px;
	background: url("imgs/first.jpg") no-repeat ;
	background-position: center;
}

#content{
	padding-top:300px;
	padding-left:400px;
}

#line{
	width:200px;
	padding:5px;
	text-align:center;
}

</style>

<script type="text/javascript" src="js/jquery.js" charset="utf-8"></script>

<script type="text/javascript">
document.onkeydown=function(event){
    var e = event || window.event || arguments.callee.caller.arguments[0];
     if(e && e.keyCode==13){ // enter 键
    	 loginSystem();
    }
};

function checkNull(){
	var account = $("#account").attr("value");
	var password =  $("#password").attr("value");
	if(account == null || password == null || account == "" || password == ""){
		return false;
	}
	return true;
}

function loginSystem(){
	var flag = checkNull();
	if(flag){
		var account = $("#account").attr("value");
		var password =  $("#password").attr("value");
		$.post('/comicdiy/comicapi', {
			'method'  : 'loginSystem',
			'account'	: account,
			'password' : password 
		}, 
		//回调函数
		function (result) {
			if(result == "true"){
				window.location.href = "manage.html";
			}else{
				$('#msg').html('<font color="red" size="2">提示：账户或密码输入错误</font>');
			}
		});
	}else{
		$('#msg').html('<font color="red" size="2">提示：账户和密码不能为空</font>');
	}
}

function clearMsg(){
	$('#msg').text("");
}
	
</script>

</head>
<body>
	<div id="content">
		<form id="contact">
			<div id ="line">
				账户 <input type="text" name="account" id="account" class="input" onfocus="clearMsg()" autofocus/>
			</div>
			<div id ="line">
				密码 <input type="password" name="password" id="password" class="input" />
			</div>
			<div id ="line">
				<input type="button" value="登录" name="submit" class="button" id="submit" onclick="loginSystem()"/> 
				<input type="reset" value="重置" name="reset" class="button" id="reset" onclick="clearMsg()"/>
			</div>
			<div id ="line"><span id="msg" ></span></div>
		</form>
	</div>
</body>
</html>