package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;

public class LinkFormatFailureRequest extends Link {
    public LinkFormatFailureRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject body = new JsonObject();
        body.add("error", chain.getProcessObject().get("error"));
        chain.getProcessObject().add("body", body);
        chain.getProcessObject().get("header").getAsJsonObject().addProperty("status", false);
        return true;
    }
}
