package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;

public class LinkFormatFalseRequest extends Link {
    public LinkFormatFalseRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject body = new JsonObject();
        body.add("error", chain.getProcessObject().get("body").getAsJsonObject().remove("error"));
        chain.getProcessObject().add("body", body);
        chain.getProcessObject().get("header").getAsJsonObject().add("to", chain.getProcessObject().get("header").getAsJsonObject().remove("from"));
        chain.getProcessObject().get("header").getAsJsonObject().addProperty("status", false);
        return true;
    }
}
