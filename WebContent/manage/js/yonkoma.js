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
};


function checkNum(){
	var val = $("#frame").val();
	if(isNaN(val)){
		 $('#frameInfo').show().html('<font color="red" size="2">*请输入数字</font>');
	}
}

function cancelInfo(){
	$("#frame").val("");
	$('#frameInfo').hide();
}

function cancelPrompt(){
	 $('#prompt').hide();
	 $('#parent').attr("value","");
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
	var name = 	$("#name").val();
	var swf = $("#assetPath").val();
	var thubmnail = $("#thumbnailPath").val();
	if(name != null && name != "" &&
			swf !=null && swf !="" && thubmnail !=null && thubmnail!=""){
		return true;
	}else{
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
		
	}else{
		$('#prompt').show().html('<font color="red" size="2">提示：请正确填写内容</font>');
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
	if(checkNull()){
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
		
	}else{
		$('#prompt').show().html('<font color="red" size="2">提示：请正确填写内容</font>');
	}
}

