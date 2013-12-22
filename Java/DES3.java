package com.twlkyao;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/** 
 * 3DES tools.
 * @author liufeng  
 * @date 2012.10.11
 * @editor Shiyao Qi
 * @date 2013.12.22
 * @email qishiyao2008@126.com 
 */ 
public class DES3 {
	
	/**
	 * 3DES encryption
	 * @param secretKey Secret key.
	 * @param iv The buffer with the IV, to protect against subsequent modification.
	 * @param plainText Plain text.
	 * @param charset The type of charset.
	 * @return Encrypted text in Base64 charset.
	 * @throws Exception
	 */
	public static String Encryption(String secretKey, String iv, String plainText, String charset) throws Exception {
		
		Key deskey = null;
		
		// Creates a DESedeKeySpec object using the first 24 bytes in key as the key material for the DES-EDE key.
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes()); 
		
		// Returns a SecretKeyFactory object that converts secret keys of the specified algorithm.
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = (Key) keyfactory.generateSecret(spec);
		
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		
		// Initializes this cipher with a key and Encrypt algorithm parameters.
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips); 
		
		// Encrypts data in a single-part operation, or finishes a multiple-part operation.
		/**
		 *  Encodes this String into a sequence of bytes using the named charset,
		 *  storing the result into a new byte array.
		 */
		byte[] encryptData = cipher.doFinal(plainText.getBytes(charset));
		
		return Base64.encode(encryptData); // Convert the encrypted string to Base64.
	}
	
	/**
	 * 3DES decryption.
	 * @param secretKey Secret key.
	 * @param iv The buffer with the IV, to protect against subsequent modification.
	 * @param encryptText Encrypt text.
	 * @param charset The type of charset.
	 * @return Plain text in specified charset.
	 * @throws Exception
	 */
	public static String Decryption(String secretKey, String iv, String encryptText, String charset) throws Exception {
		Key deskey = null;
		
		// Creates a DESedeKeySpec object using the first 24 bytes in key as the key material for the DES-EDE key.
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		
		// Returns a SecretKeyFactory object that converts secret keys of the specified algorithm.
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = (Key) keyfactory.generateSecret(spec);
		
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		
		// Initializes this cipher with a key and Decrypt algorithm parameters.
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		
		// Decrypts data in a single-part operation, or finishes a multiple-part operation.
		/**
		 *  Encodes this String into a sequence of bytes,
		 *  storing the result into a new byte array.
		 */
		byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
		
		// Constructs a new String by decoding the specified array of bytes using the specified charset.
		return new String(decryptData, charset); 
	}
}