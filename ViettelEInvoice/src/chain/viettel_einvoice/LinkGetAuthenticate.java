package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import socket_handler.RESTRequest;

import java.io.IOException;
import java.util.HashMap;

public class LinkGetAuthenticate extends Link {
    public LinkGetAuthenticate(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonObject sendObject = new JsonObject();
        sendObject.add("username", chain.getProcessObject().get("body").getAsJsonObject().get("username"));
        sendObject.add("password", chain.getProcessObject().get("body").getAsJsonObject().get("password"));
        HashMap<String, String> maps = new HashMap<>();
        maps.put("Content-Type", "application/json");
        String returnString;
        try {
            returnString = RESTRequest.post(Url.Authenticate.path, sendObject.toString(), maps);
        } catch (IOException e) {
            chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thể đăng nhập vào máy chủ Viettel");
            return false;
        }
        JsonObject returnJson = new Gson().fromJson(returnString, JsonObject.class);
		chain.getProcessObject().get("body").getAsJsonObject().add("cookies", returnJson.get("access_token"));
        return true;
    }
}


