package chain.connection_manager.start_module;

import chain.Chain;
import chain.Link;
import memorable.ConnectionManager;
import socket_handler.connection_manager.SocketHandler;
import socket_handler.SocketWrapper;

import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

class LinkStartSocket extends Link {
    LinkStartSocket(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        int port = chain.getProcessObject().get("hostPort").getAsInt();
        String certPath = chain.getProcessObject().get("keyStorePath").getAsString(),
                certPass = chain.getProcessObject().get("keyStorePassword").getAsString(),
                hostAddress = chain.getProcessObject().get("hostAddress").getAsString();
        SocketWrapper socket;
        try {
            socket = new SocketWrapper(InetAddress.getByName(hostAddress), port, certPath, certPass);
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException |
                 UnrecoverableKeyException | KeyManagementException e) {
            System.err.println("Cannot connect to host");
            e.printStackTrace();
            return false;
        }
        ConnectionManager.getInstance().socket = socket;
        new Thread(new SocketHandler(socket)).start();
        System.out.printf("Connected to the host at %s:%d\n", hostAddress, port);
        return true;
    }
}
