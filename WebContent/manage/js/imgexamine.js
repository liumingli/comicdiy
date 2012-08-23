//存储全部的图片
var imageList = new Array();
//存储页面与元素下标的对应关系
var matchupArray= new Array();

//审查图片
function getAllImage(){
	console.log("getAllImage<<<");
	$.post("/comicdiy/comicapi",{
		'method' : 'getAllImage'
	},
	function (result) {
		$('#imgList').children().remove();
		if(result.length > 0){
			if(imageList.length>0){
				imageList = new Array();
			}
			for( key in result ){
				imageList.push(result[key]);
			}
			
			var total = parseInt(result.length / 12)+1;
			
			if(matchupArray.length > 0){
				matchupArray = new Array();
			}
			for(var i=1;i<=total;i++){
				matchupArray[i]=(i-1)*12;
			}
			
			$('#total').html(total);
			$('#current').html(1);
			
			getImgByPage(1);
		}
		
	},"json");
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
			'method'  : 'exanimeImage',
			'imgId' : imgId,
			"imgPath": path
		}, 
		//回调函数
		function (result) {
			imageList.slice(key, 1);
			if(result.trim() == 'false'){
				 alert("操作有误，请重试！");
		     }
		});
	     //操作后刷新列表
		getAllImage();
	}
}

function getImgByPage(pageNum){
	$('#imgList').children().remove();
	var begin=matchupArray[pageNum];
	var end = begin+12;
	var total=$('#total').text();
	//判断如果是最后一页或者总长度小于pageSize
	if(pageNum == total || imageList.length<12 ){
		end = imageList.length;
	}
	$('#current').html(pageNum);
	for(var i=begin;i<end;i++){
		generateImgTr(i);
		
		generateTd(parseInt(i)+1,i);
		
		generateImgTd(imageList[i].path,imageList[i].id,i);

		generateTd(imageList[i].uploadTime,i);
		
		generateImgOperate(imageList[i].path,imageList[i].id,i);
	}
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


	