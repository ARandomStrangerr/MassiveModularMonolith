package chain.connection_manager;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.ConnectionManager;
import socket_handler.ListenerWrapper;
import socket_handler.connection_manager.ListenerHandler;
import system_monitor.MonitorHandler;

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
        JsonObject monitorObj = new JsonObject();
        monitorObj.addProperty("status", true);
        monitorObj.addProperty("notification", "Thành cồng mở cổng để kết nối với máy con bên ngoài");
        MonitorHandler.addQueue(monitorObj);
        return true;
    }
}
