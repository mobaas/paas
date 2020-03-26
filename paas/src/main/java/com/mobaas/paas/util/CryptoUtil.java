/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/*
 * 加密辅助类
 */
public class CryptoUtil {
	
	/*
	 * MD5加密
	 */
	public static String MD5(String s) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] bytes = md.digest(s.getBytes("utf-8"));
	        return toHex(bytes);
	    }
	    catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	private static String toHex(byte[] bytes) {

	    final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}
 
      
    /** 
     * DES加密 
     * @param data:需要加密的数据 
     * @return 
     */  
    public static String encryptDES(String key, String data) {  
        String s = null;  
        if ( data != null ) {  
        	try {
	            //DES算法要求有一个可信任的随机数源  
	            SecureRandom sr = new SecureRandom();  
	            //从原始密钥数据创建DESKeySpec对象  
	            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());  
	            //创建一个密钥工厂，用它将DESKeySpec转化成SecretKey对象  
	            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
	            SecretKey securekey = keyFactory.generateSecret(desKeySpec);  
	            //Cipher对象实际完成加密操作  
	            Cipher cipher = Cipher.getInstance("DES");  
	            //用密钥初始化Cipher对象  
	            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);  
	            //将加密后的数据编码成字符串  
	            s = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes())); 
        	} catch (Exception ex) {
        		throw new RuntimeException(ex);
        	}
        }  
        return s;  
    }  
      
    /** 
     * DES解密 
     * @param data:需要解密的数据 
     * @return 
     * @throws Exception 
     */  
    public static String decryptDES(String key, String data)  {  
        String s = null;  
        if ( data != null ) {  
        	try {
	            //DES算法要求有一个可信任的随机数源  
	            SecureRandom sr = new SecureRandom();  
	            //从原始密钥数据创建DESKeySpec对象  
	            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());  
	            //创建一个密钥工厂，用它将DESKeySpec转化成SecretKey对象  
	            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
	            SecretKey securekey = keyFactory.generateSecret(desKeySpec);  
	            //Cipher对象实际完成解密操作  
	            Cipher cipher = Cipher.getInstance("DES");  
	            //用密钥初始化Cipher对象  
	            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);  
	            //将加密后的数据解码再解密   
	            byte[] buf = cipher.doFinal(Base64.getDecoder().decode(data));  
	            s = new String(buf);  
        	} catch (Exception ex) {
        		throw new RuntimeException(ex);
        	}
        }  
        return s;  
    }  
      
}
