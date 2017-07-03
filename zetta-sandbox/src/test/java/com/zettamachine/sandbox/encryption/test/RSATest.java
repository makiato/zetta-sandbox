package com.zettamachine.sandbox.encryption.test;

import org.apache.log4j.Logger;

import com.zettamachine.sandbox.encryption.rsa.RSAProcessor;

public class RSATest {

	public static void main(String[] args) {
		Logger log = Logger.getLogger("com.zettamachine.sandbox.encryption");
		long start = 0L;
		long end = 0L;
		
		log.debug("AES Processor initializing");
		RSAProcessor rsaProcessor = new RSAProcessor(
			"/Users/l088166/Workspace/neon/PGPTest/",
			"pony.jpg"
		);
		log.debug("RSA Processor initialized");
		try {
			log.debug("Generating RSA keys");
			start = System.currentTimeMillis();
			rsaProcessor.genKey();
			end = System.currentTimeMillis();
			log.debug("Generated RSA keys in " + (end - start) + " msecs");
			log.debug("Encryption is happening");
			start = System.currentTimeMillis();
			rsaProcessor.encrypt();
			end = System.currentTimeMillis();
			log.debug("Encrypted in " + (end - start) + " msecs");
			log.debug("Decryption is happening");
			start = System.currentTimeMillis();
			rsaProcessor.decrypt();
			end = System.currentTimeMillis();
			log.debug("Decrypted in " + (end - start) + " msecs");
		} catch (Exception e) {
			log.error(e.getClass().getName(), e);
		} 
	}

}
