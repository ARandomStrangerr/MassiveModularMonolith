package chain.connection_manager;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;

public class LinkFormatFailureRequest extends Link {
    public LinkFormatFailureRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        chain.getProcessObject().get("header").getAsJsonObject().addProperty("status", false); // change status to false
        JsonObject body = new JsonObject();
        body.add("error", chain.getProcessObject().remove("error"));
        chain.getProcessObject().add("body", body);
        return true;
    }
}
