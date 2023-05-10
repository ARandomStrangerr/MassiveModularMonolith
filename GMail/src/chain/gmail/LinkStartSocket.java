package chain.gmail;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.GMail;
import socket_handler.SocketWrapper;
import socket_handler.gmail.SocketHandler;
import system_monitor.MonitorHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LinkStartSocket extends Link {
    public LinkStartSocket(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        int dataStreamPort = chain.getProcessObject().get("port").getAsInt();
        InetAddress dataStreamAddress;
        try{
            dataStreamAddress = InetAddress.getByName(chain.getProcessObject().get("address").getAsString());
        } catch (UnknownHostException e){
            JsonObject monitorObj = new JsonObject();
            monitorObj.addProperty("status", false);
            monitorObj.addProperty("notification", "Địa chỉ máy chủ không hợp lệ");
            MonitorHandler.addQueue(monitorObj);
            return false;
        }
        String jksPath = chain.getProcessObject().get("jksPath").getAsString();
        String jksPass = chain.getProcessObject().get("jksPassword").getAsString();
        SocketWrapper socket;
        try{
            socket = new SocketWrapper(dataStreamAddress,dataStreamPort,jksPath, jksPass);
        } catch (Exception e){
            JsonObject monitorObj = new JsonObject();
            monitorObj.addProperty("status", true);
            monitorObj.addProperty("notification", "Kết nối đến DataStream không thành công");
            MonitorHandler.addQueue(monitorObj);
            return false;
        }
        GMail.getInstance().socketToDataStream = socket;
        new Thread(new SocketHandler(socket)).start();
        JsonObject monitorObj = new JsonObject();
        monitorObj.addProperty("status", true);
        monitorObj.addProperty("notification", "Kết nối đến DataStream");
        MonitorHandler.addQueue(monitorObj);
        return true;
    }
}
