package chain.data_stream.start_module;

import chain.Chain;
import chain.Link;
import system_monitor.MonitorHandler;

import java.io.IOException;

public class LinkStartSystemMonitor extends Link {
    public LinkStartSystemMonitor(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        new Thread(new MonitorHandler("log.txt")).start();
        MonitorHandler.addQueue("SUCCESS start thread for monitoring tool");
        return true;
    }
}
