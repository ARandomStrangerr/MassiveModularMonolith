package chain.smtp_mail.request_handler;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;

public class LinkFormatMessage extends Link {
    public LinkFormatMessage(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        // override the body of the
        JsonObject body = new JsonObject();
        body.add("error", chain.getProcessObject().remove("error"));
        chain.getProcessObject().add("body", body);
        // set to message
        chain.getProcessObject().get("header").getAsJsonObject().add("to", chain.getProcessObject().get("header").getAsJsonObject().remove("from"));
        return true;
    }
}
