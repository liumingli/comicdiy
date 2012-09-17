
//打开上传新素材页面
function uploadAssets(){
//	window.open("assetsUpload.html", 'new','');
	window.location.href = "assetsUpload.html";
}

//获取所有可用素材
function getAllAssets(){
	$.post('/comicdiy/comicapi', {
		'method'  : 'getAllAssetsCount'
	}, 
	//回调函数
	function (result) {
		if(result > 0){
			
			var total = 0;
			if(parseInt(result%12) == 0){
				total = parseInt(result / 12);
			}else{
				 total = parseInt(result / 12)+1;
			}
			$('#total').html(total);
			$('#current').html(1);
			
			getAssetsByPage(1);
		
		}
	});
}

function generateTr(key){
	$('<tr></tr>').appendTo($('#assetsList'))
	.attr("id","line"+key);
}

//生成各td
function generateTd(txt,key){
	$('<td></td>').appendTo($('#line'+key))
	.text(txt);
}

//生成缩略图
function generateImgTd(thumbnail,path,key){
	var tr = document.getElementById("line"+key);
	var para = document.createElement("td");
	var a = document.createElement("a");
	para.appendChild(a);
	var img = document.createElement("img");
	var local = '/comicdiy/comicapi?method=getThumbnail&relativePath=';
	img.setAttribute("src",local+thumbnail);
	img.setAttribute("height",36);
	a.appendChild(img);
	//var swf =  '/comicdiy/comicapi?method=getAssetFile&relativePath=';
	var swf =  '/comicdiy/comicapi?method=getAssetFile&relativePath=';
	a.setAttribute("href",swf+path);
	a.setAttribute("target", "_blank");
	tr.appendChild(para);
}

//生成 编辑和删除 选项
function generateOperate(id,key){
	$('<td></td>').appendTo($('#line'+key))
	.append($('<a></a>')
		.append($('<img>').attr("src","imgs/edit.png"))
		.attr("href","javascript:getAssetById("+key+");"))
	.append($('<input type="hidden"></input>')
		.attr("id","assetId"+key)
		.attr("value",id));
	
	$('<td></td>').appendTo($('#line'+key))
	.append($('<a></a>')
		.append($('<img>').attr("src","imgs/del.png"))
		.attr("href","javascript:deleteAssetById("+key+");"))
	.append($('<input type="hidden"></input>')
		.attr("id","assetId"+key)
		.attr("value",id));
}

function getAssetById(key){
	var assetId=$("#assetId"+key).attr("value");
	//根据id查出
	$.post('/comicdiy/comicapi', {
		'method'  : 'getAssetById',
		'assetId' : assetId
	}, 
	//回调函数
	function (result) {
		if(result != "[]"){
			 createEditWindow(result,assetId);
		}else{
			alert("获取素材详情有误，请重试！");
		}
	}, "json");
}

function createEditWindow(result,assetId){
	centerPopup();
	loadPopup();
	$("#name").focus();
	$("#name").val(result.name);
	$("#price").val(result.price);
	$("#holiday").val(result.holiday);
	$("#assetId").val(assetId);
	$("#label").val(result.labelIds);
	
	$("#labelSpan").children().remove();
	
	if(result.labelIds.indexOf(",")>0){
		var idArr =new Array();
		idArr = result.labelIds.split(",");
		var labArr = new Array();
		labArr = result.label.split(",");
		for(var i =0;i<idArr.length;i++){
			setLabelName(idArr[i],labArr[i]);
		}
	}else{
		setLabelName(result.labelIds,result.label);
	}
	
	var radio=document.getElementsByName("radiobutton");
	for(var i=0;i<radio.length;i++)
	{
		 var type=radio.item(i).getAttribute("value");  
		 if(type == result.type){
			 radio.item(i).checked=true;
	         break;
	     }else{
	    	 continue;
	     }
	}
}


function confirmDel(){
    if(confirm("确定要删除吗？删除将不能恢复！"))
   		 return true;
    else
   		 return false;
}

function deleteAssetById(key){
	if(confirmDel()){
		var assetId=$("#assetId"+key).attr("value");
		$.post('/comicdiy/comicapi', {
			'method'  : 'deleteAssetById',
			'assetId' : assetId
		}, 
		//回调函数
		function (result) {
			if(result.trim() == 'false'){
				 alert("操作有误，请重试！");
		     }
		    //操作后刷新列表
			var num = $('#current').text();
			getAssetsByPage(num);
		});
	}
	
}

//检查字段空值
function checkForm(){
	var assetId=$("#assetId").attr("value");
	var name=$("#name").attr("value");
	var price=$("#price").attr("value");
	var holiday=$("#holiday").attr("value");
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
	if(assetId == "" || assetId == null || name == "" || name == null ||
			price == "" || price == null || holiday == "" || holiday == null ||
			type == "" || type == null){
		return false;
	}else{
		return true;
	}
}

function updateAsset(){
	var assetId=$("#assetId").attr("value");
	var name=$("#name").attr("value");
	var price=$("#price").attr("value");
	var holiday=$("#holiday").attr("value");
	var labelIds=$("#label").attr("value");
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
	if(checkForm()){
		//根据id查出广告详情并填充newAd用于修改
		$.post('/comicdiy/comicapi', {
			'method'  : 'updateAssetById',
			'assetId' : assetId,
			'name' : name,
			'price' : price,
			'holiday' : holiday,
			'type' : type,
			'labelIds': labelIds
		}, 
		//回调函数
		function (result) {
			if(result.trim() == 'false'){
				 alert("操作有误，请重试！");
		     }
			
			//关闭编辑窗口
			disablePopup();  
		     //操作后刷新列表
			var num = $('#current').text();
			getAssetsByPage(num);
		});
	}
}

//按标签搜索素材
function searchAssets(){
	var keys = $('#keys').val();
	if(keys != "" && keys != null){
		$("#assetsList").html("");
		$.post('/comicdiy/comicapi', {
			'method'  : 'getSearchCountByLabel',
//			'method' : 'getSearchCountByLabelAndType',
//			'type' : 'element',
			'keys' : keys
		}, 
		//回调函数
		function (result) {
			if(result > 0){
				generatePaging();
				
				var total = 0;
				if(parseInt(result%12) == 0){
					total = parseInt(result / 12);
				}else{
					 total = parseInt(result / 12)+1;
				}
				$('#total').html(total);
				$('#current').html(1);
				
				
				getSearchAssetsByPage(1);
			
			}else{
				$('#total').html(0);
				$('#current').html(0);
			}
		});
	}
}

function generatePaging(){
	$("#paging").children().remove();
	var html = '<span id="firstPage"><a href="javascript:getFirstPage()">第一页</a></span>';
	html+='<span id="prevPage"><a href="javascript:getPrevPage()">上一页</a></span>';
	html+='<span id="nextPage"><a href="javascript:getNextPage()">下一页</a></span>';
	html+='<span id="lastPage"><a href="javascript:getLastPage()">最后一页</a></span>';
	html+='<span id="currentPage">当前第<span id="current"></span>页</span>';
	html+='<span id="totalPage">共<span id="total"></span>页</span>';
	$("#paging").append(html);
}

function getSearchAssetsByPage(pageNum){
	$('#assetsList').children().remove();
	var keys = $('#keys').val();
	if(keys != "" && keys != null){
		$("#assetsList").html("");
		$.post('/comicdiy/comicapi', {
			'method'  : 'searchByLabelPage',
//			'method' : 'searchByLabelAndTypePage',
//			'type' : 'element',
			'keys' : keys,
			'pageNum' : pageNum
		}, 
		function(result){
			if(result.length > 0){
			
			$('#current').html(pageNum);
			
			for( key in result ){
				generateTr(key);
				//序号
				generateTd(parseInt(key)+1,key);
				//名称
				generateTd(result[key].name,key);
				//缩略图
				generateImgTd(result[key].thumbnail,result[key].path,key);
				//价钱
				generateTd(result[key].price,key);
				//上传时间
				generateTd(result[key].uploadTime,key);
				//类型
				var type = result[key].type;
				if(type == 'element'){
					type = '元件';
				}else if(type == 'theme'){
					type = '情景';
				}else if(type == "scene"){
					type = "场景";
				}
				generateTd(type,key);
				//分类
				generateTd(result[key].category,key);
				//标签
				generateTd(result[key].label,key);
				
				//关联节假日调用Holiday.js将英文转化成中文
				var enHoliday = result[key].holiday;
				var h = new Holiday();
				var val = h.convert(enHoliday);
				
				generateTd(val,key);
//				generateTd(result[key].holiday,key);
				//修改与删除
				generateOperate(result[key].id,key);
				}
			}
		}, "json");
	}
}


//第一页的时候传1
function getAssetsByPage(pageNum){
	$("#assetsList").html("");
	$.post('/comicdiy/comicapi', {
		'method'  : 'getAssetsByPage',
		'pageNum' : pageNum
	}, 
	//回调函数
	function (result) {
		if(result.length > 0){
			
			$('#current').html(pageNum);
			
			for( key in result ){
				generateTr(key);
				//序号
				generateTd(parseInt(key)+1,key);
				//名称
				generateTd(result[key].name,key);
				//缩略图
				generateImgTd(result[key].thumbnail,result[key].path,key);
				//价钱
				generateTd(result[key].price,key);
				//上传时间
				generateTd(result[key].uploadTime,key);
				//类型
				var type = result[key].type;
				if(type == 'element'){
					type = '元件';
				}else if(type == 'theme'){
					type = '情景';
				}else if(type == "scene"){
					type = "场景";
				}
				generateTd(type,key);
				//分类
				generateTd(result[key].category,key);
				//标签
				generateTd(result[key].label,key);
				
				//关联节假日调用Holiday.js将英文转化成中文
				var enHoliday = result[key].holiday;
				var h = new Holiday();
				var val = h.convert(enHoliday);
				
				generateTd(val,key);
//				generateTd(result[key].holiday,key);
				//修改与删除
				generateOperate(result[key].id,key);
			}
		}
	}, "json");
}


function getFirstpageAssets(){
	var num = $('#current').text();
	//最后一页，如果当前是最后一页
	if(parseInt(num) == 1){
		return;
	}else{
		getAssetsByPage(1);
	}
}


function getPrevpageAssets(){
	var num = $('#current').text();
	var pageNum = parseInt(num) - 1;
	//前一页，如果当时是第一页
	if(parseInt(num) == 1){
		return;
	}else{
		getAssetsByPage(pageNum);
	}
}


function getNextpageAssets(){
	var sum = $('#total').text();
	var num = $('#current').text();
	var pageNum = parseInt(num) + 1;
	//后一页，如果当前是最后一页
	if(sum == num){
		return;
	}else{
		getAssetsByPage(pageNum);
	}

}

function getLastpageAssets(){
	var sum = $('#total').text();
	var num = $('#current').text();
	//最后一页，如果当前是最后一页
	if( sum == num){
		return;
	}else{
		getAssetsByPage(sum);
	}
}


function getFirstPage(){
	var num = $('#current').text();
	//最后一页，如果当前是最后一页
	if(parseInt(num) == 1){
		return;
	}else{
		getSearchAssetsByPage(1);
	}
}


function getPrevPage(){
	var num = $('#current').text();
	var pageNum = parseInt(num) - 1;
	//前一页，如果当时是第一页
	if(parseInt(num) == 1){
		return;
	}else{
		getSearchAssetsByPage(pageNum);
	}
}


function getNextPage(){
	var sum = $('#total').text();
	var num = $('#current').text();
	var pageNum = parseInt(num) + 1;
	//后一页，如果当前是最后一页
	if(sum == num){
		return;
	}else{
		getSearchAssetsByPage(pageNum);
	}

}

function getLastPage(){
	var sum = $('#total').text();
	var num = $('#current').text();
	//最后一页，如果当前是最后一页
	if( sum == num){
		return;
	}else{
		getSearchAssetsByPage(sum);
	}
}


