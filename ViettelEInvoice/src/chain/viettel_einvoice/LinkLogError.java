package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import system_monitor.MonitorHandler;

public class LinkLogError extends Link {
    public LinkLogError(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject monitorObject = new JsonObject();
        monitorObject.addProperty("status", false);
        monitorObject.add("notification", chain.getProcessObject().get("error"));
        MonitorHandler.addQueue(monitorObject);
        return true;
    }
}
