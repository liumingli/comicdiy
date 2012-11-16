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
		var num = Math.ceil((parseInt(key)+1)/4);
		generateTr(num);
		generatePrimaryTd(id,name,thumbnail,num);
	}
}

function generateTr(num){
	$('<tr></tr>').appendTo($('#yonkomaList'))
	.attr("id","line"+num);
}


//生成主动画
function generatePrimaryTd(id,name,thumbnail,num){
	var tr = document.getElementById("line"+num);
	var para = document.createElement("td");
	var a = document.createElement("a");
	para.appendChild(a);
	var img = document.createElement("img");
	var local = '/comicdiy/comicapi?method=getThumbnail&relativePath=';
	img.setAttribute("src",local+thumbnail);
	img.setAttribute("title", "主动画名称："+name);
	a.appendChild(img);
	//var swf =  '/comicdiy/comicapi?method=getAssetFile&relativePath=';
	//	a.setAttribute("href",swf+path);
	//a.setAttribute("target", "_blank");
	a.setAttribute("href","javascript:getEnding('"+id+"','"+name+"')");
	tr.appendChild(para);
}

function getEnding(id,name){
	$.post('/comicdiy/comicapi', {
		'method'  : 'getEndingCountByPrimary',
		'primary' : id
	}, 
	function (result) {
		if(result > 0){
			var total = 0;
			if(parseInt(result%2) == 0){
				total = parseInt(result / 12);
			}else{
				 total = parseInt(result / 12)+1;
			}
			$('#total').html(total);
			$('#current').html(1);
			$("#caption").html("<b>结局动画列表</b> (主动画名称："+name+")");
			$('#parent').attr("value",id);
			getEndingByPrimaryAndPage(1,id);
		
		}else{
			$('#total').html(0);
			$('#current').html(0);
			$('#primaryList').children().remove();
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
	$("#reback").removeAttr("style");
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
	tr.appendChild(para);
}



function getPrevpage(){
	var num = $('#current').text();
	var pageNum = parseInt(num) - 1;
	//前一页，如果当时是第一页
	if(parseInt(num) == 1){
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
	var parent = $("#parent").val();
	if(parent != null && parent != ""){
		console.log(parent);
		getEndingByPrimaryAndPage(pageNum,parent);
	}else{
		getPrimaryByPage(pageNum);
	}
}