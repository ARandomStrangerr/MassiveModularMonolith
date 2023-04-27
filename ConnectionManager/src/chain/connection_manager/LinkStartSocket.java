package chain.connection_manager;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.ConnectionManager;
import socket_handler.connection_manager.SocketHandler;
import socket_handler.SocketWrapper;
import system_monitor.MonitorHandler;

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
        JsonObject monitorObj = new JsonObject();
        monitorObj.addProperty("status", true);
        monitorObj.addProperty("notification", "Thành cồng kết nối đến Data Stream module");
        MonitorHandler.addQueue(monitorObj);
        return true;
    }
}
