
//审查图片
function getAllFile(){
	$.post("/comicdiy/comicapi",{
		'method' : 'getMovieClipCount'
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
			
			getClipByPage(1);
		}else{
			$('#total').html(0);
			$('#current').html(0);
			$('#clipList').children().remove();
		}
		
	});
}


function getClipByPage(pageNum){
	$('#current').html(pageNum);
	$('#clipList').children().remove();
	$.post("/comicdiy/comicapi",{
		'method' : 'getMovieClipByPage',
		'pageNum' : pageNum
	},
	function (result) {
		if(result.length >0){
			for(key in result){
				generateTr(key);
				
				generateTd(parseInt(key)+1,key);
				
				generateTd(result[key].name,key);
				
				generateImgTd(result[key].thumbnail,result[key].swf,result[key].id,key);
				
				generateTd(result[key].type,key);
				
				generateUrlTd(result[key].url,key);
		
				generateTd(result[key].createTime,key);
				
				generateOperate(result[key].id,key);
			}
		}
	},"json");
}

function generateTr(key){
	$('<tr></tr>').appendTo($('#clipList'))
	.attr("id","line"+key);
}

function generateUrlTd(txt,key){
	//生成各td
	$('<td></td>').appendTo($('#line'+key))
	.html('<a href = "'+txt+'" target="_blank" >'+txt+'</a>');
}

//生成缩略图
function generateImgTd(path,swf,id,key){
	var tr = document.getElementById("line"+key);
	var para = document.createElement("td");
	var a = document.createElement("a");
	para.appendChild(a);
	var img = document.createElement("img");
	var local = '/comicdiy/comicapi?method=getAssetFile&relativePath=';
	img.setAttribute("src",local+path);
	img.setAttribute("height",30);
	a.appendChild(img);
	a.setAttribute("href",local+swf);
	a.setAttribute("target", "_blank");
	tr.appendChild(para);
}

function generateOperate(id,key){
	
	$('<td></td>').appendTo($('#line'+key))
	.append($('<a></a>')
		.append($('<img>').attr("src","imgs/del.png"))
		.attr("href","javascript:deleteMovieclip("+key+");"))
	.append($('<input type="hidden"></input>')
		.attr("id","clipId"+key)
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

function deleteMovieclip(key){
	if(confirmDel()){
		var clipId=$("#clipId"+key).attr("value");
		$.post('/comicdiy/comicapi', {
			'method'  : 'delMovieClip',
			'id' : clipId
		}, 
		//回调函数
		function (result) {
			if(result.trim() == 'false'){
				 alert("操作有误，请重试！");
		     }
		});
	    //操作后刷新列表
		var num = $('#current').text();
		getClipByPage(num);
	}
}

function getFirstpage(){
	var num = $('#current').text();
	//最后一页，如果当前是最后一页
	if(parseInt(num) == 1){
		return;
	}else{
		getClipByPage(1);
	}
}


function getPrevpage(){
	var num = $('#current').text();
	var pageNum = parseInt(num) - 1;
	//前一页，如果当时是第一页
	if(parseInt(num) == 1){
		return;
	}else{
		getClipByPage(pageNum);
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
		getClipByPage(pageNum);
	}

}

function getLastpage(){
	var sum = $('#total').text();
	var num = $('#current').text();
	//最后一页，如果当前是最后一页
	if( sum == num){
		return;
	}else{
		getClipByPage(sum);
	}
}


	