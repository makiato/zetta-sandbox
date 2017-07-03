package com.zettamachine.sandbox.encryption.test;
import org.apache.log4j.Logger;

import com.zettamachine.sandbox.encryption.pgp.PGPProcessor;

public class PGPTest {

	public static void main(String[] args) {
		Logger log = Logger.getLogger("com.zettamachine.sandbox.encryption");
		long start = 0L;
		long end = 0L;
		
		log.debug("PGP Processor initializing");
		PGPProcessor pgpProcessor = new PGPProcessor(
			false, 
			"captainjack", 
			"blackpearl", 
			false, 
			"pgp-pub.key", 
			"pgp-private.key", 
			"/Users/l088166/Workspace/neon/PGPTest/", 
			"random.txt" 
		);
		log.debug("PGP Processor initialized");
		try {
			log.debug("Generating key pair");
			start = System.currentTimeMillis();
			pgpProcessor.genKeyPair();
			end = System.currentTimeMillis();
			log.debug("Generated key pair in " + (end -start) + " msecs.");
			log.debug("Encrypting input file");
			start = System.currentTimeMillis();
			pgpProcessor.encrypt();
			end = System.currentTimeMillis();
			log.debug("Encrypted input file in " + (end -start) + " msecs.");
			log.debug("Decrypting input file");
			start = System.currentTimeMillis();
			pgpProcessor.decrypt();
			end = System.currentTimeMillis();
			log.debug("Decrypted input file in " + (end -start) + " msecs.");
		} catch (Exception e) {
			log.error(e.getClass().getName(), e);
		} 
	}

}
