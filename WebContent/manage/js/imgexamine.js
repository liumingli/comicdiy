
//审查图片
function getAllImage(){
	var logger = new Logger();
	logger.trace("getImageCount<<<");
	$.post("/comicdiy/comicapi",{
		'method' : 'getImageCount'
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
			
			getImgByPage(1);
		}else{
			$('#total').html(0);
			$('#current').html(0);
			$('#imgList').children().remove();
		}
		
	});
}

function generateImgTr(key){
	$('<tr></tr>').appendTo($('#imgList'))
	.attr("id","line"+key);
}



//生成缩略图
function generateImgTd(path,id,key){
	var tr = document.getElementById("line"+key);
	var para = document.createElement("td");
	var a = document.createElement("a");
	para.appendChild(a);
	var img = document.createElement("img");
	var local = '/comicdiy/comicapi?method=getThumbnail&relativePath=';
	img.setAttribute("src",local+path);
	img.setAttribute("height",30);
	a.appendChild(img);
	a.setAttribute("href",local+path);
	a.setAttribute("target", "_blank");
	tr.appendChild(para);
}

function generateImgOperate(path,id,key){
	
	$('<td></td>').appendTo($('#line'+key))
	.append($('<a></a>')
		.append($('<img>').attr("src","imgs/del.png"))
		.attr("href","javascript:deleteImgById('"+path+"',"+key+");"))
	.append($('<input type="hidden"></input>')
		.attr("id","imgId"+key)
		.attr("value",id));
}

//生成各td
function generateTd(txt,key){
	$('<td></td>').appendTo($('#line'+key))
	.text(txt);
}

function confirmDel(){
    if(confirm("确定要删除吗？删除将不能恢复！"))
   		 return true;
    else
   		 return false;
}

function deleteImgById(path,key){
	if(confirmDel()){
		var imgId=$("#imgId"+key).attr("value");
		$.post('/comicdiy/comicapi', {
			'method'  : 'examineImage',
			'imgId' : imgId,
			"imgPath": path
		}, 
		//回调函数
		function (result) {
			if(result.trim() == 'false'){
				 alert("操作有误，请重试！");
		     }
		});
	    //操作后刷新列表
		var num = $('#current').text();
		getImgByPage(num);
	}
}

function getImgByPage(pageNum){
	$('#current').html(pageNum);
	$('#imgList').children().remove();
	$.post("/comicdiy/comicapi",{
		'method' : 'getImageByPage',
		'pageNum' : pageNum
	},
	function (result) {
		if(result.length >0){
			for(key in result){
				generateImgTr(key);
				
				generateTd(parseInt(key)+1,key);
				
				generateImgTd(result[key].path,result[key].id,key);
		
				generateTd(result[key].uploadTime,key);
				
				generateImgOperate(result[key].path,result[key].id,key);
			}
		}
	},"json");
}


function getFirstpageImg(){
	var num = $('#current').text();
	//最后一页，如果当前是最后一页
	if(parseInt(num) == 1){
		return;
	}else{
		getImgByPage(1);
	}
}


function getPrevpageImg(){
	var num = $('#current').text();
	var pageNum = parseInt(num) - 1;
	//前一页，如果当时是第一页
	if(parseInt(num) == 1){
		return;
	}else{
		getImgByPage(pageNum);
	}
}


function getNextpageImg(){
	var sum = $('#total').text();
	var num = $('#current').text();
	var pageNum = parseInt(num) + 1;
	//后一页，如果当前是最后一页
	if(sum == num){
		return;
	}else{
		getImgByPage(pageNum);
	}

}

function getLastpageImg(){
	var sum = $('#total').text();
	var num = $('#current').text();
	//最后一页，如果当前是最后一页
	if( sum == num){
		return;
	}else{
		getImgByPage(sum);
	}
}


	