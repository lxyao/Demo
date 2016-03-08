package com.yao.testdemo.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	public static String getMD5(String content){
		
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(content.getBytes());
			return getHashString(digest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private static String getHashString(MessageDigest digest){
		
		StringBuilder builder = new StringBuilder();
		for(byte b:digest.digest()){
			builder.append(Integer.toHexString((b>>4)&0xf));
			builder.append(Integer.toHexString(b&0xf));
			
		}
		
		return builder.toString();
	}

}
