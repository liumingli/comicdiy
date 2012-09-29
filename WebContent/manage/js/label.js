function initParentLabel(){
	$('#parentAdd').children().remove();
	$('#parentAdd').text("父标签");
	$('#parentLabel').children().remove();
	//获取所有父级Label
	$.post('/comicdiy/comicapi', {
		'method'  : 'getAllParentLabel'
	}, 
	//回调函数
	function (result) {
		var parent ="('parent')";
		$('#parentAdd').append('<a href="javascript:addLabel'+parent+'"><img src="imgs/add.png"></a>');
		if(result.length > 0){
			for( key in result ){
				var num = parseInt(key);
				var para = "('"+result[key].id+"','"+result[key].parent+"')";
				if(num%2){
					$('#parentLabel').append('<div class="odd"><span  id="'+result[key].id+'" onclick="initChildLabel'+para+'" >'+result[key].name+'</span><a href="javascript:deleteLabel'+para+'"><img src="imgs/delete.png"></a></div>');
				}else{
					$('#parentLabel').append('<div class="ldd"><span id="'+result[key].id+'" onclick="initChildLabel'+para+'">'+result[key].name+'</span><a href="javascript:deleteLabel'+para+'"><img src="imgs/delete.png"></a></div>');
				}
			}
	    }
	},"json");
}

var staticParend="";

function initChildLabel(parentId){
	//给父标签当前选中的是哪个添加样式
	if(staticParend != parentId && staticParend != ""){
		$('#'+staticParend).removeClass("extendCss");
	}
	$('#'+parentId).addClass("extendCss");
	staticParend=parentId;
	
	$('#childAdd').children().remove();
	$('#childAdd').text("子标签");
	$('#childLabel').children().remove();
	$('#childAdd').append('<div id="load"><img src="imgs/loading.gif"></div>');
	//根据父标签id获取所有子标签
	$.post('/comicdiy/comicapi', {
		'method'  : 'getLabelByParent',
		'parentId' : parentId
	}, 
	//回调函数
	function (result) {
		$('#load').remove();
		var parent ="('"+parentId+"')";
		$('#childAdd').append('<a href="javascript:addLabel'+parent+'"><img src="imgs/add.png"></a>');
		
		if(result.length > 0){
			for( key in result ){
				var num = parseInt(key);
				var para = "('"+result[key].id+"','"+result[key].parent+"')";
				if(num%2){
					$('#childLabel').append('<div class="ldd">'+result[key].name+'<a href="javascript:deleteLabel'+para+'"><img src="imgs/delete.png"></a></div>');
				}else{
					$('#childLabel').append('<div class="odd">'+result[key].name+'<a href="javascript:deleteLabel'+para+'"><img src="imgs/delete.png"></a></div>');
				}
			}
	    }
	},"json");
}


function addLabel(parent){
	centerPopup();
	loadPopup();
	$('#prompt').hide();
	$('#parent').val(parent);
	$('#name').val("");
	$("#name").focus();
}

function getByteLen(val) {    //传入一个字符串
    var len = 0;
    for (var i = 0; i < val.length; i++) {
        if (val[i].match(/[^\x00-\xff]/ig) != null){ //全角 
            len += 2; //如果是全角，占用两个字节
    	}else{
            len += 1; //半角占用一个字节
    	}
    }
    return len;
 } 

function createLabel(){
	var parent = $('#parent').val();
	var name = $('#name').val();
	
	if(name != null && name !="" && getByteLen(name) <9){
		disablePopup();
		$.post('/comicdiy/comicapi', {
			'method'  : 'createLabel',
			'name' : name ,
			'parent' : parent
			
		}, 
		//回调函数
		function (result) {
			if(result == "false"){
				 alert("操作有误，请重试！");
		    }
			
			//操作后刷新本级列表
			if(parent == "parent"){
				initParentLabel();
			}else{
				initChildLabel(parent);
			}
		});
	}
}

function deleteLabel(labelId,parent){
	if(confirmDel()){
		$.post('/comicdiy/comicapi', {
			'method'  : 'deleteLabel',
			'labelId' : labelId
		}, 
		//回调函数
		function (result) {
			if(result == "false"){
				 alert("操作有误，请重试！");
		    }
			
			//删除操作后刷新本级列表
			if(parent == "parent"){
				operateParent(labelId);
			}else{
				initChildLabel(parent);
			}
		});
	}
}

function confirmDel(){
    if(confirm("确定要删除吗？删除将不能恢复！"))
   		 return true;
    else
   		 return false;
}

function operateParent(parentId){
	//刷新列表
	initParentLabel();
	
	$('#childLabel').children().remove();
	
	//删除该父标签的下挂子标签
	$.post('/comicdiy/comicapi', {
		'method'  : 'deleteLabelByParent',
		'parentId' : parentId
	}, 
	//回调函数
	function (result) {
		if(result == "false"){
			 alert("操作有误，请重试！");
	    }
	});
}