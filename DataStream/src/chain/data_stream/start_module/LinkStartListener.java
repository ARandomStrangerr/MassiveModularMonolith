package chain.data_stream.start_module;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.DataStream;
import socket_handler.data_stream.ListenerHandler;
import socket_handler.ListenerWrapper;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

class LinkStartListener extends Link {
    LinkStartListener(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject processObject = chain.getProcessObject();
        ListenerWrapper listener;
        try {
            listener = new ListenerWrapper(processObject.get("port").getAsInt(), processObject.get("keyStorePath").getAsString(), processObject.get("keyStorePassword").getAsString());
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException |
                 UnrecoverableKeyException | KeyManagementException e) {
            e.printStackTrace();
            return false;
        }
        new Thread(new ListenerHandler(listener, processObject.get("timeout").getAsInt())).start();
        DataStream.getInstance().setListener(listener);
        System.out.println("A TLS socket is running at port " + processObject.get("port").getAsString());
        return true;
    }
}