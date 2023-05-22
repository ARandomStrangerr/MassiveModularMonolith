package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import system_monitor.MonitorHandler;

public class LinkLogFalseRequest extends Link {
    public LinkLogFalseRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject monitorObject = new JsonObject();
        monitorObject.addProperty("status", false);
        monitorObject.add("notification", chain.getProcessObject().get("body").getAsJsonObject().get("error"));
        monitorObject.add("request", chain.getProcessObject());
        MonitorHandler.addQueue(monitorObject);
        return true;
    }
}
