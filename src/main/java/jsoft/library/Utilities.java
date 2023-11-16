package jsoft.library;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.servlet.*;
import net.htmlparser.jericho.*;

public class Utilities {
	
	public static byte getByteParam(ServletRequest request, String name) {
		byte value = -1;
		
		String str_value = request.getParameter(name);
		if(str_value != null && !str_value.equalsIgnoreCase("")) {
			value = Byte.parseByte(str_value);
		}
		
		return value;
	}
	
	public static short getShortParam(ServletRequest request, String name) {
		short value = -1;
		
		String str_value = request.getParameter(name);
		if(str_value != null && !str_value.equalsIgnoreCase("")) {
			value = Short.parseShort(str_value);
		}
		
		return value;
	}
	
	public static int getIntParam(ServletRequest request, String name) {
		int value = -1;
		
		String str_value = request.getParameter(name);
		if(str_value != null && !str_value.equalsIgnoreCase("")) {
			value = Integer.parseInt(str_value);
		}
		
		return value;
	}
	
	
	 public static String getMd5(String input)
	    {
	        try {
	 
	            // Static getInstance method is called with hashing MD5
	            MessageDigest md = MessageDigest.getInstance("MD5");
	 
	            // digest() method is called to calculate message digest
	            // of an input digest() return array of byte
	            byte[] messageDigest = md.digest(input.getBytes());
	 
	            // Convert byte array into signum representation
	            BigInteger no = new BigInteger(1, messageDigest);
	 
	            // Convert message digest into hex value
	            String hashtext = no.toString(16);
	            while (hashtext.length() < 32) {
	                hashtext = "0" + hashtext;
	            }
	            return hashtext;
	        }
	 
	        // For specifying wrong message digest algorithms
	        catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException(e);
	        }
	    } 
	
	public static String encode(String str_unicode) {
		return CharacterReference.encode(str_unicode);
	}
	
	public static String decode(String str_html) {
		return CharacterReference.decode(str_html);
	}
}
