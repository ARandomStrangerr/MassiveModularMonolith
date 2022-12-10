package chain.connection_manager.request_handler;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import memorable.ConnectionManager;

import java.util.LinkedList;
import java.util.Map;

public class LinkFormatRequest extends Link {
    public LinkFormatRequest(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject header = new JsonObject(),
                body = new JsonObject();
        header.addProperty("from", ConnectionManager.getInstance().moduleName);
        header.addProperty("to", chain.getProcessObject().remove("request").getAsString());
        header.addProperty("clientId", chain.getProcessObject().remove("clientId").getAsString());
        LinkedList<String> lst = new LinkedList<>();
        for (Map.Entry<String, JsonElement> a : chain.getProcessObject().entrySet()) lst.add(a.getKey());
        for (String key:lst) body.add(key, chain.getProcessObject().remove(key));
        chain.getProcessObject().add("header", header);
        chain.getProcessObject().add("body",body);
        return true;
    }
}
