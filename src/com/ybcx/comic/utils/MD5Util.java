package com.ybcx.comic.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	
    public final static String MD5(String s) {
    	try {
			MessageDigest md5 = MessageDigest.getInstance("md5");
			byte[] btyes = md5.digest(s.getBytes("utf-8"));
			int b = 0;
			StringBuffer buf = new StringBuffer();
			for (int offset = 0; offset < btyes.length; offset++) {
				b = btyes[offset];
				if (b < 0)
					b += 256;
				if (b < 16)
					buf.append("0");
				buf.append(Integer.toHexString(b));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    public static void main(String[] args) {
    	//ab56b4d92b40713acc5af89985d4b786
       // System.out.print(MD5Util.MD5("abcde"));
    }
}