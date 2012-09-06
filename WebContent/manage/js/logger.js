function Logger(){
	//这里可以放属性，并可以在下面的方法中处理
};

Logger.prototype.trace = function(msg){
	if(typeof console != "undefined"){
		console.log(msg);
	}
};
