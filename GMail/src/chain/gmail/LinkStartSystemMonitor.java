package chain.gmail;

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
        String fileName = String.format("%s %s.txt", chain.getProcessObject().get("moduleName").getAsString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        new Thread(new MonitorHandler(fileName)).start();
        JsonObject monitorObj = new JsonObject();
        monitorObj.addProperty("status", true);
        monitorObj.addProperty("notification", "Bắt đầu hệ thống ghi chép");
        MonitorHandler.addQueue(monitorObj);
        return false;
    }
}
