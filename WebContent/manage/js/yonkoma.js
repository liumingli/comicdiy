window.onload = function(){
	$('#assetUpload').fileupload({
		add : function(e, data) {
			//因为现在只传一个文件，所以可以用files[0]来取到
			var fileName = data.files[0].name;
			var regexp = /\.(swf)$/i;
		    if (!regexp.test(fileName)) {
		    	  $('#assetInfo').show().html('<img src="imgs/no.png">');
		    }else{
		    	 $('#assetInfo').show().html('<img src="imgs/loading.gif">');
				var jqXHR = data.submit().success(
						function(result, textStatus, jqXHR) {
							if(result == "reject"){
							    $('#assetInfo').show().html('<img src="imgs/no.png">');
							}else{
								$('#assetPath').val(result);
							    $('#assetInfo').show().html('<img src="imgs/ok.png">');
							    $('#assetUpload').attr('disabled','disabled');
							}
						}).error(
						function(jqXHR, textStatus, errorThrown) {
							 $('#assetInfo').show().html('<img src="imgs/no.png">');
						}).complete(
						function(result, textStatus, jqXHR) {
						});
		    }
		}
	});

	$('#thumbnailUpload').fileupload({
		add : function(e, data) {
			var fileName = data.files[0].name;
			var regexp = /\.(png)|(jpg)|(gif)$/i;
		    if (!regexp.test(fileName)) {
		    	  $('#thumbnailInfo').show().html('<img src="imgs/no.png">');
		    }else{
		    	 $('#thumbnailInfo').show().html('<img src="imgs/loading.gif">');
				var jqXHR = data.submit().success(
						function(result, textStatus, jqXHR) {
							if(result == "reject"){
							    $('#thumbnailInfo').show().html('<img src="imgs/no.png">');
							}else{
								$('#thumbnailPath').val(result);
								$('#thumbnailInfo').show().html('<img src="imgs/ok.png">');
								$('#thumbnailUpload').attr('disabled','disabled');
							}
						}).error(
						function(jqXHR, textStatus, errorThrown) {
							$('#thumbnailInfo').show().html('<img src="imgs/no.png">');
						}).complete(
						function(result, textStatus, jqXHR) {
						});
		    }
		}
	});
	
	//检查是否有主动画id参数
	var parentId = getQueryString("primaryId");
	var parentName = getQueryString("primaryName");
	if(parentId !=null && parentId !="" && parentName!="" && parentName !=null){
		addEnding(parentName,parentId);
	}
};

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
 }


function checkNum(){
	var val = $("#frame").val();
	if(isNaN(val)){
		 $('#frameInfo').show().html('<font color="red" size="2">*请输入数字</font>');
	}else{
		var r = /^\+?[1-9][0-9]*$/;//正整数 
		if(r.test(val)){
			 $('#frameInfo').hide();
		}else{
			 $('#frameInfo').show().html('<font color="red" size="2">*帧数必须大于0</font>');
		}
	}
}

function cancelInfo(){
	$("#frame").val("");
	$('#frameInfo').hide();
}

function cancelPrompt(){
	 $('#prompt').hide();
}

function emptyForm(){
	 $("#name").val("");
	 $("#frame").val("0");
	 $("#assetPath").val("");
	 $("#thumbnailPath").val("");
	 $('#assetUpload').removeAttr('disabled');
	 $('#thumbnailUpload').removeAttr('disabled');
	 $('#assetInfo').hide();
	 $('#thumbnailInfo').hide();
	 $('#prompt').hide();
	 $('#load').hide();
	 $('#parent').attr("value","");
	 $('#primary').attr("value","");
}

function createYonkoma(){
	var parent = $("#parent").val();
	if(parent != null && parent != ""){
		createEnding();
	}else{
		createPrimary();
	}
}

function checkNull(){
	 $('#prompt').show().html('');
	var name = 	$("#name").val();
	var swf = $("#assetPath").val();
	var thubmnail = $("#thumbnailPath").val();
	var val = $("#frame").val();
	var r = /^\+?[1-9][0-9]*$/;//正整数 
	
	if(name != null && name != "" && swf !=null && swf !="" && 
			thubmnail !=null && thubmnail!="" && r.test(val)){
		return true;
	}else{
		$('#prompt').show().html('<font color="red" size="2">提示：</font>');
		if(name == null || name == "" ){
			$('#prompt').append('<font color="red" size="2">请输入名称</font><br>');
		}
		if(!r.test(val)){
			 $('#prompt').append('<font color="red" size="2">帧数需为正整数</font><br>');
		}
		if(swf ==null || swf =="" ){
			$('#prompt').append('<font color="red" size="2">请上传swf文件</font><br>');
		}
		if(thubmnail ==null || thubmnail =="" ){
			$('#prompt').append('<font color="red" size="2">请上传缩略图文件</font><br>');
		}
		return false;
	}
}

function createPrimary(){
	if(checkNull()){
		$('#load').show().html('<img src="imgs/load.gif">');
		var name = 	$("#name").val();
		var frame = $("#frame").val();
		var swf = $("#assetPath").val();
		var thubmnail = $("#thumbnailPath").val();
		$.post('/comicdiy/comicapi', {
			'method'  : 'createPrimary',
			'name' : name,
			'frame' : frame,
			'swf' : swf,
			'thumbnail' : thubmnail
		}, 
		function (result) {
			$('#load').attr("style","display:none");
			if(result == 'false'){
				alert("上传主动画有误，请重试");
			}else{
				var param ="('"+name+"','"+result+"')";
				$('#prompt').show().html('<font color="red" size="2">提示：上传主动画成功，现在去<a href="javascript:addEnding'+param+';">添加结局</a></font>');
			}
			//清空表单
			//emptyForm();
		});
		
	}
}

function addEnding(nameParam,parentParam){
	emptyForm();
	$("#name").focus();
	$('#primary').attr("value",nameParam);
	$('#parent').attr("value",parentParam);
	$("#caption").html("<b>新增结局</b> (主动画名称："+nameParam+")");
	$("#frameLi").attr("style","display:none");
	$("#cancel").attr("style","display:none");
	$("#reback").removeAttr("style");
}

function createEnding(){
	if(checkEnding()){
		$('#load').show().html('<img src="imgs/load.gif">');
		var name = 	$("#name").val();
		var parent = $("#parent").val();
		var swf = $("#assetPath").val();
		var thubmnail = $("#thumbnailPath").val();
		$.post('/comicdiy/comicapi', {
			'method'  : 'createEnding',
			'name' : name,
			'parent' : parent,
			'swf' : swf,
			'thumbnail' : thubmnail
		}, 
		function (result) {
			$('#load').attr("style","display:none");
			if(result == 'false'){
				alert("提示：上传结局有误，请重试");
			}else{
				var primary = $("#primary").val();
				var param ="('"+primary+"','"+parent+"')";
				$('#prompt').show().html('<font color="red" size="2">提示：上传结局成功，点击<a href="javascript:addEnding'+param+';">继续添加</a></font>');
			}
		});
		
//	}else{
//		$('#prompt').show().html('<font color="red" size="2">提示：请正确填写表单内容</font>');
	}
}

function checkEnding(){
	 $('#prompt').show().html('');
	var name = 	$("#name").val();
	var swf = $("#assetPath").val();
	var thubmnail = $("#thumbnailPath").val();
	
	if(name != null && name != "" && swf !=null && swf !="" && 
			thubmnail !=null && thubmnail!=""){
		return true;
	}else{
		$('#prompt').show().html('<font color="red" size="2">提示：</font>');
		if(name == null || name == "" ){
			$('#prompt').append('<font color="red" size="2">请输入名称</font><br>');
		}
		if(swf ==null || swf =="" ){
			$('#prompt').append('<font color="red" size="2">请上传swf文件</font><br>');
		}
		if(thubmnail ==null || thubmnail =="" ){
			$('#prompt').append('<font color="red" size="2">请上传缩略图文件</font><br>');
		}
		return false;
	}
}

