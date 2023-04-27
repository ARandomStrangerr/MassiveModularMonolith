package chain.connection_manager;

import chain.Chain;
import chain.Link;
import memorable.ConnectionManager;
import socket_handler.SocketWrapper;

import java.io.IOException;

public class LinkTerminateSocketFromClient extends Link {
    public LinkTerminateSocketFromClient(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        if (!chain.getProcessObject().get("header").getAsJsonObject().get("status").getAsBoolean()) {
            try {
                ConnectionManager.getInstance().listenerWrapper.removeSocket(chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsString()).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
