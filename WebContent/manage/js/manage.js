//存储搜索的素材结果
var assetsList = new Array();
//存储页面与元素下标的对应关系
var matchupArray= new Array();

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
		if(result.length > 0){
			
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
	//根据id查出广告详情并填充newAd用于修改
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
			getAllAssets();
		});
	}
	
}

//检查字段空值
function checkNull(){
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
	
	if(checkNull()){
		//根据id查出广告详情并填充newAd用于修改
		$.post('/comicdiy/comicapi', {
			'method'  : 'updateAssetById',
			'assetId' : assetId,
			'name' : name,
			'price' : price,
			'holiday' : holiday,
			'type' : type
		}, 
		//回调函数
		function (result) {
			if(result.trim() == 'false'){
				 alert("操作有误，请重试！");
		     }
			
			//关闭编辑窗口
			disablePopup();  
		     //操作后刷新列表
			getAllAssets();
		});
	}
}

//按标签搜索素材
function searchAssets(){
	var keys = $('#keys').val();
	if(keys != "" && keys != null){
		$("#assetsList").html("");
		$.post('/comicdiy/comicapi', {
			'method'  : 'searchByLabel',
			'keys' : keys
		}, 
		//回调函数
		function (result) {
			if(result.length > 0){
				if(assetsList.length>0){
					assetsList = new Array();
				}
				for( key in result ){
					assetsList.push(result[key]);
				}
				
				var total = 0;
				if(parseInt(result.length % 12) == 0){
					total = parseInt(result.length / 12);
				}else{
					 total = parseInt(result.length / 12)+1;
				}
				
				if(matchupArray.length > 0){
					matchupArray = new Array();
				}
				for(var i=1;i<=total;i++){
					matchupArray[i]=(i-1)*12;
				}
				
				generatePaging();
				
				$('#total').html(total);
				$('#current').html(1);
				
				getSearchAssetsByPage(1);
			}else{
				$('#total').html(0);
				$('#current').html(0);
			}
		}, "json");
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
	var begin=matchupArray[pageNum];
	var end = begin+12;
	var total=$('#total').text();
	//判断如果是最后一页或者总长度小于pageSize
	if(pageNum == total || assetsList.length<12 ){
		end = assetsList.length;
	}
	$('#current').html(pageNum);
	for(var i=begin;i<end;i++){
		generateTr(i);
		//序号
		generateTd(parseInt(i)+1,i);
		//名称
		generateTd(assetsList[i].name,i);
		//缩略图
		generateImgTd(assetsList[i].thumbnail,assetsList[i].path,i);
		//价钱
		generateTd(assetsList[i].price,i);
		//上传时间
		generateTd(assetsList[i].uploadTime,i);
		//类型
		var type = assetsList[i].type;
		if(type == 'element'){
			type = '元件';
		}else{
			type = '主题';
		}
		generateTd(type,i);
		//分类
		generateTd(assetsList[i].category,i);
		//标签
		generateTd(assetsList[i].label,i);
		
		//关联节假日调用Holiday.js将英文转化成中文
		var enHoliday = assetsList[i].holiday;
		var h = new Holiday();
		var val = h.convert(enHoliday);
		generateTd(val,i);
		
		//修改与删除
		generateOperate(assetsList[i].id,i);
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
				}else{
					type = '主题';
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


