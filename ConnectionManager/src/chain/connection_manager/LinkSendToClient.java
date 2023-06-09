package chain.connection_manager;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.ConnectionManager;
import socket_handler.SocketWrapper;
import system_monitor.MonitorHandler;

import java.io.IOException;

public class LinkSendToClient extends Link {
    public LinkSendToClient(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        String socketName = chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsString();
        SocketWrapper soc = ConnectionManager.listener.getSocket(socketName);
        try {
            soc.write(chain.getProcessObject().get("body").toString());
        } catch (IOException e) {
            JsonObject monitorObj = new JsonObject();
            monitorObj.addProperty("status", false);
            monitorObj.addProperty("notification", "Không thể gửi tin nhắn đến client");
            monitorObj.add("request", chain.getProcessObject());
            MonitorHandler.addQueue(monitorObj);
        } catch (NullPointerException e) {
            JsonObject monitorObj = new JsonObject();
            monitorObj.addProperty("status", false);
            monitorObj.addProperty("notification", String.format("Không tìm thấy socket với tên %s", socketName));
            monitorObj.add("request", chain.getProcessObject());
            MonitorHandler.addQueue(monitorObj);
        }
        return true;
    }
}
