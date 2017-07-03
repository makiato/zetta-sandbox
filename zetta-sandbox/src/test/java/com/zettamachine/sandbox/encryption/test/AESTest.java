package com.zettamachine.sandbox.encryption.test;

import org.apache.log4j.Logger;

import com.zettamachine.sandbox.encryption.aes.AESProcessor;

public class AESTest {

	public static void main(String[] args) {
		Logger log = Logger.getLogger("com.zettamachine.sandbox.encryption");
		long start = 0L;
		long end = 0L;
		
		log.debug("AES Processor initializing");
		AESProcessor aesProcessor = new AESProcessor(
			"/Users/l088166/Workspace/neon/PGPTest/",
			"beach.jpg",
			"1234567890123456"
		);
		log.debug("AES Processor initialized");
		try {
			log.debug("Generating AES key");
			start = System.currentTimeMillis();
			aesProcessor.genKey();
			end = System.currentTimeMillis();
			log.debug("Generated AES key in " + (end - start) + " msecs");
			log.debug("Encryption is happening");
			start = System.currentTimeMillis();
			aesProcessor.encrypt();
			end = System.currentTimeMillis();
			log.debug("Encrypted in " + (end - start) + " msecs");
			start = System.currentTimeMillis();
			aesProcessor.decrypt();
			end = System.currentTimeMillis();
			log.debug("Decrypted in " + (end - start) + " msecs");
		} catch (Exception e) {
			log.error(e.getClass().getName(), e);
		}
	}

}
