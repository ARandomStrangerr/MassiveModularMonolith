package chain.connection_manager.request_handler;

import chain.Chain;
import chain.Link;
import memorable.ConnectionManager;

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
            ConnectionManager.getInstance().listenerHandler.getThread(chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsString()).stop();
            System.out.printf("Socket and thread associated working for the socket %s has been closed and stopped\n", chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsString());
        }
        return true;
    }
}
