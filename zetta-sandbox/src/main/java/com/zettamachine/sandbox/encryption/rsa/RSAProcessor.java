package com.zettamachine.sandbox.encryption.rsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;

import com.zettamachine.sandbox.encryption.util.FixKeyLength;

public class RSAProcessor {
	private KeyPair keyPair = null;
	Logger log = Logger.getLogger("com.zettamachine.sandbox.encryption");
	
	private String path = "";
	private String clearFile = "";
	private String encryptedFile = "";
	private String decryptedFile = "";
	
	public RSAProcessor(String path, String clearFile) {
		this.path = path;
		this.clearFile = path + clearFile;
		this.encryptedFile = path + "rsa-enc-" + clearFile + ".rsa";
		this.decryptedFile = path + "rsa-dec-" + clearFile;
	}
	
	public void genKey() throws NoSuchAlgorithmException {
		FixKeyLength.fixKeyLength();
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		keyPair = keyPairGenerator.generateKeyPair();
		log.debug("Generated public key is: " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
		log.debug("Generated private key is: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
	}
	

	public void encrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
		File clearInFile = new File(clearFile);
		FileInputStream clearIn = new FileInputStream(clearInFile);
		FileOutputStream encryptedOut = new FileOutputStream(encryptedFile);
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
		byte[] clearFileBytes = new byte[(int)clearInFile.length()];
		
		clearIn.read(clearFileBytes);
		
		int chunkSize = 117;
		log.debug("Clear file size :" + clearFileBytes.length);
		int enc = (int)(Math.ceil(clearFileBytes.length/117.0) * 128);
		log.debug("enc :" + enc);
		int i = 0;
		ByteBuffer buffer = ByteBuffer.allocate(enc);
		
		while (i < clearFileBytes.length) {
			int length = Math.min(clearFileBytes.length - i, chunkSize);
			if (length < chunkSize) log.debug("Now the length is  " + length + " and i is " + i);
			byte[] encChunk = cipher.doFinal(clearFileBytes, i , length);
			buffer.put(encChunk);
			i += length;
		}
		
		encryptedOut.write(buffer.array());
		clearIn.close();
		encryptedOut.close();
	}
	
	public void decrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
		File encryptedInFile = new File(encryptedFile);
		FileInputStream encryptedIn = new FileInputStream(encryptedInFile);
		FileOutputStream decryptedOut = new FileOutputStream(decryptedFile);
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
		byte[] encryptedFileBytes = new byte[(int)encryptedInFile.length()];
		
		encryptedIn.read(encryptedFileBytes);

		int chunkSize = 128;
		int i = 0;
		ByteBuffer buffer = ByteBuffer.allocate(encryptedFileBytes.length);
		while (i < encryptedFileBytes.length) {
			int length = Math.min(encryptedFileBytes.length - i, chunkSize);
			byte[] encChunk = cipher.doFinal(encryptedFileBytes, i , length);
			buffer.put(encChunk);
			i += length;
		}
		
		decryptedOut.write(buffer.array());
		
		encryptedIn.close();
		decryptedOut.close();
	}
}
