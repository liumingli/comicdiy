function initParentLabel(){
	$('#parentLabel').children().remove();
	//获取所有父级Label
	$.post('/comic/comicapi', {
		'method'  : 'getAllParentLabel'
	}, 
	//回调函数
	function (result) {
		var parent ="('parent')";
		$('#parentLabel').append('<a href="javascript:addLabel'+parent+'"><img src="imgs/add.png"></a>');
		if(result.length > 0){
			for( key in result ){
				var para = "('"+result[key].id+"','"+result[key].parent+"')";
				$('#parentLabel').append('<div><span onclick="initChildLabel'+para+'">'+result[key].name+'</span><a href="javascript:deleteLabel'+para+'"><img src="imgs/delete.png"></a></div>');
			}
	    }
	},"json");
}

function initChildLabel(parentId){
	$('#childLabel').children().remove();
	//根据父标签id获取所有子标签
	$.post('/comic/comicapi', {
		'method'  : 'getLabelByParent',
		'parentId' : parentId
	}, 
	//回调函数
	function (result) {
		var parent ="('"+parentId+"')";
		$('#childLabel').append('<a href="javascript:addLabel'+parent+'"><img src="imgs/add.png"></a>');
		if(result.length > 0){
			for( key in result ){
				var para = "('"+result[key].id+"','"+result[key].parent+"')";
				$('#childLabel').append('<div>'+result[key].name+'<a href="javascript:deleteLabel'+para+'"><img src="imgs/delete.png"></a></div>');
			}
	    }
	},"json");
}


function addLabel(parent){
	$('#parent').val(parent);
	$('#name').val("");
	centerPopup();
	loadPopup();
}

function createLabel(){
	var parent = $('#parent').val();
	var name = $('#name').val();
	disablePopup();
	
	if(name != null && name !=""){
		$.post('/comic/comicapi', {
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
		$.post('/comic/comicapi', {
			'method'  : 'deleteLabel',
			'labelId' : labelId
		}, 
		//回调函数
		function (result) {
			if(result == "false"){
				 alert("操作有误，请重试！");
		    }
			
			//删除操作后刷新本级列表
			if(parent == 'parent'){
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
	$.post('/comic/comicapi', {
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