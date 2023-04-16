package chain.data_stream.handle_request;

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
        JsonObject monitorObject = new JsonObject(),
            request = new JsonObject();
        monitorObject.addProperty("status", "FAILURE");
        monitorObject.add("notification", chain.getProcessObject().remove("notification"));
        request.add("header",  chain.getProcessObject().get("header"));
        request.add("body", chain.getProcessObject().get("body"));
        monitorObject.add("request", request);
        MonitorHandler.addQueue(monitorObject);
        return false;
    }
}
