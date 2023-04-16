package socket_handler;

import java.io.*;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Hashtable;

import javax.net.ssl.*;

/**
 * this class creates a wrapper for the ServerSocket.
 **/
public class ListenerWrapper {
    private final ServerSocket serverSocket;
    private final SocketType type;
    private final Hashtable<String, SocketWrapper> socketStorage;

    /**
     * this constructor creates a plain ServerSocket without encryption
     *
     * @param port: the port which the ServerSocket operates on
     **/
    public ListenerWrapper(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        type = SocketType.PLAIN;
        socketStorage = new Hashtable<>();
    }

    /**
     * this constructor create a ServerSocket with TLS encryption. a SSLServerSocket converted to its superclass (ServerSocket)
     *
     * @param port                 the port which the ServerSocket operates on
     * @param keyStoreFilePath     the path to the JavaKeyStore file, which is generated with keytool
     * @param keyStoreFilePassword the password for the above file
     * @throws IOException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public ListenerWrapper(int port, String keyStoreFilePath, String keyStoreFilePassword) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        // more detail about how to create KeyStore with keytool https://docs.oracle.com/cd/E19509-01/820-3503/ggfen/index.html
        // this class represents a storage facility for cryptographic keys and certificates.
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keyStoreFilePath), keyStoreFilePassword.toCharArray());
        // This class acts as a factory for key managers based on a source of key material.
        // Each key manager manages a specific type of key material for use by secure sockets.
        // The key material is based on a KeyStore and/or provider specific sources.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyStoreFilePassword.toCharArray());

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null, null);

        SSLServerSocketFactory sslServerFactory = context.getServerSocketFactory();
        serverSocket = sslServerFactory.createServerSocket(port);

        type = SocketType.TLS;
        socketStorage = new Hashtable<>();
    }

    /**
     * accept a socket tries to connect to this listener
     *
     * @return return a SocketWrapper
     **/
    public SocketWrapper accept() throws IOException {
        return new SocketWrapper(serverSocket.accept(), this.type);
    }

    /**
     * get the type of the listener
     **/
    public SocketType getType() {
        return type;
    }

    public SocketWrapper getSocket(String key){
        return socketStorage.get(key);
    }

    public void putSocket(String key, SocketWrapper socket){
        socketStorage.put(key, socket);
    }

    public SocketWrapper removeSocket(String key){
        return socketStorage.remove(key);
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void close() throws IOException {
        serverSocket.close();
    }
}
