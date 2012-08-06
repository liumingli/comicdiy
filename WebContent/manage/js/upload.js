window.onload = function(){
	//加载页面时动态获取所有分类
	getAllCategory();
	
	$('#assetUpload').fileupload({
		add : function(e, data) {
			//因为现在只传一个文件，所以可以用files[0]来取到
			var fileName = data.files[0].name;
			var regexp = /\.(swf)$/i;
		    if (!regexp.test(fileName)) {
		    	  $('#assetInfo').show().html('<img src="imgs/no.png">');
		    }else{
				var jqXHR = data.submit().success(
						function(result, textStatus, jqXHR) {
							console.log(result);
							if(result == "reject"){
							    $('#assetInfo').show().html('<img src="imgs/no.png">');
							}else{
								$('#assetPath').val(result);
							    $('#assetInfo').show().html('<img src="imgs/ok.png">');
							    $('#assetUpload').attr('disabled','disabled');
							}
						}).error(
						function(jqXHR, textStatus, errorThrown) {
							console.log("error");
							 $('#assetInfo').show().html('<img src="imgs/no.png">');
						}).complete(
						function(result, textStatus, jqXHR) {
							console.log(result);
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
				var jqXHR = data.submit().success(
						function(result, textStatus, jqXHR) {
							console.log(result);
							if(result == "reject"){
							    $('#thumbnailInfo').show().html('<img src="imgs/no.png">');
							}else{
								$('#thumbnailPath').val(result);
								$('#thumbnailInfo').show().html('<img src="imgs/ok.png">');
								$('#thumbnailUpload').attr('disabled','disabled');
							}
						}).error(
						function(jqXHR, textStatus, errorThrown) {
							console.log("error");
							$('#thumbnailInfo').show().html('<img src="imgs/no.png">');
						}).complete(
						function(result, textStatus, jqXHR) {
							console.log(result);
						});
		    }
		}
	});
	
};

//获取所有分类
function getAllCategory(){
	console.log("get all category");
	$.post('/comicdiy/comicapi', {
		'method'  : 'getAllCategory'
	}, 
	//回调函数
	function (result) {
		if(result.length>0){
			for(key in result){
				$('#category').append("<option value='"+result[key].id+"'>"+result[key].name+"</option>");
			}
		}
	},"json");
}

function selfDefination(){
	$('#self').attr("style","display:none");
	$('#def').attr("style","display:visible");
}

//自定义新分类
function createCategory(){
	var val = $('#selfCategory').val();
	if(val != null && val != ""){
		$.post('/comicdiy/comicapi', {
			'method'  : 'createCategory',
			'name': val
		}, 
		function (result) {
			if(result!=null && result!=""){
				$('#category').append("<option value='"+result+"' selected='selected'>"+val+"</option>");
			}
		});
	}
	definationCancel();
}

function definationCancel(){
	$('#selfCategory').val("");
	$('#self').attr("style","display:visible");
	$('#def').attr("style","display:none");
}


function checkNull(){
		var name =  $("#name").val();
		
		var type=null;
	    var radio=document.getElementsByName("radiobutton");
		for(var i=0;i<radio.length;i++)
		{
		     if(radio.item(i).checked){
		         type=radio.item(i).getAttribute("value");  
		         break;
		     }else{
		    	 continue;
		     }
		}
		console.log(type);
		
		var price =  $("#price").val();
		var category =  $("#category").val();
		var label =  $("#label").val();
		var holiday =  $("#holiday").val();
		var assetPath =  $("#assetPath").val();
		var thumbnailPath =  $("#thumbnailPath").val();
		
		if(name !=null && name !="" && type !=null && type !="" && price !=null && price !="" 
			&& category !=null && category !="" && label !=null && label !="" && holiday !=null && holiday !="" 
			&& assetPath !=null && assetPath !="" && thumbnailPath !=null && thumbnailPath!="" ){
			
			return true;
		}else{
			
			return false;
		}
}

function createAsset(){
	if(checkNull()){
		var name =  $("#name").val();
		var type=null;
	    var radio=document.getElementsByName("radiobutton");
		for(var i=0;i<radio.length;i++)
		{
		     if(radio.item(i).checked){
		         type=radio.item(i).getAttribute("value");  
		         break;
		     }else{
		    	 continue;
		     }
		} 
		var price =  $("#price").val();
		var category =  $("#category").val();
		var label =  $("#label").val();
		var holiday =  $("#holiday").val();
		var assetPath =  $("#assetPath").val();
		var thumbnailPath =  $("#thumbnailPath").val();
		$.post('/comicdiy/comicapi', {
			'method'  : 'createAsset',
			'name' : name,
			'type' : type,
			'price' : price,
			'category' : category,
			'label' : label,
			'holiday' : holiday,
			'assetPath' : assetPath,
			'thumbnailPath' : thumbnailPath
		}, 
		function (result) {
			if(result == 'false'){
				alert("上传素材有误，请重试");
			}else{
				$('#prompt').show().html('<font color="red" size="2">上传成功</font>');
				console.log("new asset complete");
				//清空表单
				emptyForm();
			}
		});
	}
}

function emptyForm(){
	 $("#name").val("");
	 $("#price").val("0");
	 $("#category").val("");
	 $("#labelSpan").children().remove();
	 $("#label").val("");
	 $("#holiday").val("");
	 $("#assetPath").val("");
	 $("#thumbnailPath").val("");
	 $('#assetUpload').removeAttr('disabled');
	 $('#thumbnailUpload').removeAttr('disabled');
	 $('#assetInfo').hide();
	 $('#thumbnailInfo').hide();
	 closePopup();
}

function themeClick(){
    $('#price').val("");
}

function elementClick(){
    $('#price').val("0");
}

function checkNum(){
	var val = $("#price").val();
	console.log(typeof val);
	if(isNaN(val)){
		 $('#priceInfo').show().html('<font color="red" size="2">请输入数字</font>');
	}
}

function cancelInfo(){
	$("#price").val("");
	$('#priceInfo').hide();
}

function cancelPrompt(){
	$('#prompt').hide();
}

function operateLabel(){
	console.log("display Label panel");
//	$('#floatBoxBg').attr("style","display: block");
	$('#tag_wrap').attr('style',"display: block");
	
	//点击标签输入框后先是初始化父级标签，再初始化出默认选择的第一个父标签的子标签
	initLabel();
}

function initLabel(){
	$('#parentLable').children().remove();
	$.post('/comicdiy/comicapi', {
		'method'  : 'getAllParentLabel'
	}, 
	function (result) {
		if(result.length > 0){
			for(key in result){
				var parent ="('"+result[key].id+"','"+key+"')";
				if(key == 0){
					$('#parentLable').append('<a href = "javascript:initChildLable'+parent+'"  id="'+key+'" class="current" >'+result[key].name+'</a>');
					initChildLable(result[key].id,key);
				}else{
					$('#parentLable').append('<a href = "javascript:initChildLable'+parent+'"  id="'+key+'">'+result[key].name+'</a>');
				}
			}
		}else{
			alert("获取标签有误");
		}
	},"json");
	
	
}

function initChildLable(parent,num){
	$('#parentLable').children().attr("class","");
	$('#'+num).attr("class","current");
	$('#childLabel').children().remove();
	$.post('/comicdiy/comicapi', {
		'method'  : 'getLabelByParent',
		'parentId' : parent
	}, 
	function (result) {
		if(result.length > 0){
			for(key in result){
				$('#childLabel').append('<a href = "javascript:;"  id="'+num+key+'" value="'+result[key].id+'">'+result[key].name+'</a>');
				//加载子标签完成后，为其添加click事件
				chooseLable(num,key);
			}
		}else{
			alert("获取标签有误");
		}
	},"json");
	
}

function closePopup(){
	console.log("close Label panel");
	$('#tag_wrap').attr('style','display: none');
}

//为子标签的各个文字添加点击事件
function chooseLable(num,key){
	var idVal = num+key;
	var ob=document.getElementById(idVal);
	var labelId = document.getElementById(idVal).getAttribute("value");
	var labelName = document.getElementById(idVal).innerText;
	ob.onclick=function(){
		
		//向框里添加标签，判断若添加过就不添加，超过三个也不添加
		var old =  document.getElementById(labelId);
		
		var child = document.getElementById("labelSpan").childNodes.length;
		
		if(old == null && child < 3){
			
			console.log(labelId+"-----------"+labelName);

			//将要用来传参的标签id以逗号分隔的方式填入
			setLabelId(labelId);
			//将标签文字添加到labelPanel里
			setLabelName(labelId,labelName);
		}
	};
}

function setLabelId(labelId){
	var val = 	$('#label').val();
	if(val == null || val ==""){
		$('#label').val(labelId);
	}else{
		var newVal = val+","+labelId;
		$('#label').val(newVal);
	}
}

function setLabelName(labelId,labelName){
	var labelSpan = document.getElementById("labelSpan");
	var newLabel = new square(labelId,labelName);
	labelSpan.appendChild(newLabel);
}

function square(id,value){
	var span =  document.createElement("span");
	span.id = id;
	span.innerText = value;
	
	var del = document.createElement("img");
	del.src="imgs/delete.png";
	del.onclick= function () { 
		deleteThis(id); 
	};
	
	span.appendChild(del);
	return span;
};

function deleteThis(id){
	var labelSpan = document.getElementById("labelSpan");
	labelSpan.removeChild(document.getElementById(id));
	

	var labelIds = 	$('#label').val();
	console.log("delete before "+labelIds);
	var strs= new Array(); //定义一数组
	strs=labelIds.split(","); //字符分割      
	for (var i=0 ; i<strs.length ; i++ )    
    {    
       //分割后的字符，与要删除的相比较
        if(id == strs[i]){
        	strs.pop();
        }
    } 
	var newIds =strs.join(",");
	console.log("delete after "+newIds);
	$('#label').val(newIds);
};
