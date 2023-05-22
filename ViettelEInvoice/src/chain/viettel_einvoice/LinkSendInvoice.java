package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import socket_handler.RESTRequest;

import java.io.IOException;
import java.util.HashMap;

public class LinkSendInvoice extends Link {
    public LinkSendInvoice(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        JsonArray sendDataArray = chain.getProcessObject().get("body").getAsJsonObject().get("sendData").getAsJsonArray();
        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        map.put("Key", "Cookie");
        map.put("Value", chain.getProcessObject().get("body").getAsJsonObject().get("cookie").getAsString());
        Gson gson = new Gson();
        String returnString;
        JsonObject returnJson;
        for (JsonElement each : sendDataArray){
            try {
                returnString = RESTRequest.post(Url.UploadInvoice.path + chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString(), each.toString(), map);
            } catch (IOException e){
                // todo finish error catching
                chain.getProcessObject().addProperty("error", "Không ");
                return false;
            }
            returnJson = gson.fromJson(returnString, JsonObject.class);
            return false;
        }
        return false;
    }
}
