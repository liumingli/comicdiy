//存储搜索的动画结果
var animsList = new Array();
//存储页面与元素下标的对应关系
var matchupArray= new Array();

function searchAnim(){
	var keys = $('#keys').val();
	if(keys != "" && keys != null){
		$.post('/comicdiy/comicapi', {
			'method'  : 'searchAnim',
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
			
				
				getSearchAnimByPage(1);
			
			}else{
				$('#total').html(0);
				$('#current').html(0);
				$('#animList').children().remove();
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

function getSearchAnimByPage(pageNum){
	$('#animList').children().remove();
	$('#current').html(pageNum);
	
	$.post("/comicdiy/comicapi",{
		'method' : 'getSearchAnimByPage',
		'keys' : keys,
		'pageNum' : pageNum
	},
	function (result) {
		if(result.length > 0){
			for(key in result){
				generateAnimTr(key);
				
				generateTd(parseInt(key)+1,key);
				
				generateTd(result[key].name,key);
				
				generateAnimTd(result[key].thumbnail,result[key].id,result[key].owner,key);
				
				generateTd(result[key].createTime,key);
				
				generateAnimOperate(result[key].id,key);
			}
		}
	},"json");
}
		
//审查动画
function getAllAnimation(){
	$.post("/comicdiy/comicapi",{
		'method' : 'getAnimCount'
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
			
			getAnimByPage(1);
		}else{
			$('#total').html(0);
			$('#current').html(0);
			$('#animList').children().remove();
		}
		
	});
}
	
function generateAnimTr(key){
	$('<tr></tr>').appendTo($('#animList'))
	.attr("id","line"+key);
}

//生成各td
function generateTd(txt,key){
	$('<td></td>').appendTo($('#line'+key))
	.text(txt);
}
	
//生成缩略图
function generateAnimTd(thumbnail,id,user,key){
	var tr = document.getElementById("line"+key);
	var para = document.createElement("td");
	var a = document.createElement("a");
	para.appendChild(a);
	var img = document.createElement("img");
	var local = '/comicdiy/comicapi?method=getThumbnail&relativePath=';
	img.setAttribute("src",local+thumbnail);
	img.setAttribute("height",30);
	a.appendChild(img);
//	a.setAttribute("onclick","javascript:redirect('"+user+"','"+id+"')");
//	a.setAttribute("href", "javascript:redirect('"+user+"','"+id+"')");
	var url =  "../animclient/Aplayer_simple.html?userId="+user+"&animId="+id;
	a.setAttribute("href", url);
	a.setAttribute("target", "_blank");
	tr.appendChild(para);
}
	
//生成删除选项
function generateAnimOperate(id,key){
	
	$('<td></td>').appendTo($('#line'+key))
	.append($('<a></a>')
		.append($('<img>').attr("src","imgs/del.png"))
		.attr("href","javascript:deleteAnimById("+key+");"))
	.append($('<input type="hidden"></input>')
		.attr("id","animId"+key)
		.attr("value",id));
}
	
function redirect(user,idVal){
	//http://localhost:8080/animclient/Aplayer_simple.html?userId=lwz7512&animId=897744a04d5e4f43
	var url =  "Aplayer_simple.html?userId="+user+"&animId="+idVal;
	window.open(url);
}

function confirmDel(){
    if(confirm("确定要删除吗？删除将不能恢复！"))
   		 return true;
    else
   		 return false;
}
	
function deleteAnimById(key){
	if(confirmDel()){
		var animId=$("#animId"+key).attr("value");
		$.post('/comicdiy/comicapi', {
			'method'  : 'examineAnim',
			'animId' : animId
		}, 
		//回调函数
		function (result) {
			if(result.trim() == 'false'){
				 alert("操作有误，请重试！");
		     }
		     //操作后刷新列表
			var num = $('#current').text();
			getAnimByPage(num);
		});
	}
}

function getAnimByPage(pageNum){
	$('#animList').children().remove();
	$('#current').html(pageNum);
	
	$.post("/comicdiy/comicapi",{
		'method' : 'getAnimByPage',
		'pageNum' : pageNum
	},
	function (result) {
		if(result.length > 0){
			for(key in result){
				generateAnimTr(key);
				
				generateTd(parseInt(key)+1,key);
				
				generateTd(result[key].name,key);
				
				generateAnimTd(result[key].thumbnail,result[key].id,result[key].owner,key);
				
				generateTd(result[key].createTime,key);
				
				generateAnimOperate(result[key].id,key);
			}
		}
	},"json");
}


function getFirstpageAnim(){
	var num = $('#current').text();
	//最后一页，如果当前是最后一页
	if(parseInt(num) == 1){
		return;
	}else{
		getAnimByPage(1);
	}
}


function getPrevpageAnim(){
	var num = $('#current').text();
	var pageNum = parseInt(num) - 1;
	//前一页，如果当时是第一页
	if(parseInt(num) == 1){
		return;
	}else{
		getAnimByPage(pageNum);
	}
}


function getNextpageAnim(){
	var sum = $('#total').text();
	var num = $('#current').text();
	var pageNum = parseInt(num) + 1;
	//后一页，如果当前是最后一页
	if(sum == num){
		return;
	}else{
		getAnimByPage(pageNum);
	}

}

function getLastpageAnim(){
	var sum = $('#total').text();
	var num = $('#current').text();
	//最后一页，如果当前是最后一页
	if( sum == num){
		return;
	}else{
		getAnimByPage(sum);
	}
}

function getFirstPage(){
	var num = $('#current').text();
	//最后一页，如果当前是最后一页
	if(parseInt(num) == 1){
		return;
	}else{
		getSearchAnimByPage(1);
	}
}


function getPrevPage(){
	var num = $('#current').text();
	var pageNum = parseInt(num) - 1;
	//前一页，如果当时是第一页
	if(parseInt(num) == 1){
		return;
	}else{
		getSearchAnimByPage(pageNum);
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
		getSearchAnimByPage(pageNum);
	}

}

function getLastPage(){
	var sum = $('#total').text();
	var num = $('#current').text();
	//最后一页，如果当前是最后一页
	if( sum == num){
		return;
	}else{
		getSearchAnimByPage(sum);
	}
}



	