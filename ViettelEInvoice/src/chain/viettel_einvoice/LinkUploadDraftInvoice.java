package chain.viettel_einvoice;

import chain.Chain;
import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import memorable.ViettelEInvoice;
import socket_handler.RESTRequest;
import system_monitor.MonitorHandler;

import java.io.IOException;
import java.util.LinkedHashMap;

public class LinkUploadDraftInvoice extends Link {
    public LinkUploadDraftInvoice(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        // http request header
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("Content-Type", "application/json");
        map.put("Cookie", String.format("access_token=" + chain.getProcessObject().get("body").getAsJsonObject().get("accessToken").getAsString()));
        // update object
        JsonObject updateObject = new JsonObject();
        JsonObject bodyUpdateObject = new JsonObject();
        updateObject.add("header", chain.getProcessObject().get("header").deepCopy());
        updateObject.get("header").getAsJsonObject().add("to", updateObject.get("header").getAsJsonObject().remove("from"));
        updateObject.add("body", bodyUpdateObject);
        // gson to convert to Json
        Gson gson = new Gson();
        // loop send data
        int index = 0;
        String username = chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString();
        for (JsonElement ele : chain.getProcessObject().get("body").getAsJsonObject().get("sendData").getAsJsonArray()) {
            index++;
            JsonObject returnObject;
            try { // send the request
                returnObject = gson.fromJson(RESTRequest.post(Url.UploadDraftInvoice.path + chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString(), ele.toString(), map), JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            JsonObject monitorObject = new JsonObject();
            if (returnObject.has("code")) {
                chain.getProcessObject().get("body").getAsJsonObject().addProperty("error", String.format("Tại dòng số %d trong tệp tin tải lên, %s", index, returnObject.get("data").getAsString()));
                return false;
            }
            monitorObject.addProperty("status", true);
            monitorObject.addProperty("notification", String.format("Đơn vị với mã số thuế %s thành công tải hoá đơn nháp thứ %d", username, index));
            MonitorHandler.addQueue(monitorObject);
            bodyUpdateObject.addProperty("update", String.format("Thành công tải lên hoá đơn nháp thứ %d", index));
            try {
                ViettelEInvoice.socketToDataStream.write(updateObject.toString());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return true;
    }
}
