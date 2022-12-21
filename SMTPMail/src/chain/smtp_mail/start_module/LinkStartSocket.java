package chain.smtp_mail.start_module;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.SMTPMail;
import socket_handler.smtp_mail.SocketHandler;
import socket_handler.SocketWrapper;

import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class LinkStartSocket extends Link {
    public LinkStartSocket(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject processObject = chain.getProcessObject();
        SocketWrapper socketWrapper;
        try {
            socketWrapper = new SocketWrapper(InetAddress.getByName(processObject.get("address").getAsString()),
                    processObject.get("port").getAsInt(), processObject.get("keyStorePath").getAsString(),
                    processObject.get("keyStorePassword").getAsString());
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException |
                 UnrecoverableKeyException | KeyManagementException e) {
            e.printStackTrace();
            return false;
        }
        new Thread(new SocketHandler(socketWrapper, chain.getProcessObject().get("moduleName").getAsString())).start();
        SMTPMail.getInstance().socket = socketWrapper;
        System.out.printf("connected to the host at %s:%d", processObject.get("address").getAsString(), processObject.get("port").getAsInt());
        return true;
    }
}
