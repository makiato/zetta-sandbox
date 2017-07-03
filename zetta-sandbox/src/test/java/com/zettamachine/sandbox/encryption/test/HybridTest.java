package com.zettamachine.sandbox.encryption.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.zettamachine.sandbox.encryption.aes.AESProcessor;
import com.zettamachine.sandbox.encryption.rsa.RSAProcessor;
import com.zettamachine.sandbox.encryption.zip.ZipProcessor;

public class HybridTest {

	public static void main(String[] args) {
		Logger log = Logger.getLogger("com.zettamachine.sandbox.encryption");
		long start = 0L;
		long end = 0L;
		String zipName = UUID.randomUUID().toString();
		
		log.debug("AES Processor initializing");
		AESProcessor aesProcessor = new AESProcessor(
			"/Users/l088166/Workspace/neon/PGPTest/",
			zipName + ".zip",
			zipName.substring(0, 16)
		);
		log.debug("AES Processor initialized");
		try {
			log.debug("Generating AES key");
			start = System.currentTimeMillis();
			aesProcessor.genKey();
			end = System.currentTimeMillis();
			log.debug("Generated AES key in " + (end - start) + " msecs");
			SecretKey key = aesProcessor.getKey();
			FileOutputStream keyOs = new FileOutputStream("/Users/l088166/Workspace/neon/PGPTest/clear-symmetric.key");
			log.debug("Generated key size: " + key.getEncoded().length);
			keyOs.write(key.getEncoded());
			log.debug("RSA Processor initializing");
			RSAProcessor rsaProcessor = new RSAProcessor(
				"/Users/l088166/Workspace/neon/PGPTest/",
				"clear-symmetric.key"
			);
			log.debug("RSA Processor initialized");
			String zipPath = "/Users/l088166/Workspace/neon/PGPTest/" + zipName + ".zip";
			log.debug("Creating a zip file: " + zipPath);
			ZipProcessor zipProcessor = new ZipProcessor();
			start = System.currentTimeMillis();
			zipProcessor.zip(
				zipPath, 
				"/Users/l088166/Workspace/neon/PGPTest/", 
				new String[] {"beach.jpg", "pony.jpg"}
			);
			end = System.currentTimeMillis();
			log.debug("Created zip file in " + (end -start) + " msecs");
			log.debug("AES Encryption is happening - " + zipPath);
			start = System.currentTimeMillis();
			aesProcessor.encrypt();
			end = System.currentTimeMillis();
			log.debug("AES Encrypted in " + (end - start) + " msecs - actual file");
			String checksum = zipProcessor.digest("/Users/l088166/Workspace/neon/PGPTest/aes-enc-" + zipName + ".zip.aes");
			log.debug("SHA-256 of the encrypted file is : " + checksum);
			
			log.debug("Generating RSA keys");
			start = System.currentTimeMillis();
			rsaProcessor.genKey();
			end = System.currentTimeMillis();
			log.debug("Generated RSA keys in " + (end - start) + " msecs");
			log.debug("RSA Encryption is happening - symmetric key");
			start = System.currentTimeMillis();
			rsaProcessor.encrypt();
			end = System.currentTimeMillis();
			log.debug("RSA Encrypted in " + (end - start) + " msecs - symmetric key");
			
			log.debug("Now we have an AES encrypted file and RSA encrypted key");
			log.debug("After submission server should check SHA-256 digest, RSA decrypt the key and use it to AES decrypt the file");
			
			log.debug("Checking SHA-256 digest. Expected digest is " + checksum);
			start = System.currentTimeMillis();
			zipProcessor.checksum("/Users/l088166/Workspace/neon/PGPTest/aes-enc-" + zipName + ".zip.aes", checksum);
			end = System.currentTimeMillis();
			log.debug("Checked digest in " + (end - start) + " msecs");
			
			log.debug("RSA Decrypting the key");
			start = System.currentTimeMillis();
			rsaProcessor.decrypt();
			end = System.currentTimeMillis();
			log.debug("RSA Decrypted the key in " + (end - start) + " msecs");
			File encKeyFile = new File("/Users/l088166/Workspace/neon/PGPTest/rsa-enc-clear-symmetric.key.rsa");
			FileInputStream encKeyIs = new FileInputStream(encKeyFile);
			byte[] encKeyBytes = new byte[(int)encKeyFile.length()];
			encKeyIs.read(encKeyBytes);
			
			log.debug("Encrypted key is " + Base64.getEncoder().encodeToString(encKeyBytes));
			
			File keyFile = new File("/Users/l088166/Workspace/neon/PGPTest/rsa-dec-clear-symmetric.key");
			FileInputStream keyIs = new FileInputStream(keyFile);
			byte[] keyBytes = new byte[(int)keyFile.length()];
			keyIs.read(keyBytes);
			log.debug("Decrypted generated key size: " + keyBytes.length);
			keyBytes = Arrays.copyOfRange(keyBytes, 0, 32);
			log.debug("Curbed decrypted generated key size: " + keyBytes.length);
			
			log.debug("Decrypted key is " + Base64.getEncoder().encodeToString(keyBytes));
			log.debug("Setting decrypted key on AES processor");
			aesProcessor.setKeySpec(new SecretKeySpec(keyBytes, "AES"));
			log.debug("Set decrypted key on AES processor");
			
			log.debug("AES Decrypting the file ");
			start = System.currentTimeMillis();
			aesProcessor.decrypt();
			end = System.currentTimeMillis();
			log.debug("AES Decrypted in " + (end - start) + " msecs");
		} catch (Exception e) {
			log.error(e.getClass().getName(), e);
		}
	}

}
