package com.twlkyao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/** 
 * Base64 encode tools.
 * @author liufeng
 * @date 2012-10-11
 * @editor Shiyao Qi
 * @email qishiyao2008@126.com 
 */ 
public class Base64 {
	
	private static final String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"; // The base string.
	private static final char[] legalChars = base.toCharArray(); // Convert the string to char array.

	/**
	 * Encode string to Base64.
	 * @param data The string to be encoded.
	 * @return Encoded Base64 string.
	 */
	public static String encode(byte[] data) {
		int start = 0;
		int len = data.length; // Get the length of the data.
		StringBuffer buf = new StringBuffer(data.length * 3 / 2);
		
		int end = len - 3; // Spare the last 2 bytes.
		int i = start;
		int n = 0;
		
		// Convert every 3 bytes into 4 Base64s.
		while (i <= end) {
			// Merge 3 bytes into a integer.
			int d = ((((int) data[i]) & 0x0ff) << 16) // Fist byte.
					| ((((int) data[i + 1]) & 0x0ff) << 8) // Second byte.
					| (((int) data[i + 2]) & 0x0ff); // Last byte.
			
			// Convert the integer into Base64.
			buf.append(legalChars[(d >> 18) & 63]); // First Base64.
			buf.append(legalChars[(d >> 12) & 63]); // Second Base64.
			buf.append(legalChars[(d >> 6) & 63]); // Third Base 64.
			buf.append(legalChars[d & 63]); // Fourth Base64.
			
			i += 3; // Jump to next three bytes.
			
			if (n++ >= 14) {
				n = 0;
				buf.append(" ");
			}
		}
		/**
		 *如果没有剩下任何数据，就什么都不要加，这样才可以保证资料还原的正确性。
		 */
		// There are 2 bytes left.
		if (i == (start + len - 2)) { // len%3 == 2
			int d = ((((int) data[i]) & 0x0ff) << 16)
					| ((((int) data[i + 1]) & 0x0ff) << 8);
			
			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append("="); // 2 bytes left add 1 "=".
		}
		// There are 1 byte left.
		else if (i == (start + len - 1)) { // len%3 == 1
			int d = (((int) data[i]) & 0x0ff) << 16;
			
			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append("=="); // 1 byte left add 2 "=".
		}
		return buf.toString(); // Convert the StringBuffer to string.
	}
	
	/**
	 * Decode the Base64.
	 * @param c The char to decode.
	 * @return The index of the char in Base64.
	 */
	private static int decode(char c) {
		if (c >= 'A' && c <= 'Z') // Upper char.
			return ((int) c) - 65;
		else if (c >= 'a' && c <= 'z') // Lower char.
			return ((int) c) - 97 + 26;
		else if (c >= '0' && c <= '9') // Digits
			return ((int) c) - 48 + 26 + 26;
		else	
			switch (c) {
				case '+':
					return 62;
				case '/':
					return 63;
				case '=':
					return 0;
				default:  
					throw new RuntimeException("Unexpected Base64 code: " + c);
			}
	}
	
	/**
	 * Decode the given Base64 encoded String to a new byte array.
	 * The byte array holding the decoded data is returned.
	 * @param s
	 * @return
	 */
	public static byte[] decode(String string) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			decode(string, bos);
		} catch (IOException e) {
			throw new RuntimeException();
		}  
		byte[] decodedBytes = bos.toByteArray();
		try {
			bos.close();
			bos = null;
		} catch (IOException ex) {  
			System.err.println("Error while decoding BASE64: " + ex.toString());
	}
		return decodedBytes;
	}
	
	/**
	 * 
	 * @param string 
	 * @param os
	 * @throws IOException
	 */
	private static void decode(String string, OutputStream os) throws IOException {
		
		int i = 0;
		int len = string.length();
		
		while (true) {
			while (i < len && string.charAt(i) <= ' ') { // Go through the string.
				i++;
			}
			
			if (i == len) { // Reaches the end.
				break;
			}
			int tri = (decode(string.charAt(i)) << 18)
					+ (decode(string.charAt(i + 1)) << 12)
					+ (decode(string.charAt(i + 2)) << 6)
					+ (decode(string.charAt(i + 3)));
	
			os.write((tri >> 16) & 255);
			if (string.charAt(i + 2) == '=') {
				break;
			}
			os.write((tri >> 8) & 255);
			if (string.charAt(i + 3) == '=') {
				break;
			}
			os.write(tri & 255);
			i += 4;
		}
	}
}
