function Logger(){};

Logger.prototype.debug = function(obj){
	console.debug(obj);
};
Logger.prototype.info = function(obj){
	console.info(obj);
};
Logger.prototype.warn = function(obj){
	console.warn(obj);
};
Logger.prototype.error = function(obj){
	console.error(obj);
};
