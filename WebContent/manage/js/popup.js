
//初始化：是否开启DIV弹出窗口功能
//0 表示开启; 1 表示不开启;
var popupStatus = 0;

//使用Jquery加载弹窗 
function loadPopup(){   
	//仅在开启标志popupStatus为0的情况下加载  
	if(popupStatus==0){   
	$("#backgroundPopup").css({   
		"opacity": "0.1"  
	});   
	$("#backgroundPopup").fadeIn("slow");   
	$("#popupContact").fadeIn("slow");   
	popupStatus = 1;   
	} 
}  

//使用Jquery去除弹窗效果 
function disablePopup(){   
	//仅在开启标志popupStatus为1的情况下去除
	if(popupStatus==1){   
	$("#backgroundPopup").fadeOut();   
	$("#popupContact").fadeOut();   
	popupStatus = 0;   
	}   
}  

//将弹出窗口定位在屏幕的中央
function centerPopup(){   
	//获取系统变量
	var windowWidth = document.documentElement.clientWidth;   
	var windowHeight = document.documentElement.clientHeight;   
	var popupHeight = $("#popupContact").height();   
	var popupWidth = $("#popupContact").width();   
	//居中设置   
	$("#popupContact").css({   
	"position": "absolute",   
	"top": windowHeight/2-popupHeight/2,   
	"left": windowWidth/2-popupWidth/2   
	});   
	//以下代码仅在IE6下有效
	  
	$("#backgroundPopup").css({   
	"height": windowHeight   
	});   
}

//键盘按下ESC时关闭窗口!
document.onkeydown=function(e){   
	var theEvent = window.event || e; 
	var code = theEvent.keyCode || theEvent.which; 
	if (code == 27  && popupStatus==1) { 
		disablePopup();   
	}
};