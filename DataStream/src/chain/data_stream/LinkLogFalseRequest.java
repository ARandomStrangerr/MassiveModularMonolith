package chain.data_stream;

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
        monitorObject.addProperty("notification", chain.getProcessObject().get("error").getAsString());
        monitorObject.add("request", chain.getProcessObject());
        MonitorHandler.addQueue(monitorObject);
        return true;
    }
}
