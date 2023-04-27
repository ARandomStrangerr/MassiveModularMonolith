package chain.connection_manager;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import system_monitor.MonitorHandler;

public class LinkLogFailureRequest extends Link {
    public LinkLogFailureRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject monitorObj = new JsonObject();
        monitorObj.addProperty("status", false);
        monitorObj.add("notification", chain.getProcessObject().get("body").getAsJsonObject().get("error"));
        monitorObj.add("request", chain.getProcessObject());
        MonitorHandler.addQueue(monitorObj);
        return false;
    }
}
