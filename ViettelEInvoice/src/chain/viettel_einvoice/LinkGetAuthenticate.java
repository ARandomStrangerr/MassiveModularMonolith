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
		JsonObject returnJson;
        try {
			returnJson = new Gson().fromJson(RESTRequest.post(Url.Authenticate.path, sendObject.toString(), maps), JsonObject.class);
        } catch (IOException e) {
            chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Không thể đăng nhập vào máy chủ Viettel");
            return false;
        }
        if (returnJson.has("status") && returnJson.get("status").getAsInt() == 401) {
            chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", "Tên đăng nhập hoặc mật khẩu không chính xác");
            return false;
        }
		chain.getProcessObject().get("body").getAsJsonObject().add("accessToken", returnJson.get("access_token"));
        return true;
    }
}


