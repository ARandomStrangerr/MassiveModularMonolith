package chain.gmail.start_module;

import chain.Chain;
import chain.Link;
import memorable.GMail;
import socket_handler.SocketWrapper;
import socket_handler.gmail.SocketHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LinkConnectToDataStream extends Link {
    public LinkConnectToDataStream(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        int port = chain.getProcessObject().get("port").getAsInt();
        InetAddress dataStreamAddress;
        try {
            dataStreamAddress = InetAddress.getByName(chain.getProcessObject().get("dataStreamAddress").getAsString());
        } catch (UnknownHostException e){
            e.printStackTrace();
            return false;
        }
        String keyStore = chain.getProcessObject().get("keyStore").getAsString(),
            keyStorePassword = chain.getProcessObject().get("keyStorePassword").getAsString();
        SocketWrapper socket;
        try {
            socket = new SocketWrapper(dataStreamAddress, port, keyStore, keyStorePassword);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        GMail.getInstance().socketToDataStream = socket;
        new Thread(new SocketHandler(socket)).start();
        System.out.printf("Connected to Data Stream at %s:%d", dataStreamAddress, port);
        return true;
    }
}
