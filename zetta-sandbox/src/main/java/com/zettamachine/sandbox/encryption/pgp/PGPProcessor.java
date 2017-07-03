package com.zettamachine.sandbox.encryption.pgp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;

public class PGPProcessor {

	private boolean isArmored = false;
	private String id = "";
	private String passwd = "";
	private boolean integrityCheck = true;

	private String pubKeyFile = "";
	private String privKeyFile = "";

	private String path = "";
	private String clearFile = "";
	private String encryptedFile = "";
	private String decryptedFile = "";
	
	Logger log = Logger.getLogger("com.zettamachine.sandbox.encryption");
	
	public PGPProcessor(boolean isArmored, String id, String passwd, boolean integrityCheck, String pubKeyFile,
			String privKeyFile, String path, String clearFile) {
		super();
		this.isArmored = isArmored;
		this.id = id;
		this.passwd = passwd;
		this.integrityCheck = integrityCheck;
		this.pubKeyFile = pubKeyFile;
		this.privKeyFile = privKeyFile;
		this.path = path;
		this.clearFile = path + clearFile;
		this.encryptedFile = path + "pgp-enc-" + clearFile + ".pgp";
		this.decryptedFile = path + "pgp-dec-" + clearFile;
	}

	public void genKeyPair() throws InvalidKeyException, NoSuchProviderException, SignatureException, IOException,
			PGPException, NoSuchAlgorithmException {
		PGPRSAKeyPairGenerator rkpg = new PGPRSAKeyPairGenerator();
		Security.addProvider(new BouncyCastleProvider());
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
		kpg.initialize(2048);
		KeyPair kp = kpg.generateKeyPair();
		FileOutputStream out1 = new FileOutputStream(privKeyFile);
		FileOutputStream out2 = new FileOutputStream(pubKeyFile);
		log.debug("Public key is " + Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()));
		log.debug("Private key is " + Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded()));
		rkpg.exportKeyPair(out1, out2, kp.getPublic(), kp.getPrivate(), id, passwd.toCharArray(), isArmored);
	}

	public void encrypt() throws NoSuchProviderException, IOException, PGPException {
		FileInputStream pubKeyIs = new FileInputStream(pubKeyFile);
		FileOutputStream cipheredFileIs = new FileOutputStream(encryptedFile);
		PGPKeyUtil.getInstance().encryptFile(cipheredFileIs, clearFile,
				PGPKeyUtil.getInstance().readPublicKey(pubKeyIs), isArmored, integrityCheck);
		cipheredFileIs.close();
		pubKeyIs.close();
	}

	public void decrypt() throws Exception  {
		FileInputStream cipheredFileIs = new FileInputStream(encryptedFile);
		FileInputStream privKeyIn = new FileInputStream(privKeyFile);
		FileOutputStream plainTextFileIs = new FileOutputStream(decryptedFile);
		PGPKeyUtil.getInstance().decryptFile(cipheredFileIs, plainTextFileIs, privKeyIn, passwd.toCharArray());
		cipheredFileIs.close();
		plainTextFileIs.close();
		privKeyIn.close();
	}

}
