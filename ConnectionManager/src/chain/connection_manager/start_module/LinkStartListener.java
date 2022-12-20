package chain.connection_manager.start_module;

import chain.Chain;
import chain.Link;
import memorable.ConnectionManager;
import socket_handler.ListenerWrapper;
import socket_handler.connection_manager.ListenerHandler;

class LinkStartListener extends Link {
    LinkStartListener(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        int port = chain.getProcessObject().get("listenerPort").getAsInt(),
                timeout = chain.getProcessObject().get("timeout").getAsInt();
        String keyStorePassword = chain.getProcessObject().get("keyStorePassword").getAsString(),
                keyStorePath = chain.getProcessObject().get("keyStorePath").getAsString();
        ListenerWrapper listener;
        try {
            listener = new ListenerWrapper(port, keyStorePath, keyStorePassword);
        } catch (Exception e) {
            System.out.println("Cannot open listener");
            e.printStackTrace();
            return false;
        }
        ConnectionManager.getInstance().listenerWrapper = listener;
        ListenerHandler listenerHandler = new ListenerHandler(listener, timeout);
        new Thread(listenerHandler).start();
        ConnectionManager.getInstance().listenerHandler = listenerHandler;
        System.out.printf("A listener is operating at port %d\n", port);
        return true;
    }
}
