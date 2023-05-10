package chain.gmail;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;
import memorable.GMail;
import system_monitor.MonitorHandler;

public class LinkSetInstances extends Link {
    public LinkSetInstances(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        GMail.getInstance().clientId = chain.getProcessObject().get("clientId").getAsString();
        GMail.getInstance().clientSecret = chain.getProcessObject().get("clientSecret").getAsString();
        GMail.getInstance().redirectUri = chain.getProcessObject().get("redirectUri").getAsString();
        JsonObject monitorObj = new JsonObject();
        monitorObj.addProperty("status", true);
        monitorObj.addProperty("notification", "Cài đặt thông tin ban đầu thành công");
        MonitorHandler.addQueue(monitorObj);
        return false;
    }
}
