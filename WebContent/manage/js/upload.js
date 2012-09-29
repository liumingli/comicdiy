//存放三大分类与项下各小分类
var elementArray = new Array();
var sceneArray = new Array();
var themeArray = new Array();

var tabLength = 500;

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

//获取所有分类
function getAllCategory(){
	var logger = new Logger();
	logger.trace(">>>get all category");
	$.post('/comicdiy/comicapi', {
		'method'  : 'getAllCategory'
	}, 
	//回调函数
	function (result) {
		if(result.length>0){
			for(key in result){
				if(result[key].parent=="element"){
					elementArray.push(result[key]);
					$('#category').append("<option value='"+result[key].id+"'>"+result[key].name+"</option>");
				}
				if(result[key].parent=="scene"){
					sceneArray.push(result[key]);
				}
				if(result[key].parent=="theme"){
					themeArray.push(result[key]);
				}
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
	var type="";
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
	if(val != null && val != ""){
		$.post('/comicdiy/comicapi', {
			'method'  : 'createCategory',
			'name': val,
			'parent': type
		}, 
		function (result) {
			if(result!=null && result!=""){
				var obj = new Object();
				obj.id=result;
				obj.name=val;
				if(type=="element"){
					elementArray.push(obj);
				}else if(type == "theme"){
					themeArray.push(obj);
				}else if(type == "scene"){
					sceneArray.push(obj);
				}
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
		$('#prompt').show().html('<img src="imgs/load.gif">');
		var name =  $("#name").val();
		var type='';
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
				//清空表单
				emptyForm();
			}
		});
	}else{
		$('#prompt').show().html('<font color="red" size="2">请填写内容</font>');
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
	  $('#category').children().remove();
    for(var i=0;i<themeArray.length;i++){
    	  $('#category').append("<option value='"+themeArray[i].id+"'>"+themeArray[i].name+"</option>");
    }
}

function elementClick(){
    $('#price').val("0");
	$('#category').children().remove();
    for(var i=0;i<elementArray.length;i++){
    	  $('#category').append("<option value='"+elementArray[i].id+"'>"+elementArray[i].name+"</option>");
    }
}

function sceneClick(){
	 $('#price').val("");
	  $('#category').children().remove();
	    for(var i=0;i<sceneArray.length;i++){
	    	  $('#category').append("<option value='"+sceneArray[i].id+"'>"+sceneArray[i].name+"</option>");
	    }
}

function checkNum(){
	var val = $("#price").val();
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
//	$('#floatBoxBg').attr("style","display: block");
	$('#tag_wrap').attr('style',"display: block");
	
	//点击标签输入框后先是初始化父级标签，再初始化出默认选择的第一个父标签的子标签
	initLabel();
}

//存储全部的标签
var parentLabelList = new Array();

//存储页面与元素下标的对应关系
var matchupArray= new Array();

var sumLength = 0;

function initLabel(){
	//判断标签框是否已加载
	var div = document.getElementById('parentLable');
	var len = div.childNodes.length;
	
	if(len==0){
		$('#parentLable').append('<a href="#" id="load"><img src="imgs/loading.gif"></a>');
		
		$.post('/comicdiy/comicapi', {
			'method'  : 'getAllParentLabel'
		}, 
		function (result) {
			$('#parentLable').children().remove();
			$('#load').remove();
			if(result.length > 0){
				//给数组赋值
				for(key in result){
					parentLabelList.push(result[key]);
				}
				
				//根据长度判断第一次显示几个
				for(key in result){
					var parent ="('"+result[key].id+"','"+key+"')";
					if(key == 0){
						$('#parentLable').append('<a href = "javascript:initChildLable'+parent+'"  id="'+key+'" class="current" >'+result[key].name+'</a>');
						initChildLable(result[key].id,key);
						
					}else{
						var html = '<a href = "javascript:initChildLable'+parent+'"  id="'+key+'">'+result[key].name+'</a>';
						var everyLength =$('#' + (parseInt(key)-1).toString()).width(); 
						sumLength+= everyLength;
						if(sumLength > tabLength)
						{
							var pageNum=1;
							//存入页码与开始元素的关系
							matchupArray[pageNum]=0;
							addRight(pageNum,key);
							break;
						}else{
							$('#parentLable').append(html);
						}
					}
				}
			}else{
				alert("无标签");
			}
	},"json");
  }
}

//向后翻页
//第num页，元素以key为下标的开始
function nextPage(key,num){
	sumLength = 0;
	$('#parentLable').children().remove();
	
	//从第key个元素开始添加第num页的标签
	for(var i=parseInt(key);i<parentLabelList.length;i++){
		var parent ="('"+parentLabelList[i].id+"','"+i+"')";
		//当前页的第一个标签，初始状态为选中
		if(i == parseInt(key)){
			$('#parentLable').append('<a href = "javascript:initChildLable'+parent+'"  id="'+i+'" class="current" >'+parentLabelList[i].name+'</a>');
			initChildLable(parentLabelList[i].id,i);
		}else{
		    var  html = '<a href = "javascript:initChildLable'+parent+'"  id="'+i+'">'+parentLabelList[i].name+'</a>';
			//累加长度
			var everyLength =$('#' + (i-1)).width(); 
			sumLength+= everyLength;
			
			if(sumLength > tabLength){
				//判断如果还可翻页则继续
				addRight(num,i);
				break;
			}else{
				$('#parentLable').append(html);
				$('.tip_right').hide();
			}
		}
	}
	//添加向前翻页
	addLeft(num-1,key);
}

function addLeft(num,key){
	if(key == 0){
		$('.tip_left').hide();
	}else{
		$('.tip_left').show();
		var para ="('"+key+"',"+num+")";
		$('#leftTip').attr('onclick','previousPage'+para);
	}
}

//往前翻页
function previousPage(key,num){
	sumLength = 0;
	$('#parentLable').children().remove();
	
	//取到当前页数是从哪个元素位置开始
	var index = matchupArray[num];
	for(var i = index;i<parentLabelList.length;i++){
		var parent ="('"+parentLabelList[i].id+"','"+i+"')";
		if(i == index){
			$('#parentLable').append('<a href = "javascript:initChildLable'+parent+'"  id="'+i+'" class="current" >'+parentLabelList[i].name+'</a>');
			initChildLable(parentLabelList[i].id,i);
		}else{
			var html = '<a href = "javascript:initChildLable'+parent+'"  id="'+i+'">'+parentLabelList[i].name+'</a>';
		
			var everyLength =$('#' +(i-1)).width(); 
			sumLength+= everyLength;
			if(sumLength > tabLength){
				addLeft(num-1,index);
				addRight(num,key);
				break;
			}else{
				$('#parentLable').append(html);
			}
		}
	}
}

function addRight(num,key){
	$('.tip_right').show();
	var newPage=num+1;
	var para ="('"+key+"',"+newPage+")";
	$('#rightTip').attr('onclick','nextPage'+para);
	//存入页码与开始元素的关系
	matchupArray[newPage]=key;
}

function initChildLable(parent,num){
	$('#parentLable').children().attr("class","");
	$('#'+num).attr("class","current");
	$('#childLabel').children().remove();
	$('#childLabel').append('<a href="#" id="load"><img src="imgs/loading.gif"></a>');
	$.post('/comicdiy/comicapi', {
		'method'  : 'getLabelByParent',
		'parentId' : parent
	}, 
	function (result) {
		$('#load').remove();
		if(result.length > 0){
			for(key in result){
				$('#childLabel').append('<a href = "javascript:;"  id="'+num+key+'" value="'+result[key].id+'">'+result[key].name+'</a>');
				//加载子标签完成后，为其添加click事件
				chooseLable(num,key);
			}
		}else{
			$('#childLabel').append('<a href="#"><font color="red">无子标签</font></a>');
		}
	},"json");
}

function closePopup(){
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
		
		if(old == null && child < 4){
			
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
	$('#label').val(newIds);
};
