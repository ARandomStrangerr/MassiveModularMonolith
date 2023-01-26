package socket_handler;

import java.net.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

// resources : http://www.java2s.com/Tutorial/Java/0490__Security/0860__SSL-Socket.htm
// this class wrap the socket and do basic function like read and write to data stream.
public class SocketWrapper {
    private final Socket socket;
    private final BufferedReader br;
    private final BufferedWriter bw;
    private final SocketType type;
    private String name;

    // mainly the job of this is for a socket accepted by a listener, the type of this socket depends on the listener which accepted it
    public SocketWrapper(Socket socket, SocketType type) throws IOException {
        this.socket = socket;
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.type = type;
    }

    /**
     * create a socket to the indicated address and port
     *
     * @args InetAddress: address to the host
     * int: port of the host
     * SocketType: type of the socket
     **/
    public SocketWrapper(InetAddress hostAddress, int port, SocketType type) throws IOException {
        switch (type) {
            case PLAIN -> {
                this.socket = new Socket(hostAddress, port);
                this.type = SocketType.PLAIN;
            }
            case TLS -> {
                this.socket = SSLSocketFactory.getDefault().createSocket(hostAddress, port);
                this.type = SocketType.TLS;
            }
            case default -> throw new IllegalArgumentException("The type of the socket is invalid");
        }
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * poor-man TSL socket constructor. This constructor create an TLS socket to a self-signed certificate host.
     * due to double handshake, both host and client must trust each other, but self-signed certificate cannot be trusted.
     * therefore, certificate of the host must be added manually. always create a TLS socket
     *
     * @args InetAddress: address of the host
     * int: port of the host
     * String: path to the truststore certificate file
     * String: the password for the trustore certificate file
     **/
    public SocketWrapper
    (InetAddress hostAddress, int port, String trustStorePath, String trustStorePassword)
            throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(trustStorePath), trustStorePassword.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, trustStorePassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sc = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        sc.init(kmf.getKeyManagers(), trustManagers, null);

        socket = sc.getSocketFactory().createSocket(hostAddress, port);
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        type = SocketType.TLS;
    }

    // write the given string to the data stream
    public synchronized void write(String data) throws IOException {
        bw.write(data);
        bw.newLine();
        bw.flush();
    }

    // read a line of data from the given stream
    public String read() throws IOException {
        return br.readLine();
    }

    public void setTimeout(int nanosecond) throws IOException {
        socket.setSoTimeout(nanosecond);
    }

    public void close() throws IOException {
        socket.close();
        br.close();
        bw.close();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
