package com.sevya.onemoney.utility;

import java.security.cert.CertificateException;

import javax.net.ssl.X509TrustManager;

public class HttpsTrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(java.security.cert.X509Certificate[] arg0,
			String arg1) throws CertificateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkServerTrusted(java.security.cert.X509Certificate[] arg0,
			String arg1) throws CertificateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return new java.security.cert.X509Certificate[]{};
	} 

}
