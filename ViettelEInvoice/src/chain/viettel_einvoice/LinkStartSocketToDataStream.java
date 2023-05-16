package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.ViettelEInvoice;
import socket_hander.viettel_einvoice.SocketHandler;
import socket_handler.SocketWrapper;
import system_monitor.MonitorHandler;

import java.net.InetAddress;

public class LinkStartSocketToDataStream extends Link {
    public LinkStartSocketToDataStream(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        try {
            InetAddress address = InetAddress.getByName(chain.getProcessObject().get("hostAddress").getAsString());
            int port = chain.getProcessObject().get("hostPort").getAsInt();
            String jksPath = chain.getProcessObject().get("jksPath").getAsString();
            String jksPassword = chain.getProcessObject().get("jksPassword").getAsString();
            String moduleName = chain.getProcessObject().get("moduleName").getAsString();
            SocketWrapper socket = new SocketWrapper(address, port, jksPath, jksPassword);
            ViettelEInvoice.socketToDataStream = socket;
            new Thread(new SocketHandler(socket, moduleName)).start();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        JsonObject monitorObject = new JsonObject();
        monitorObject.addProperty("status", true);
        monitorObject.addProperty("notification", "kết nối đến Data Stream module");
        MonitorHandler.addQueue(monitorObject);
        return true;
    }
}
