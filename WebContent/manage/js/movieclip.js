window.onload = function(){
	$('#swfUpload').fileupload({
		add : function(e, data) {
			//因为现在只传一个文件，所以可以用files[0]来取到
			var fileName = data.files[0].name;
			var regexp = /\.(swf)$/i;
		    if (!regexp.test(fileName)) {
		    	  $('#swfInfo').show().html('<img src="imgs/no.png">');
		    }else{
		    	 $('#swfInfo').show().html('<img src="imgs/loading.gif">');
				var jqXHR = data.submit().success(
						function(result, textStatus, jqXHR) {
							if(result == "reject"){
							    $('#swfInfo').show().html('<img src="imgs/no.png">');
							}else{
								$('#swfPath').val(result);
							    $('#swfInfo').show().html('<img src="imgs/ok.png">');
							    $('#swfUpload').attr('disabled','disabled');
							}
						}).error(
						function(jqXHR, textStatus, errorThrown) {
							 $('#swfInfo').show().html('<img src="imgs/no.png">');
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


function uploadClip(){
	if(checkNull()){
		$('#load').show().html('<img src="imgs/loading.gif">');
		var name = 	$("#name").val();
		var swf = $("#swfPath").val();
		var thumbnail = $("#thumbnailPath").val();
		var type =  $("#type").val();
		$.post('/comicdiy/comicapi', {
			'method'  : 'createMovieClip',
			'name' : name,
			'swf' : swf,
			'thumbnail' : thumbnail,
			'type' : type
		}, 
		function (result) {
			$('#load').hide();
			if(result == "true"){
				$('#prompt').show().html('<font color="red" size="2">提示：上传成功 ，<a href = "javascript:emptyForm();">继续上传</a></font>');
			}else{
				$('#prompt').show().html('<font color="red" size="2">提示：上传失败，<a href = "javascript:emptyForm();">重试</a></font>');
			}
		});
	}else{
		$('#prompt').show().html('<font color="red" size="2">提示：请正确填写表单内容</font>');
	}
}


function checkName(){
	$('#nameInfo').show().html('<img src="imgs/loading.gif">');
	var name = 	$("#name").val();
	if(name != null && name !=""){
		$.post('/comicdiy/comicapi',{
			"method" : "checkClipName",
			'name' : name
		},
		function(result){
			$('#nameInfo').hide();
			if(result == "true"){
				$('#nameInfo').show().html('<img src="imgs/ok.png">');
			}else{
				$('#nameInfo').show().html('<img src="imgs/no.png">');
				$("#name").focus();
			}
		});
	}else{
		$('#nameInfo').show().html('<img src="imgs/no.png">');
		$("#name").focus();
	}
}

function checkNull(){
	var name = 	$("#name").val();
	var swf = $("#swfPath").val();
	var thumbnail = $("#thumbnailPath").val();
	var type =  $("#type").val();
	if(name != null && name != "" && swf != null && swf != "" 
		&& thumbnail != null && thumbnail != "" && type != null && type != ""){
		return true;
	}else{
		return false;
	}
}


function emptyForm(){
	 $("#name").val("");
	 $("#name").focus();
	 $("#swfPath").val("");
	 $("#thumbnailPath").val("");
	 $('#swfUpload').removeAttr('disabled');
	 $('#thumbnailUpload').removeAttr('disabled');
	 $('#swfInfo').hide();
	 $('#thumbnailInfo').hide();
	 $('#nameInfo').hide();
	 $('#prompt').hide();
	 $('#load').hide();
}