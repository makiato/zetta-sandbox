package com.zettamachine.sandbox.test;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

public class AnyTest {

	public static void main(String[] args) {
		Logger log = Logger.getLogger("com.zettamachine.sandbox");
		try {
			log.info("Content-type: " + Files.probeContentType(Paths.get("/Users/l088166/Workspace/neon/PGPTest/pony.jpg")));
		} catch (Exception e) {
			log.error(e.getClass().getName(), e);
		}
	}

}
