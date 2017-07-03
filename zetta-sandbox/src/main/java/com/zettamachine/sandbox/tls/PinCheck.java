package com.zettamachine.sandbox.tls;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;

public class PinCheck {
	Logger log = Logger.getLogger("com.zettamachine.sandbox.tls");
	private String CERT_LOCATION = "/Users/l088166/Workspace/neon/SSLPin/stgeorge.com.au.cer";
	private String KEY_STORE_TYPE = "jks";
	private String TRUST_MANAGER_ALGO = "PKIX";
	private String HOST = "";
	X509Certificate cert = null;
	
	public void check(String HOST) {
		try {
			this.HOST = HOST;
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");			
			cert = (X509Certificate)
				certificateFactory.generateCertificate(new FileInputStream(CERT_LOCATION));
			log.debug("Serial number of the cert (" + CERT_LOCATION + ") 0" + cert.getSerialNumber().toString(16));
			log.debug("Creating a key store of type: " + KEY_STORE_TYPE);
			
			KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
			keyStore.load(null, null);
			keyStore.setCertificateEntry("cert", cert);
			
			log.debug("Creating a trust manager using: " + TRUST_MANAGER_ALGO);
			
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TRUST_MANAGER_ALGO);
			trustManagerFactory.init(keyStore);
			
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
			
			log.debug("Opening connection to " + HOST);
			URL url = new URL(HOST);
			Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("intproxy80.prod.srv.westpac.com.au", 80));
			HttpsURLConnection https = (HttpsURLConnection)url.openConnection(proxy);
			https.setSSLSocketFactory(sslContext.getSocketFactory());
			
			BufferedReader page = new BufferedReader(new InputStreamReader(https.getInputStream()));
			String html = "";
			while ((html = page.readLine()) != null) {
				log.info(html);
			}
			log.debug(https.getContentEncoding());
			log.debug("Opened connection to " + HOST);
		} catch (SSLHandshakeException e) {
			log.error("The certificate in the KeyStore (" + cert.getSubjectDN().getName() + ") is not valid for " + HOST);
		} catch (Exception e) {
			log.error(e.getClass().getName(), e);
		}

	}

}
