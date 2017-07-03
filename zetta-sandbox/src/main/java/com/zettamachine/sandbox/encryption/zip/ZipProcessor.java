package com.zettamachine.sandbox.encryption.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class ZipProcessor {
	static final int BUFFER = 2048; 
	Logger log = Logger.getLogger("com.zettamachine.sandbox.encryption");
	
	public String zip(String zipFile, String path, String[] files) throws IOException, NoSuchAlgorithmException {
		FileOutputStream fos = new FileOutputStream(zipFile);
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		DigestOutputStream checksum = new DigestOutputStream(fos, md);
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(checksum));
		BufferedInputStream bis = null;
		byte[] data = new byte[BUFFER];
		for(String file: files) {
			bis = new BufferedInputStream(new FileInputStream(path + file));
			ZipEntry entry = new ZipEntry(file);
			zos.putNextEntry(entry);
			int index = 0;
			while ((index = bis.read(data, 0, BUFFER)) != -1) {
				zos.write(data, 0, index);
			}
			bis.close();
		}
		zos.close();
		return Base64.getEncoder().encodeToString(md.digest());
	}
	
	public void checksum(String file, String digest) throws IOException, NoSuchAlgorithmException {
		File _file = new File(file);
		FileInputStream fis = new FileInputStream(_file);
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		DigestInputStream dis = new DigestInputStream(fis, md);
		byte[] fileBytes = new byte[(int)_file.length()];
		dis.read(fileBytes);
		String checksum = Base64.getEncoder().encodeToString(md.digest());
		log.debug("Digest for the given file is: " + checksum);
		log.debug("Provided digest value is: " + digest);
		if(checksum.equals(digest))
			log.debug("It is the correct file"); 
		else 
			log.debug("It is the wrong file");
	}
	
	public String digest(String file) throws IOException, NoSuchAlgorithmException {
		File _file = new File(file);
		FileInputStream fis = new FileInputStream(_file);
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		DigestInputStream dis = new DigestInputStream(fis, md);
		byte[] fileBytes = new byte[(int)_file.length()];
		dis.read(fileBytes);
		String checksum = Base64.getEncoder().encodeToString(md.digest());
		return checksum;
	}
}
