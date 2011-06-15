package com.mediacollector.tools;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class Crypter {
	
	private final static String HEX = "0123456789ABCDEF";
	
	public static String encrypt(final String cryptString) throws Exception {
		return encrypt(cryptString, HEX);
	}
	
	public static String decrypt(final String cryptString) throws Exception {
		return decrypt(cryptString, HEX);
	}
	
	public static String encrypt(final String cryptString, 
								 final String seed) throws Exception {	
		byte[] rawKey = getRawKey(seed);
		byte[] result = encrypt(rawKey, cryptString.getBytes());
        return toHex(result);
	}
	
	public static String decrypt(final String cryptString,
								 final String seed) throws Exception {
		byte[] rawKey = getRawKey(seed);
		byte[] enc = toByte(cryptString);
		byte[] result = decrypt(rawKey, enc);
        return new String(result);
	}
	
	private static byte[] getRawKey(final String seed) throws Exception {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		SecureRandom secRandom = SecureRandom.getInstance("SHA1PRNG");		
		secRandom.setSeed(seed.getBytes());
		keygen.init(128, secRandom);
		return keygen.generateKey().getEncoded();
	}
	
	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
	}
	
	private static byte[] decrypt(byte[] raw, 
			          	  	 	  byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
	
	public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }
	
	public static String fromHex(String hex) {
        return new String(toByte(hex));
    }
	
	public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
        	result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 
        			16).byteValue();
        return result;
    }
	
	public static String toHex(byte[] buf) {
        if (buf == null) return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) appendHex(result, buf[i]);
        return result.toString();
    }
	
	private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }

}
