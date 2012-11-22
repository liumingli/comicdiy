//打开上传新素材页面
function uploadYonkoma(){
//	window.open("assetsUpload.html", 'new','');
    var parent = $("#parent").val();
    var primary = $("#primary").val();
	window.location.href = "yonkomaUpload.html?primaryId="+parent+"&primaryName="+primary;
}

function getPrimary(){
	$('#parent').attr("value","");
	$.post('/comicdiy/comicapi', {
		'method'  : 'getPrimaryCount'
	}, 
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
			
			getPrimaryByPage(1);
		
		}else{
			$('#total').html(0);
			$('#current').html(0);
			$('#primaryList').children().remove();
		}
	});
}

function getPrimaryByPage(pageNum){

	$.post('/comicdiy/comicapi', {
		'method'  : 'getPrimaryByPage',
		'pageSize' : 12,
		'pageNum' : pageNum
	}, 
	function (result) {
		$('#current').html(pageNum);
		generatePrimary(result);
		
	},"json");
}


function generatePrimary(result){
	$('#yonkomaList').children().remove();
	for(key in result){
		var id= result[key].id;
		var thumbnail = result[key].thumbnail;
		var name = result[key].name;
		var swf = result[key].swf;
		var frame = result[key].frame;
		//行数 现在显示12个，3行4列
		var num = Math.ceil((parseInt(key)+1)/4);
		generateTr(num);
		generatePrimaryTd(id,name,thumbnail,swf,frame,num);
	}
}

function generateTr(num){
	$('<tr></tr>').appendTo($('#yonkomaList'))
	.attr("id","line"+num);
}

//生成主动画
function generatePrimaryTd(id,name,thumbnail,swf,frame,num){
	var tr = document.getElementById("line"+num);
	var para = document.createElement("td");
	var a = document.createElement("a");
	para.appendChild(a);
	var img = document.createElement("img");
	var local = '/comicdiy/comicapi?method=getThumbnail&relativePath=';
	img.setAttribute("src",local+thumbnail);
	img.setAttribute("title", "主动画名称："+name);
	a.appendChild(img);
//	a.setAttribute("href","javascript:getEnding('"+id+"','"+name+"')");
//	tr.appendChild(para);
	var path =  '/comicdiy/comicapi?method=getAssetFile&relativePath=';
	a.setAttribute("href", path+swf);
	a.setAttribute("target", "_blank");
	
	//生成编辑、删除和查看结局三个按钮
	generatePrimaryEditTd(id,name,frame,para);
	
	tr.appendChild(para);
}


function generatePrimaryEditTd(id,name,frame,td){
	var aCheck = document.createElement("a");
	var imgCheck = document.createElement("img");
	imgCheck.setAttribute("src","imgs/eye.png");
	imgCheck.setAttribute("title","查看结局");
	aCheck.appendChild(imgCheck);
	aCheck.setAttribute("href","javascript:getEnding('"+id+"','"+name+"')");
	td.appendChild(aCheck);
	
	var aEdit = document.createElement("a");
	var imgEdit = document.createElement("img");
	imgEdit.setAttribute("src", "imgs/edit.png");
	imgEdit.setAttribute("title", "编辑");
	aEdit.appendChild(imgEdit);
	aEdit.setAttribute("href","javascript:editPrimary('"+id+"','"+name+"','"+frame+"')");
	td.appendChild(aEdit);
	
	var aDel = document.createElement("a");
	var imgDel = document.createElement("img");
	imgDel.setAttribute("src", "imgs/del.png");
	imgDel.setAttribute("title", "删除");
	aDel.appendChild(imgDel);
	aDel.setAttribute("href", "javascript:delPrimary('"+id+"')");
	td.appendChild(aDel);
}


function confirmDel(){
    if(confirm("确定要删除吗？删除将不能恢复！"))
   		 return true;
    else
   		 return false;
}


function delPrimary(id){
	if(confirmDel()){
		$.post('/comicdiy/comicapi', {
			'method'  : 'delPrimary',
			'primaryId' : id
		}, 
		//回调函数
		function (result) {
			if(result.trim() == 'false'){
				 alert("操作有误，请重试！");
		     }
		    //操作后刷新列表
			var num = $('#current').text();
			getPrimaryByPage(num);
		});
	}
}

function editPrimary(id,name,frame){
	centerPopup();
	loadPopup();
	$("#name").focus();
	$("#primary").val(id);
	$("#name").val(name);
	$("#frame").val(frame);
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
function checkNull(){
	 $('#prompt').show().html('');
	var name = 	$("#name").val();
	var val = $("#frame").val();
	var r = /^\+?[1-9][0-9]*$/;//正整数 
	
	if(name != null && name != "" &&  r.test(val)){
		return true;
	}else{
		$('#prompt').show().html('<font color="red" size="2">提示：</font>');
		if(name == null || name == "" ){
			$('#prompt').append('<font color="red" size="2">请输入名称</font><br>');
		}
		if(!r.test(val)){
			 $('#prompt').append('<font color="red" size="2">帧数需为正整数</font><br>');
		}
		return false;
	}
}

function updatePrimary(){
	if(checkNull()){
		var id = $("#primary").val();
		var name = $("#name").val();
		var frame= $("#frame").val();
		$.post('/comicdiy/comicapi', {
			'method'  : 'updatePrimary',
			'id' : id,
			'name' : name,
			'frame' : frame
		}, 
		//回调函数
		function (result) {
			if(result.trim() == 'false'){
				 alert("操作有误，请重试！");
		     }
		    //操作后刷新列表
			disablePopup();
			var num = $('#current').text();
			getPrimaryByPage(num);
		});
	}
}

function getEnding(id,name){
	$.post('/comicdiy/comicapi', {
		'method'  : 'getEndingCountByPrimary',
		'primary' : id
	}, 
	function (result) {
		$("#caption").html('<b>结局动画列表</b> (主动画名称：'+name+')'+
				'<input type="button" id="upload" value="上传结局" onclick="uploadYonkoma();">'+
				'<input type="button" id="reback" value="返回主动画" onclick="window.location.reload();">');
		//主动画的id
		$('#parent').attr("value",id);
		$('#primary').attr("value",name);
		$('#yonkomaList').children().remove();
		if(result > 0){
			var total = 0;
			if(parseInt(result%12) == 0){
				total = parseInt(result / 12);
			}else{
				 total = parseInt(result / 12)+1;
			}
			$('#total').html(total);
			$('#current').html(1);
			getEndingByPrimaryAndPage(1,id);
		
		}else{
			$('#total').html(0);
			$('#current').html(0);
		}
	});
}

function getEndingByPrimaryAndPage(pageNum,id){
	$.post('/comicdiy/comicapi', {
		'method'  : 'getEndingByPrimaryAndPage',
		'primary' : id,
		'pageSize' : 12,
		'pageNum' : pageNum
	}, 
	function (result) {
		$('#current').html(pageNum);
		generateEnding(result);
	},"json");
}

function generateEnding(result){
	$('#yonkomaList').children().remove();
	for(key in result){
		var id= result[key].id;
		var thumbnail = result[key].thumbnail;
		var name = result[key].name;
		var swf = result[key].swf;
		var num = Math.ceil((parseInt(key)+1)/4);
		generateTr(num);
		generateEndingTd(id,name,thumbnail,swf,num);
	}
}

//生成结局动画
function generateEndingTd(id,name,thumbnail,swf,num){
	var tr = document.getElementById("line"+num);
	var para = document.createElement("td");
	var a = document.createElement("a");
	para.appendChild(a);
	var img = document.createElement("img");
	var local = '/comicdiy/comicapi?method=getThumbnail&relativePath=';
	img.setAttribute("src",local+thumbnail);
	img.setAttribute("title", "结局动画名称："+name);
	a.appendChild(img);
	var path =  '/comicdiy/comicapi?method=getAssetFile&relativePath=';
	a.setAttribute("href", path+swf);
	a.setAttribute("target", "_blank");
	
	var aDel = document.createElement("a");
	var imgDel = document.createElement("img");
	imgDel.setAttribute("src", "imgs/del.png");
	imgDel.setAttribute("title", "删除");
	aDel.appendChild(imgDel);
	aDel.setAttribute("href", "javascript:delEnding('"+id+"')");
	para.appendChild(aDel);
	
	tr.appendChild(para);
}

function delEnding(id){
	if(confirmDel()){
		$.post('/comicdiy/comicapi', {
			'method'  : 'delEnding',
			'endingId' : id
		}, 
		//回调函数
		function (result) {
			if(result.trim() == 'false'){
				 alert("操作有误，请重试！");
		     }
		    //操作后刷新列表
			var num = $('#current').text();
			var parent = $("#parent").val();
			getEndingByPrimaryAndPage(num,parent);
		});
	}
}


function getPrevpage(){
	var num = $('#current').text();
	var pageNum = parseInt(num) - 1;
	//前一页，如果当时是第一页
	if(parseInt(num) <= 1){
		return;
	}else{
		getYonkoma(pageNum);
	}
}

function getNextpage(){
	var sum = $('#total').text();
	var num = $('#current').text();
	var pageNum = parseInt(num) + 1;
	//后一页，如果当前是最后一页
	if(sum == num){
		return;
	}else{
		getYonkoma(pageNum);
	}
}

function getYonkoma(pageNum){
	alert(pageNum);
	var parent = $("#parent").val();
	if(parent != null && parent != ""){
		getEndingByPrimaryAndPage(pageNum,parent);
	}else{
		getPrimaryByPage(pageNum);
	}
}