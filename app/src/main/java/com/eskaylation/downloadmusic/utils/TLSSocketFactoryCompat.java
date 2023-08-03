package com.eskaylation.downloadmusic.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import com.eskaylation.downloadmusic.R;
public class TLSSocketFactoryCompat extends SSLSocketFactory {
    public static TLSSocketFactoryCompat instance;
    public SSLSocketFactory internalSSLSocketFactory;

    public TLSSocketFactoryCompat() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext instance2 = SSLContext.getInstance("TLS");
        instance2.init(null, null, null);
        this.internalSSLSocketFactory = instance2.getSocketFactory();
    }

    public static TLSSocketFactoryCompat getInstance() throws NoSuchAlgorithmException, KeyManagementException {
        TLSSocketFactoryCompat tLSSocketFactoryCompat = instance;
        if (tLSSocketFactoryCompat != null) {
            return tLSSocketFactoryCompat;
        }
        instance = new TLSSocketFactoryCompat();
        return instance;
    }

    public String[] getDefaultCipherSuites() {
        return this.internalSSLSocketFactory.getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return this.internalSSLSocketFactory.getSupportedCipherSuites();
    }

    public Socket createSocket() throws IOException {
        Socket createSocket = this.internalSSLSocketFactory.createSocket();
        enableTLSOnSocket(createSocket);
        return createSocket;
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        Socket createSocket = this.internalSSLSocketFactory.createSocket(socket, str, i, z);
        enableTLSOnSocket(createSocket);
        return createSocket;
    }

    public Socket createSocket(String str, int i) throws IOException {
        Socket createSocket = this.internalSSLSocketFactory.createSocket(str, i);
        enableTLSOnSocket(createSocket);
        return createSocket;
    }

    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
        Socket createSocket = this.internalSSLSocketFactory.createSocket(str, i, inetAddress, i2);
        enableTLSOnSocket(createSocket);
        return createSocket;
    }

    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        Socket createSocket = this.internalSSLSocketFactory.createSocket(inetAddress, i);
        enableTLSOnSocket(createSocket);
        return createSocket;
    }

    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        Socket createSocket = this.internalSSLSocketFactory.createSocket(inetAddress, i, inetAddress2, i2);
        enableTLSOnSocket(createSocket);
        return createSocket;
    }

    public final Socket enableTLSOnSocket(Socket socket) {
        if (socket != null && (socket instanceof SSLSocket)) {
            ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1.1", "TLSv1.2"});
        }
        return socket;
    }
}
