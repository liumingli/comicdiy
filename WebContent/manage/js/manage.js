document.onkeydown = function (e) { 
	var theEvent = window.event || e; 
	var code = theEvent.keyCode || theEvent.which; 
	if (code == 13) { 
		$("#search").click(); 
		return true;
	} 
};

//打开上传新素材页面
function uploadAssets(){
	window.open("assetsUpload.html", 'new','');
}

//获取所有可用素材
function getAllAssets(){
	$("#assetsList").html("");
	$.post('/comicdiy/comicapi', {
		'method'  : 'getAllAssets'
	}, 
	//回调函数
	function (result) {
		if(result.length > 0){
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
				console.log(enHoliday);
				var h = new Holiday();
				var val = h.convert(enHoliday);
				console.log(val);
				
				generateTd(val,key);
//				generateTd(result[key].holiday,key);
				//修改与删除
				generateOperate(result[key].id,key);
			}
		}
	}, "json");
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
	$("#name").val(result.name);
	$("#price").val(result.price);
	$("#holiday").val(result.holiday);
	$("#assetId").val(assetId);
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

function updateAsset(){
	var assetId=$("#assetId"+key).attr("value");
	var name=$("#name").attr("value");
	var price=$("#price").attr("value");
	var holiday=$("#holiday").attr("value");
	//根据id查出广告详情并填充newAd用于修改
	$.post('/comicdiy/comicapi', {
		'method'  : 'updateAssetById',
		'assetId' : assetId,
		'name' : name,
		'price' : price,
		'holiday' : holiday
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




//按条件搜索素材
function searchAssets(){
	$("#assetsList").html("");
	var keys = $('#keys').val();
	if(keys != "" && keys != null){
		$.post('/comicdiy/comicapi', {
			'method'  : 'searchByLabel',
			'keys' : keys
		}, 
		//回调函数
		function (result) {
			if(result.length > 0){
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
					console.log(enHoliday);
					var h = new Holiday();
					var val = h.convert(enHoliday);
					console.log(val);
					
					generateTd(val,key);
//					generateTd(result[key].holiday,key);
					//修改与删除
					generateOperate(result[key].id,key);
				}
			}
		}, "json");
	}
}


