package com.zettamachine.sandbox.encryption.test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

import com.zettamachine.sandbox.encryption.zip.ZipProcessor;

public class ZipTest {
	
	public static void main(String[] args) {
		Logger log = Logger.getLogger("com.zettamachine.sandbox.encryption");
		ZipProcessor zipProcessor = new ZipProcessor();
		try {
			log.debug("zipping");
			String zipName = "/Users/l088166/Workspace/neon/PGPTest/" + UUID.randomUUID() + ".zip"; 
			String checksum = zipProcessor.zip(
				zipName, 
				"/Users/l088166/Workspace/neon/PGPTest/", 
				new String[] {"beach.jpg", "pony.jpg"}
			);
			log.debug("zipped: " + checksum);
			log.debug("Zipped file path: " + zipName);
			
			ZipFile zipFile = new ZipFile(zipName);
			log.debug("Content-type: " + Files.probeContentType(Paths.get("/Users/l088166/Workspace/neon/PGPTest/pony.jpg")));
			log.debug("Content-type: " + Files.probeContentType(Paths.get(zipName)));
			
			Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>)zipFile.entries();
			ZipEntry entry = null;
			
			while(entries.hasMoreElements()) {
				entry = entries.nextElement();
				log.debug("File: " + entry.getName() + " | " + entry.getSize());
			}
			
			zipProcessor.checksum(zipName, checksum);
		} catch (Exception e) {
			log.error(e.getClass().getName(), e);
		}
	}

}
