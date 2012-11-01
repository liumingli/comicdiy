package com.ybcx.comic.utils;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IKAnalzyerUtil {
	
	public static String analyzerKeys(String key){
		
		StringBuffer result = new StringBuffer();
		
		//构建IK分词器，使用smart分词模式
		Analyzer analyzer = new IKAnalyzer(true);
		//获取Lucene的TokenStream对象
	    TokenStream ts = null;
		try {
			ts = analyzer.tokenStream("myfield", new StringReader(key));
			//获取词元位置属性
		 //   OffsetAttribute  offset = ts.addAttribute(OffsetAttribute.class); 
		    //获取词元文本属性
		    CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
		    //获取词元文本属性
		 //   TypeAttribute type = ts.addAttribute(TypeAttribute.class);
		    
		    
		    //重置TokenStream（重置StringReader）
			ts.reset(); 
			//迭代获取分词结果
			while (ts.incrementToken()) {
				if (result.length() > 0) {
					result.append(" ");
				}
				result.append(term.toString().trim());
			  //System.out.println(offset.startOffset() + " - " + offset.endOffset() + " : " + term.toString() + " | " + type.type());
			}
			//关闭TokenStream（关闭StringReader）
			ts.end();   // Perform end-of-stream operations, e.g. set the final offset.

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//释放TokenStream的所有资源
			if(ts != null){
		      try {
				ts.close();
		      } catch (IOException e) {
				e.printStackTrace();
		      }
			}
	    }
		return result.toString();
	}
	
	public static void main(String[] args) {
		String key="孙悟空吃饭";
		String result = analyzerKeys(key);
		System.out.println(result);
	}
}
