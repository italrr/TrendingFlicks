package com.nite.projecteuropa.Tools;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.Base64;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509TrustManager;

import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.conn.ssl.X509HostnameVerifier;

public class Controller {
    private static Controller singleton = null;
    private static AsyncHttpClient client = null;
    private static String hostOMDB = "http://www.omdbapi.com/";
    private static String hostEuropa = "http://juble.xyz:8185/";

    public static Controller get(){
        if(singleton == null){
            singleton = new Controller();
        }
        return singleton;
    }

    public static String getHostOMDB(){
        return hostOMDB;
    }

    public static String getHostEuropa(){
        return hostEuropa;
    }

    public static AsyncHttpClient getClient(){
        if(client == null){
            try {
                SSLContext contextSSL = SSLContext.getInstance("SSL");
                contextSSL.init(null, new X509TrustManager[]{new X509TrustManager(){
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }}}, new SecureRandom());
                client = new AsyncHttpClient();
                client.setSSLSocketFactory(new SSLSocketFactory(contextSSL, new X509HostnameVerifier(){
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                    @Override
                    public void verify(String host, SSLSocket ssl) throws IOException {}
                    @Override
                    public void verify(String host, X509Certificate cert) throws SSLException {}
                    @Override
                    public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                    }} ));
                client.addHeader("Content-Type", "application/json");

            } catch (Exception e) { // should never happen
                e.printStackTrace();
            }
        }
        return client;
    }

    private Controller(){

    }
}
