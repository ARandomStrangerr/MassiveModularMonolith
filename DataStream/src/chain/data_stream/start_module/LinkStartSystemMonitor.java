package chain.data_stream.start_module;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import system_monitor.MonitorHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LinkStartSystemMonitor extends Link {
    public LinkStartSystemMonitor(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        new Thread(new MonitorHandler(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".txt")).start();
        JsonObject monitorObject = new JsonObject();
        monitorObject.addProperty("status", "SUCCESS");
        monitorObject.addProperty("notification", "start thread for monitoring tool");
        MonitorHandler.addQueue(monitorObject);
        return true;
    }
}
