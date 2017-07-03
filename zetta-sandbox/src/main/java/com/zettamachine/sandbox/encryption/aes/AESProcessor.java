package com.zettamachine.sandbox.encryption.aes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.zettamachine.sandbox.encryption.util.FixKeyLength;

public class AESProcessor {
	private SecretKey key = null;
	private SecretKeySpec keySpec = null;
	
	private String initVector = "";
	
	private String path = "";
	private String clearFile = "";
	private String encryptedFile = "";
	private String decryptedFile = "";
	
	Logger log = Logger.getLogger("com.zettamachine.sandbox.encryption");
	
	
	
	public AESProcessor() {
	}

	public AESProcessor(String path, String clearFile, String initVector) {
		this.path = path;
		this.clearFile = path + clearFile;
		this.encryptedFile = path + "aes-enc-" + clearFile + ".aes";
		this.decryptedFile = path + "aes-dec-" + clearFile; 
		this.initVector = initVector;
	}
	
	public void genKey() throws NoSuchAlgorithmException {
		FixKeyLength.fixKeyLength();
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(256);
		key = keyGenerator.generateKey();
		log.debug("Generated key is: " + Base64.getEncoder().encodeToString(key.getEncoded()));
	}
	
	public void encrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
		File clearInFile = new File(clearFile);
		FileInputStream clearIn = new FileInputStream(clearInFile);
		FileOutputStream encryptedOut = new FileOutputStream(encryptedFile);
		
		IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
		
		if(keySpec == null) {
			keySpec = new SecretKeySpec(key.getEncoded(), "AES");
		}
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
		byte[] clearFileBytes = new byte[(int)clearInFile.length()];
		
		clearIn.read(clearFileBytes);
		
		byte[] encryptedFileBytes = cipher.doFinal(clearFileBytes);
		encryptedOut.write(encryptedFileBytes);
		
		clearIn.close();
		encryptedOut.close();
	}
	
	public void decrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
		File encryptedInFile = new File(encryptedFile);
		FileInputStream encryptedIn = new FileInputStream(encryptedInFile);
		FileOutputStream decryptedOut = new FileOutputStream(decryptedFile);
		
		IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
		
		if(keySpec == null) {
			keySpec = new SecretKeySpec(key.getEncoded(), "AES");
		}
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
		
		byte[] encryptedFileBytes = new byte[(int)encryptedInFile.length()];
		
		encryptedIn.read(encryptedFileBytes);
		
		byte[] decryptedFileBytes = cipher.doFinal(encryptedFileBytes);
		decryptedOut.write(decryptedFileBytes);
		
		encryptedIn.close();
		decryptedOut.close();
	}

	public SecretKey getKey() {
		return key;
	}

	public void setKeySpec(SecretKeySpec keySpec) {
		this.keySpec = keySpec;
	}
}
