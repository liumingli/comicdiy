function Holiday(){
	//这里可以放属性，并可以在下面的方法中处理
};

Holiday.prototype.convert = function(enHoliday){//container:Element
	
   var chHoliday=null;

   if(enHoliday == 'Neujahr'){
	   chHoliday = '元旦';
   }else  if(enHoliday == 'Spring'){
	   chHoliday = '春节';
   }else  if(enHoliday == 'Lantern'){
	   chHoliday = '元宵节';
   }else  if(enHoliday == 'Valentine'){
	   chHoliday = '情人节';
   }else  if(enHoliday == 'Women'){
	   chHoliday = '妇女节';
   }else  if(enHoliday == 'Tomb-sweeping'){
	   chHoliday = '清明节';
   }else  if(enHoliday == 'Labour'){
	   chHoliday = '劳动节';
   }else  if(enHoliday == 'Youth'){
	   chHoliday = '青年节';
   }else  if(enHoliday == 'Mother'){
	   chHoliday = '母亲节';
   }else  if(enHoliday == 'Father'){
	   chHoliday = '父亲节';
   }else  if(enHoliday == 'Doanngo'){
	   chHoliday = '端午节';
   }else  if(enHoliday == 'Double-seventh'){
	   chHoliday = '七夕节';
   }else  if(enHoliday == 'Teacher'){
	   chHoliday = '教师节';
   }else  if(enHoliday == 'Mid-autumn'){
	   chHoliday = '中秋节';
   }else  if(enHoliday == 'National'){
	   chHoliday = '国庆节';
   }else  if(enHoliday == 'Halloween'){
	   chHoliday = '万圣节';
   }else  if(enHoliday == 'Neujahr'){
	   chHoliday = '元旦';
   }else  if(enHoliday == 'Christmas-eve'){
	   chHoliday = '平安夜';
   }else  if(enHoliday == 'Christmas'){
	   chHoliday = '圣诞节';
   }else{
	   chHoliday = '其他';
   }
   
   return chHoliday;
	
};