package chain.viettel_einvoice;

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
        String fileName = String.format("%s %s.txt",chain.getProcessObject().get("moduleName").getAsString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        new Thread(new MonitorHandler(fileName)).start();
        JsonObject monitorObject = new JsonObject();
        monitorObject.addProperty("status", true);
        monitorObject.addProperty("notification", "Khởi động hệ thống ghi chép thông tin");
        MonitorHandler.addQueue(monitorObject);
        return true;
    }
}
