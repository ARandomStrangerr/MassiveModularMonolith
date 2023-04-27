package chain.connection_manager;

import chain.Chain;
import chain.Link;
import system_monitor.MonitorHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LinkStartMonitorThread extends Link {
    public LinkStartMonitorThread(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        String fileName = String.format("%s %s.txt",chain.getProcessObject().get("moduleName").getAsString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        new Thread(new MonitorHandler(fileName)).start();
        return true;
    }
}
