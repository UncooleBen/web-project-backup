package com.j2ee.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

//import sun.misc.BASE64Encoder;



public class MD5Util {

	public static String EncoderPwdByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] encrypted1 =Base64.decodeBase64(str);
		return Base64.encodeBase64String(encrypted1);
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println(MD5Util.EncoderPwdByMD5("123456"));
	}
}
