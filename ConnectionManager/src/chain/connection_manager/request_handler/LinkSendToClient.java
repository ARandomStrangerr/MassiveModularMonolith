package chain.connection_manager.request_handler;

import chain.Chain;
import chain.Link;
import memorable.ConnectionManager;
import socket_handler.SocketWrapper;

import java.io.IOException;

public class LinkSendToClient extends Link {
    public LinkSendToClient(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        String socketName = chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsString();
        SocketWrapper soc = ConnectionManager.getInstance().listenerWrapper.getSocket(socketName);
        try {
            soc.write(chain.getProcessObject().get("body").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
