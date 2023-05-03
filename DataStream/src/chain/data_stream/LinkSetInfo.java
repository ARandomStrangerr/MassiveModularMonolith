package chain.data_stream;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.DataStream;
import system_monitor.MonitorHandler;

class LinkSetInfo extends Link {
    LinkSetInfo(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        DataStream memorableObject = DataStream.getInstance();
        memorableObject.setModuleName(chain.getProcessObject().get("moduleName").getAsString());
        JsonObject monitorObject = new JsonObject();
        monitorObject.addProperty("status", true);
        monitorObject.addProperty("notification", "declare initial information");
        MonitorHandler.addQueue(monitorObject);
        return true;
    }
}
