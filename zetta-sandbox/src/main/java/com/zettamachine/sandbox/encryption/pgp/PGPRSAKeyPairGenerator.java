package com.zettamachine.sandbox.encryption.pgp;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.Date;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.RSASecretBCPGKey;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyConverter;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

public class PGPRSAKeyPairGenerator {
	    public void exportKeyPair(
	        OutputStream    secretOut,
	        OutputStream    publicOut,
	        PublicKey       publicKey,
	        PrivateKey      privateKey,
	        String          identity,
	        char[]          passPhrase,
	        boolean         armor)
	        throws IOException, InvalidKeyException, NoSuchProviderException, SignatureException, PGPException
	    {    
	        if (armor)
	        {
	            secretOut = new ArmoredOutputStream(secretOut);
	        }
	        
	        
	        PGPPublicKey a = (new JcaPGPKeyConverter().getPGPPublicKey(PGPPublicKey.RSA_GENERAL, publicKey, new Date()));
	        RSAPrivateCrtKey rsK = (RSAPrivateCrtKey)privateKey;
	        RSASecretBCPGKey privPk = new RSASecretBCPGKey(rsK.getPrivateExponent(), rsK.getPrimeP(), rsK.getPrimeQ());
	        PGPPrivateKey b = new PGPPrivateKey(a.getKeyID(), a.getPublicKeyPacket(), privPk);

	        PGPDigestCalculator sha1Calc = new JcaPGPDigestCalculatorProviderBuilder().build().get(HashAlgorithmTags.SHA1);
	        PGPKeyPair          keyPair = new PGPKeyPair(a,b);
	        PGPSecretKey        secretKey = new PGPSecretKey(PGPSignature.DEFAULT_CERTIFICATION, keyPair, identity, sha1Calc, null, null, new JcaPGPContentSignerBuilder(keyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1), new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.CAST5, sha1Calc).setProvider("BC").build(passPhrase));
	        
	        secretKey.encode(secretOut);
	        
	        secretOut.close();
	        
	        if (armor)
	        {
	            publicOut = new ArmoredOutputStream(publicOut);
	        }

	        PGPPublicKey    key = secretKey.getPublicKey();
	        
	        key.encode(publicOut);
	        
	        publicOut.close();
	    }
	    
	    
	}
