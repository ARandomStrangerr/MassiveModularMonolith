package chain.data_stream.start_module;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import socket_handler.ListenerWrapper;
import socket_handler.SocketWrapper;
import system_monitor.MonitorHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LinkStartSystemMonitor extends Link {
    public LinkStartSystemMonitor(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        String logFileName = String.format("%s %s.txt", chain.getProcessObject().get("moduleName").getAsString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        if (chain.getProcessObject().has("monitorToolPort")){
            SocketWrapper socket;
            try {
                ListenerWrapper listener = new ListenerWrapper(chain.getProcessObject().get("monitorToolPort").getAsInt());
                socket = listener.accept();
                listener.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            new Thread(new MonitorHandler(logFileName)).start();
        }
        else new Thread(new MonitorHandler(logFileName)).start();
        JsonObject monitorObject = new JsonObject();
        monitorObject.addProperty("status", true);
        monitorObject.addProperty("notification", "start thread for monitoring tool");
        MonitorHandler.addQueue(monitorObject);
        return true;
    }
}
