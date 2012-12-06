package com.ybcx.comic.tools;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

public class SpliceImage {

	     
	  public String spliceImage(String primaryLong,String endingLong)     
	  {    
		int position = primaryLong.lastIndexOf(".");
		//后缀 .jpg
		String extend = primaryLong.substring(position);
		//文件类型 jpg
		//String fileType = primaryLong.substring(position+1).toLowerCase();
		Random r = new Random();
		String num = String.valueOf(r.nextInt(100));
	    String destImg = primaryLong.substring(0,position)+"_weibo"+num+extend;	
	    
	    File  outFile  =  new  File(destImg);  
	    
	    try    
	    {    
	      //读取第一张图片    
	       File   fileOne   =   new   File(primaryLong);    
	       BufferedImage   ImageOne   =   ImageIO.read(fileOne);    
	       int   width   =   ImageOne.getWidth();//图片宽度    
	       int   height   =   ImageOne.getHeight();//图片高度    
	  
	       //从图片中读取RGB    
	       int[]   ImageArrayOne   =   new   int[width*height];    
	       ImageArrayOne   =   ImageOne.getRGB(0,0,width,height,ImageArrayOne,0,width);    
	  
	       //对第二张图片做相同的处理    
	       File   fileTwo   =   new   File(endingLong);    
	       BufferedImage   ImageTwo   =   ImageIO.read(fileTwo);      
	       int   width2   =   ImageTwo.getWidth();//图片宽度    
	       int   height2   =   ImageTwo.getHeight();//图片高度   
	       int[]   ImageArrayTwo   =   new   int[width2*height2];  
	       ImageArrayTwo   =   ImageTwo.getRGB(0,0,width2,height2,ImageArrayTwo,0,width2);    
	       
	       //新的宽度以小的为准
	       int newWidth=0;
	       if(width >width2){
	    	   newWidth = width2;
	       }else{
	    	   newWidth = width;
	       }
	  
	       //新高度累加
	       int newHeight = height+height2;
	       
	       //生成新图片 
	       BufferedImage   ImageNew   =   new   BufferedImage(newWidth,newHeight,BufferedImage.TYPE_INT_RGB);    
	       ImageNew.getGraphics().drawImage(
					ImageNew.getScaledInstance(newWidth, newHeight,
							Image.SCALE_SMOOTH), 0, 0, null);
	       
	       ImageNew.setRGB(0,0,newWidth,height,ImageArrayOne,0,width);//设置上半部分的RGB    
	       ImageNew.setRGB(0,height,newWidth,height2,ImageArrayTwo,0,width2);//设置下半部分的RGB   
	       
	       //保存画质
	       ImageWriter writer = null;

	       Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");

	        if( iter.hasNext() ){
	            writer = (ImageWriter)iter.next();
	        }
	        
	       ImageOutputStream ios = ImageIO.createImageOutputStream( outFile );

	        writer.setOutput(ios);

	        ImageWriteParam param = new JPEGImageWriteParam( java.util.Locale.getDefault() );

	        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT) ;

	        param.setCompressionQuality(0.90f);

	        writer.write(null, new IIOImage( ImageNew, null, null ), param);

	    //   ImageIO.write(ImageNew, fileType, outFile);//写图片    
	       
	     } catch(Exception   e)  {    
	           e.printStackTrace();    
	     }
	    
	     if(outFile.exists()){
	    	   return destImg;
	     }else{
	    	   return "";
	     }
	 }
}
